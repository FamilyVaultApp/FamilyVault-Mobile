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

// Enum to manage NFC modes
enum class NFCMode {
    Emulate,
    Read,
    Idle
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NFCManager : NfcAdapter.ReaderCallback {
    // Flow for reading tags
    private val _tags = MutableSharedFlow<AddFamilyMemberDataPayload>()
    actual val tags: SharedFlow<AddFamilyMemberDataPayload> = _tags

    // Flow for write status (optional, kept for compatibility)
    private val _writeStatus = MutableSharedFlow<NFCWriteStatus>()
    actual val writeStatus: SharedFlow<NFCWriteStatus> = _writeStatus

    private var nfcAdapter: NfcAdapter? = null
    private val scope = CoroutineScope(SupervisorJob())
    private var currentMode: NFCMode = NFCMode.Idle

    // Companion object to share data with HCE service
    companion object {
        var dataToShare: AddFamilyMemberDataPayload? = null
            private set
    }

    @Composable
    actual fun registerApp() {
        val context = LocalContext.current
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        // Don’t enable reader mode here; let setReadMode() handle it
    }

    @Composable
    actual fun unregisterApp() {
        nfcAdapter?.disableReaderMode(LocalContext.current as Activity)
        currentMode = NFCMode.Idle
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
        tag?.let {
            readFromTag(it)
        } ?: Log.w("NFCManager", "Tag is null")
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
                                val jsonBytes = payload.copyOfRange(1 + languageCodeLength, payload.size)
                                val jsonString = String(jsonBytes, Charsets.UTF_8)
                                val memberData = Json.decodeFromString<AddFamilyMemberDataPayload>(jsonString)
                                scope.launch {
                                    _tags.emit(memberData)
                                    Log.d("NFCManager", "Read data: $memberData")
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
                    AddFamilyMemberDataPayload(
                        newMemberData = NewFamilyMemberData("", "", PublicPrivateKeyPair("", "")),
                        joinStatusToken = ""
                    )
                )
                Log.e("NFCManager", "Read error: ${e.message}")
            }
        }
    }
}

@Composable
actual fun getNFCManager(): NFCManager {
    return NFCManager()
}