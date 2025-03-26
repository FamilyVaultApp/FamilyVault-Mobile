package com.github.familyvault.components

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.models.PublicPrivateKeyPair
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.Arrays

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NFCManager : NfcAdapter.ReaderCallback {
    // Odczyt
    private val _tags = MutableSharedFlow<AddFamilyMemberDataPayload>()
    actual val tags: SharedFlow<AddFamilyMemberDataPayload> = _tags

    // Zapis
    private val _writeStatus = MutableSharedFlow<NFCWriteStatus>()
    actual val writeStatus: SharedFlow<NFCWriteStatus> = _writeStatus

    private var nfcAdapter: NfcAdapter? = null
    private var dataToWrite: AddFamilyMemberDataPayload? = null
    private val scope = CoroutineScope(SupervisorJob())

    @Composable
    actual fun registerApp() {
        val context = LocalContext.current
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        nfcAdapter?.enableReaderMode(
            context as Activity,
            this,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS or NfcAdapter.FLAG_READER_NFC_BARCODE,
            Bundle().apply {
                putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 500)
            }
        )
        Log.d("NFCManager", "NFC adapter: $nfcAdapter, is enabled: ${nfcAdapter?.isEnabled}")
    }
    @Composable
    actual fun unregisterApp() {
        nfcAdapter?.disableReaderMode(LocalContext.current as Activity)
    }

    actual fun prepareWrite(data: AddFamilyMemberDataPayload) {
        dataToWrite = data
    }

    override fun onTagDiscovered(tag: Tag?) {
        Log.d("NFCManager", "Tag discovered: $tag")
        tag?.let {
            if (dataToWrite != null) {
                Log.d("NFCManager", "Writing to tag with data: $dataToWrite")
                writeToTag(it, dataToWrite!!)
            } else {
                Log.d("NFCManager", "Reading from tag...")
                readFromTag(it)
            }
        } ?: Log.w("NFCManager", "Tag is null")
    }

    private fun readFromTag(tag: Tag) {
        try {
            Log.d("NFCManager", "Reading tag: ${tag.id.toHexString()}")
            Ndef.get(tag)?.let { ndef ->
                try {
                    ndef.connect()
                    val ndefMessage = ndef.cachedNdefMessage
                    if (ndefMessage == null) {
                        Log.w("NFCManager", "No NDEF message found on tag")
                        emitErrorPayload("No NDEF message found on tag")
                        return
                    }
                    
                    val records = ndefMessage.records
                    if (records.isEmpty()) {
                        Log.w("NFCManager", "NDEF message has no records")
                        emitErrorPayload("NDEF message has no records")
                        return
                    }
                    
                    Log.d("NFCManager", "Found ${records.size} NDEF records")
                    
                    // Try to find a text record with our data
                    for (record in records) {
                        Log.d("NFCManager", "Record TNF: ${record.tnf}, type: ${String(record.type)}")
                        
                        if (record.tnf == NdefRecord.TNF_WELL_KNOWN && 
                            Arrays.equals(record.type, NdefRecord.RTD_TEXT)) {
                            val payload = record.payload
                            if (payload != null && payload.isNotEmpty()) {
                                val languageCodeLength = payload[0].toInt() and 0xFF
                                if (payload.size > languageCodeLength + 1) {
                                    val jsonBytes = payload.copyOfRange(1 + languageCodeLength, payload.size)
                                    val jsonString = String(jsonBytes, Charsets.UTF_8)
                                    Log.d("NFCManager", "Found JSON data: $jsonString")
                                    
                                    try {
                                        val memberData = Json.decodeFromString<AddFamilyMemberDataPayload>(jsonString)
                                        scope.launch {
                                            _tags.emit(memberData)
                                        }
                                        return
                                    } catch (e: Exception) {
                                        Log.e("NFCManager", "Failed to parse JSON: ${e.message}")
                                        // Continue to next record if parsing fails
                                    }
                                }
                            }
                        }
                    }
                    
                    // If we get here, we didn't find a valid record
                    Log.w("NFCManager", "No valid family member data found in any record")
                    emitErrorPayload("No valid family member data found")
                } finally {
                    try {
                        ndef.close()
                    } catch (e: Exception) {
                        Log.e("NFCManager", "Error closing NDEF connection: ${e.message}")
                    }
                }
            } ?: run {
                Log.e("NFCManager", "NDEF is not supported on this tag")
                emitErrorPayload("NDEF not supported on this tag")
            }
        } catch (e: Exception) {
            Log.e("NFCManager", "Read error: ${e.message}", e)
            emitErrorPayload("Read error: ${e.message ?: "Unknown error"}")
        }
    }
    
    private fun emitErrorPayload(errorMessage: String) {
        scope.launch {
            Log.e("NFCManager", errorMessage)
            _tags.emit(
                AddFamilyMemberDataPayload(
                    newMemberData = NewFamilyMemberData("", "", PublicPrivateKeyPair("", "")),
                    joinStatusToken = ""
                )
            )
        }
    }
    
    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02X".format(it) }
    }

    private fun writeToTag(tag: Tag, data: AddFamilyMemberDataPayload) {
        scope.launch {
            try {
                val ndef = Ndef.get(tag)
                ndef?.connect()
                if (ndef?.isWritable == true) {
                    val message = createNdefMessage(data)
                    ndef.writeNdefMessage(message)
                    _writeStatus.emit(NFCWriteStatus.Success)
                } else {
                    _writeStatus.emit(NFCWriteStatus.Error("Tag nie jest zapisywalny"))
                }
                ndef?.close()
                dataToWrite = null
            } catch (e: Exception) {
                _writeStatus.emit(NFCWriteStatus.Error("Błąd zapisu: ${e.message ?: "Nieznany błąd"}"))
            }
        }
    }

    private fun createNdefMessage(data: AddFamilyMemberDataPayload): NdefMessage {
        val jsonString = Json.encodeToString(data)
        val textRecord = NdefRecord.createTextRecord("en", jsonString)
        return NdefMessage(arrayOf(textRecord))
    }
}

@Composable
actual fun getNFCManager(): NFCManager {
    return NFCManager()
}