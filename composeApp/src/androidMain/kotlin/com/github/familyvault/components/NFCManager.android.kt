package com.github.familyvault.components

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
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

// Enum to manage NFC modes
enum class NFCMode {
    Emulate,
    Read,
    Idle
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NFCManager : NfcAdapter.ReaderCallback {

    private val _tags = MutableSharedFlow<AddFamilyMemberDataPayload>()
    actual val tags: SharedFlow<AddFamilyMemberDataPayload> = _tags

    // Flow for write status (optional, kept for compatibility)
    private val _writeStatus = MutableSharedFlow<NFCWriteStatus>()
    actual val writeStatus: SharedFlow<NFCWriteStatus> = _writeStatus

    private var nfcAdapter: NfcAdapter? = null
    private val scope = CoroutineScope(SupervisorJob())
    private var currentMode: NFCMode = NFCMode.Idle

    companion object {
        var dataToShare: AddFamilyMemberDataPayload? = null
            private set
    }

    @Composable
    actual fun registerApp() {
        val context = LocalContext.current
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    }

    @Composable
    actual fun unregisterApp() {
        setIdleMode()
    }

    // Set the phone to emulate an NFC tag with the given data
    @Composable
    actual fun setEmulateMode(data: AddFamilyMemberDataPayload) {
        dataToShare = data
        nfcAdapter?.disableReaderMode(LocalContext.current as Activity)
        currentMode = NFCMode.Emulate
        Log.d("NFCManager", "Emulate mode set with data: $data")
    }

    // Set the phone to read NFC tags
    @Composable
    actual fun setReadMode() {
        dataToShare = null
        nfcAdapter?.enableReaderMode(
            LocalContext.current as Activity,
            this,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
            Bundle().apply {
                putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 500)
            }
        )
        currentMode = NFCMode.Read
        Log.d("NFCManager", "Read mode enabled")
    }

    // Set the phone to idle (neither emulating nor reading)
    @Composable
    actual fun setIdleMode() {
        dataToShare = null
        nfcAdapter?.disableReaderMode(LocalContext.current as Activity)
        currentMode = NFCMode.Idle
        Log.d("NFCManager", "Idle mode set")
    }

    // Reader callback: only handle reading
    override fun onTagDiscovered(tag: Tag?) {
        Log.d("NFCManager", "Tag discovered: $tag")

        val isoDep = IsoDep.get(tag)
        if (isoDep == null) {
            Log.e("NFCManager", "isoDep is NULL, aborted")
            return
        } else {
            Log.i("NFCManager", "isoDep is available")
        }

        val tagId = isoDep.tag.id
        Log.d("NFCManager", "Tag ID: ${tagId.joinToString(":") { "%02x".format(it) }}")

        try {
            isoDep.connect()
            var command = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)
            var response = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)

            val aidString = "D2760000850101"
            val aid = aidString.toByteArray()
        } catch (e: Exception) {
            Log.e("NFCManager", "Error connecting to tag: ${e.message}", e)
            e.printStackTrace()
        }
        tag?.let {
            readFromTag(it)
        } ?: Log.w("NFCManager", "Tag is null")
    }

    private fun readFromTag(tag: Tag) {
        try {
            Log.d("NFCManager", "Tag discovered: $tag, ID: ${tag.id.joinToString(":") { "%02x".format(it) }}")
            val ndef = Ndef.get(tag)
            if (ndef == null) {
                Log.w("NFCManager", "NDEF not supported by this tag")
                return
            }
            Log.d("NFCManager", "NDEF supported, connecting...")
            ndef.connect()
            val message = ndef.cachedNdefMessage
            if (message == null) {
                Log.w("NFCManager", "No NDEF message found")
                ndef.close()
                return
            }
            val records = message.records
            if (records.isEmpty()) {
                Log.w("NFCManager", "NDEF message has no records")
                ndef.close()
                return
            }
            Log.d("NFCManager", "Found ${records.size} records, processing...")
            records.forEach { record ->
                Log.d("NFCManager", "Processing record: TNF=${record.tnf}, URI=${record.toUri()}")
                if (record.toUri() == null && record.tnf == NdefRecord.TNF_WELL_KNOWN) {
                    Log.d("NFCManager", "Record matches criteria: TNF_WELL_KNOWN and no URI")
                    val payload = record.payload
                    if (payload != null && payload.isNotEmpty()) {
                        Log.d("NFCManager", "Payload found, length: ${payload.size}")
                        val languageCodeLength = payload[0].toInt() and 0xFF
                        if (payload.size > languageCodeLength + 1) {
                            val jsonBytes = payload.copyOfRange(1 + languageCodeLength, payload.size)
                            val jsonString = String(jsonBytes, Charsets.UTF_8)
                            Log.d("NFCManager", "Extracted JSON string: $jsonString")
                            val memberData = Json.decodeFromString<AddFamilyMemberDataPayload>(jsonString)
                            Log.d("NFCManager", "Parsed data: $memberData")
                            scope.launch {
                                _tags.emit(memberData)
                                Log.d("NFCManager", "Read data: $memberData")
                            }
                        } else {
                            Log.w("NFCManager", "Payload too short for language code")
                        }
                    } else {
                        Log.w("NFCManager", "Payload is null or empty")
                    }
                } else {
                    Log.d("NFCManager", "Record skipped: does not match criteria")
                }
            }
            ndef.close()
        } catch (e: Exception) {
            Log.e("NFCManager", "Error in readFromTag: ${e.message}", e)
            scope.launch {
                _tags.emit(
                    AddFamilyMemberDataPayload(
                        newMemberData = NewFamilyMemberData("", "", PublicPrivateKeyPair("", "")),
                        joinStatusToken = ""
                    )
                )
            }
        }
    }
}

@Composable
actual fun getNFCManager(): NFCManager {
    return NFCManager()
}