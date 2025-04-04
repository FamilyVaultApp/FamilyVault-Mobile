package com.github.familyvault.ui.components

import android.app.Activity
import android.content.Intent
import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.github.familyvault.components.MyHostApduService
import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import java.nio.charset.Charset


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class NFCManager(private val context: Context) : NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    actual val tags: Flow<AddFamilyMemberDataPayload> = callbackFlow {
        Log.d("NFCManager", "Starting callbackFlow for reading NFC tags")
        val readerCallback = NfcAdapter.ReaderCallback { tag: Tag ->
            Log.d("NFCManager", "ReaderCallback invoked with tag: $tag")
            // Próba odczytania wiadomości NDEF z tagu
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                try {
                    Log.d("NFCManager", "Connecting to NFC tag")
                    ndef.connect()
                    val ndefMessage: NdefMessage = ndef.ndefMessage
                    Log.d("NFCManager", "NDEF Message received with ${ndefMessage.records.size} record(s)")
                    if (ndefMessage.records.isNotEmpty()) {
                        val payloadBytes = ndefMessage.records[0].payload
                        Log.d("NFCManager", "Raw payload bytes: ${payloadBytes.joinToString()}")
                        val statusByte = payloadBytes[0].toInt()

                        val languageCodeLength = statusByte and 0x3F

                        val isUtf8 = (statusByte and 0x80) == 0
                        val charset = if (isUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")

                        val textBytes = payloadBytes.drop(1 + languageCodeLength).toByteArray()

                        val jsonString = String(textBytes, charset)
                        Log.d("NFCManager", "Extracted JSON string: $jsonString")

                        val payload: AddFamilyMemberDataPayload = Json.decodeFromString(
                            AddFamilyMemberDataPayload.serializer(),
                            jsonString
                        )
                        Log.d("NFCManager", "Deserialized payload: $payload")
                        trySend(payload)
                    } else {
                        Log.w("NFCManager", "NDEF Message contains no records")
                    }
                } catch (e: Exception) {
                    Log.e("NFCManager", "Error during NFC tag reading", e)
                } finally {
                    try {
                        ndef.close()
                        Log.d("NFCManager", "NFC tag connection closed")
                    } catch (e: Exception) {
                        Log.e("NFCManager", "Error closing NFC tag connection", e)
                    }
                }
            } else {
                Log.w("NFCManager", "Tag does not support NDEF")
            }
            val isoDep = IsoDep.get(tag)
            if (isoDep != null) {
                try {
                    isoDep.connect()
                    Log.d("NFCManager", "IsoDep connected")

                    val selectCommand = byteArrayOf(
                        0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte(),
                        0x07.toByte(),
                        0xD2.toByte(), 0x76.toByte(), 0x00.toByte(),
                        0x00.toByte(), 0x85.toByte(), 0x01.toByte(), 0x01.toByte()
                    )
                    Log.d("NFCManager", "Sending SELECT command: ${selectCommand.joinToString(" ")}")
                    val selectResponse = isoDep.transceive(selectCommand)
                    Log.d("NFCManager", "Response from SELECT: ${selectResponse.joinToString(" ")}")

                    if (!isResponseOkay(selectResponse)) {
                        Log.e("NFCManager", "SELECT command failed")
                    }
                    val readLengthCommand = byteArrayOf(
                        0x00.toByte(), 0xB0.toByte(), 0x00.toByte(), 0x00.toByte(), 0x02.toByte()
                    )
                    Log.d("NFCManager", "Sending READ LENGTH command: ${readLengthCommand.joinToString(" ")}")
                    val lengthResponse = isoDep.transceive(readLengthCommand)
                    Log.d("NFCManager", "Length response: ${lengthResponse.joinToString(" ")}")
                    if (!isResponseOkay(lengthResponse) || lengthResponse.size < 4) {
                        Log.e("NFCManager", "Failed to read length")
                    }
                    val lengthBytes = lengthResponse.sliceArray(0 until lengthResponse.size - 2)
                    val dataLength = ((lengthBytes[0].toInt() and 0xFF) shl 8) or (lengthBytes[1].toInt() and 0xFF)
                    Log.d("NFCManager", "Data length: $dataLength")
                    val readDataCommand = byteArrayOf(
                        0x00.toByte(), 0xB0.toByte(), 0x00.toByte(), 0x02.toByte(),
                        dataLength.toByte()
                    )

                    Log.d("NFCManager", "Sending READ DATA command: ${readDataCommand.joinToString(" ")}")
                    val dataResponse = isoDep.transceive(readDataCommand)
                    Log.d("NFCManager", "Data response: ${dataResponse.joinToString(" ")}")
                    if (!isResponseOkay(dataResponse)) {
                        Log.e("NFCManager", "Failed to read data")
                    }

                    val jsonBytes = dataResponse.sliceArray(0 until dataResponse.size - 2)
                    val jsonString = jsonBytes.toString(Charset.forName("UTF-8"))
                    Log.d("NFCManager", "Received JSON string: $jsonString")

                    val payload = Json.decodeFromString(AddFamilyMemberDataPayload.serializer(), jsonString)
                    Log.d("NFCManager", "Deserialized payload: $payload")

                    trySend(payload)
                } catch (e: Exception) {
                    Log.e("NFCManager", "Error communicating with IsoDep", e)
                } finally {
                    try {
                        isoDep.close()
                        Log.d("NFCManager", "IsoDep connection closed")
                    } catch (e: Exception) {
                        Log.e("NFCManager", "Error closing IsoDep connection", e)
                    }
                }
            } else {
                Log.w("NFCManager", "Tag does not support IsoDep")
            }
        }

        nfcAdapter?.enableReaderMode(
            context as Activity,
            readerCallback,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_V,
            null
        )
        Log.d("NFCManager", "Reader mode enabled")

        awaitClose {
            nfcAdapter?.disableReaderMode(context as Activity)
            Log.d("NFCManager", "Reader mode disabled (awaitClose)")
        }
    }
    private fun isResponseOkay(response: ByteArray): Boolean {
        if (response.size < 2) return false
        val sw1 = response[response.size - 2]
        val sw2 = response[response.size - 1]
        return sw1 == 0x90.toByte() && sw2 == 0x00.toByte()
    }

    @Composable
    actual fun RegisterApp() {
        Log.d("NFCManager", "registerApp() invoked")
        if (nfcAdapter == null) {
            Log.w("NFCManager", "NFC is not available on this device")
        } else {
            Log.d("NFCManager", "NFC is available on this device")
        }
    }

    @Composable
    actual fun UnregisterApp() {
        Log.d("NFCManager", "unregisterApp() invoked")
        Log.d("NFCManager", "setIdleMode() invoked")
        nfcAdapter?.disableReaderMode(LocalContext.current as Activity)
        context.stopService(Intent(context, MyHostApduService::class.java))
        Log.d("NFCManager", "Idle mode set; service stopped")
    }

    @Composable
    actual fun SetEmulateMode(data: AddFamilyMemberDataPayload) {
        Log.d("NFCManager", "setEmulateMode() invoked with data: $data")
        val intent = Intent(context, MyHostApduService::class.java)
        nfcAdapter?.disableReaderMode(LocalContext.current as Activity)
        val jsonString = Json.encodeToString(AddFamilyMemberDataPayload.serializer(), data)
        intent.putExtra("ndefMessage", jsonString)
        context.startService(intent)
        Log.d("NFCManager", "Emulate mode set with JSON data: $jsonString")
    }

    @Composable
    actual fun SetReadMode() {
        Log.d("NFCManager", "setReadMode() invoked")
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
        Log.d("NFCManager", "Read mode enabled")
    }


    override fun onTagDiscovered(tag: Tag?) {
        Log.d("NFCManager", "onTagDiscovered() invoked with tag: $tag")
    }
}

@Composable
actual fun getNFCManager(): NFCManager {
    Log.d("NFCManager", "getNFCManager() invoked")
    return NFCManager(context = LocalContext.current)
}
