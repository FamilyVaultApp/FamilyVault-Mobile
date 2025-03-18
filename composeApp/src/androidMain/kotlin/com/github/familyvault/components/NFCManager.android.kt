package com.github.familyvault.components

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.github.familyvault.models.FamilyMemberJoinStatus
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.models.enums.JoinTokenStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NFCManager : NfcAdapter.ReaderCallback {
    // Odczyt
    private val _tags = MutableSharedFlow<NewFamilyMemberData>()
    actual val tags: SharedFlow<NewFamilyMemberData> = _tags

    // Zapis
    private val _writeStatus = MutableSharedFlow<NFCWriteStatus>()
    actual val writeStatus: SharedFlow<NFCWriteStatus> = _writeStatus

    private var nfcAdapter: NfcAdapter? = null
    private var dataToWrite: NewFamilyMemberData? = null
    private val scope = CoroutineScope(SupervisorJob())

    @Composable
    actual fun registerApp() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(LocalContext.current)
        nfcAdapter?.enableReaderMode(
            LocalContext.current as Activity,
            this,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
            Bundle().apply {
                putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 500)
            }
        )
    }

    actual fun prepareWrite(data: NewFamilyMemberData) {
        dataToWrite = data
    }

    override fun onTagDiscovered(tag: Tag?) {
        tag?.let {
            if (dataToWrite != null) {
                writeToTag(it, dataToWrite!!)
            } else {
                readFromTag(it)
            }
        }
    }

    private fun readFromTag(tag: Tag) {
        try {
            Ndef.get(tag)?.let { ndef ->
                ndef.connect()
                ndef.cachedNdefMessage?.records?.forEach { record ->
                    if (record.toUri() == null && record.tnf == NdefRecord.TNF_WELL_KNOWN) {
                        val payload = record.payload
                        if (payload != null && payload.isNotEmpty()) {
                            val languageCodeLength = payload[0].toInt() and 0xFF
                            if (payload.size > languageCodeLength + 1) {
                                val jsonBytes = payload.copyOfRange(
                                    1 + languageCodeLength,
                                    payload.size
                                )
                                val jsonString = String(jsonBytes, Charsets.UTF_8)

                                val json = Json {
                                    ignoreUnknownKeys = true
                                    encodeDefaults = true
                                }

                                try {
                                    val memberData = json.decodeFromString<NewFamilyMemberData>(jsonString)
                                    scope.launch {
                                        _tags.emit(memberData)
                                    }
                                } catch (e: Exception) {
                                    scope.launch {
                                        _tags.emit(
                                            NewFamilyMemberData(
                                                firstname = "",
                                                surname = "",
                                                keyPair = PublicPrivateKeyPair("", ""),
                                                joinStatus = FamilyMemberJoinStatus(
                                                    token = "",
                                                    status = JoinTokenStatus.Error,
                                                    info = null
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                ndef.close()
            }
        } catch (e: Exception) {
            scope.launch {
                _tags.emit(
                    NewFamilyMemberData(
                        firstname = "",
                        surname = "",
                        keyPair = PublicPrivateKeyPair("", ""),
                        joinStatus = FamilyMemberJoinStatus(
                            token = "",
                            status = JoinTokenStatus.Error,
                            info = null
                        )
                    )
                )
            }
        }
    }

    private fun writeToTag(tag: Tag, data: NewFamilyMemberData) {
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

    private fun createNdefMessage(data: NewFamilyMemberData): NdefMessage {
        val textRecord = NdefRecord.createTextRecord("en", data.toString())
        return NdefMessage(arrayOf(textRecord))
    }
}

@Composable
actual fun getNFCManager(): NFCManager {
    return NFCManager()
}