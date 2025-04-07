package com.github.familyvault.services

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
import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.utils.PayloadDecryptor
import com.github.familyvault.utils.PayloadEncryptor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.nio.charset.Charset


class NfcService(private val context: Context) : INfcService {

    private var nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    override val tags: Flow<AddFamilyMemberDataPayload> = callbackFlow {
        Log.d("NfcService", "Starting callbackFlow for reading NFC tags")
        val readerCallback = NfcAdapter.ReaderCallback { tag: Tag ->
            Log.d("NfcService", "ReaderCallback invoked with tag: $tag")
            // try reading ndef from tag
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                try {
                    Log.d("NfcService", "Connecting to NFC tag")
                    ndef.connect()
                    val ndefMessage: NdefMessage = ndef.ndefMessage
                    Log.d("NfcService", "NDEF Message received with ${ndefMessage.records.size} record(s)")
                    if (ndefMessage.records.isNotEmpty()) {
                        val payloadBytes = ndefMessage.records[0].payload
                        Log.d("NfcService", "Raw payload bytes: ${payloadBytes.joinToString()}")
                        val statusByte = payloadBytes[0].toInt()

                        val languageCodeLength = statusByte and 0x3F

                        val isUtf8 = (statusByte and 0x80) == 0
                        val charset = if (isUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")

                        val textBytes = payloadBytes.drop(1 + languageCodeLength).toByteArray()

                        val jsonString = String(textBytes, charset)
                        Log.d("NfcService", "Extracted JSON string: $jsonString")

                        val payload: AddFamilyMemberDataPayload = PayloadDecryptor.decrypt(jsonString)

                        Log.d("NfcService", "Deserialized payload: $payload")
                        trySend(payload)
                    } else {
                        Log.w("NfcService", "NDEF Message contains no records")
                    }
                } catch (e: Exception) {
                    Log.e("NfcService", "Error during NFC tag reading", e)
                } finally {
                    try {
                        ndef.close()
                        Log.d("NfcService", "NFC tag connection closed")
                    } catch (e: Exception) {
                        Log.e("NfcService", "Error closing NFC tag connection", e)
                    }
                }
            } else {
                Log.w("NfcService", "Tag does not support NDEF")
            }
            // if tag does not support ndef, try isoDep
            val isoDep = IsoDep.get(tag)
            if (isoDep != null) {
                try {
                    isoDep.connect()
                    Log.d("NfcService", "IsoDep connected")

                    // Use the same AID as defined in MyHostApduService
                    val selectCommand = byteArrayOf(
                        0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte(),
                        0x07.toByte(),
                        0xD2.toByte(), 0x76.toByte(), 0x00.toByte(),
                        0x00.toByte(), 0x85.toByte(), 0x01.toByte(), 0x01.toByte()
                    )

                    Log.d("NfcService", "Sending SELECT command: ${selectCommand.joinToString(" ")}")
                    val selectResponse = isoDep.transceive(selectCommand)
                    Log.d("NfcService", "Response from SELECT: ${selectResponse.joinToString(" ")}")

                    // Don't immediately return if response isn't OK - log it and continue
                    if (!isResponseOkay(selectResponse)) {
                        Log.w("NfcService", "SELECT command didn't return 90 00, but continuing: ${selectResponse.joinToString(" ")}")
                    }

                    val getDataCommand = byteArrayOf(
                        0x00.toByte(), // CLA
                        0xA0.toByte(), // INS - custom instruction
                        0x00.toByte(), // P1
                        0x00.toByte(), // P2
                        0x00.toByte()  // Lc - no data to send, just requesting data
                    )

                    Log.d("NfcService", "Requesting data: ${getDataCommand.joinToString(" ")}")
                    val dataResponse = isoDep.transceive(getDataCommand)
                    Log.d("NfcService", "Response from data request: ${dataResponse.joinToString(" ")}")

                    if (isResponseOkay(dataResponse)) {
                        val responseData = dataResponse.sliceArray(0 until dataResponse.size - 2)
                        val statusByte = responseData[0].toInt()

                        val languageCodeLength = statusByte and 0x3F

                        val isUtf8 = (statusByte and 0x80) == 0
                        val charset = if (isUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")

                        val textBytes = responseData.drop(1 + languageCodeLength).toByteArray()

                        val jsonString = String(textBytes, charset)
                        Log.d("NfcService", "Received data: $jsonString")

                        try {
                            val payload: AddFamilyMemberDataPayload = PayloadDecryptor.decrypt(jsonString)
                            Log.d("NfcService", "Deserialized payload: $payload")
                            trySend(payload)
                        } catch (e: Exception) {
                            Log.e("NfcService", "Error deserializing JSON", e)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("NfcService", "Error communicating with IsoDep", e)
                    e.printStackTrace()
                } finally {
                    try {
                        isoDep.close()
                        Log.d("NfcService", "IsoDep connection closed")
                    } catch (e: Exception) {
                        Log.e("NfcService", "Error closing IsoDep connection", e)
                    }
                }
            }
        }

        nfcAdapter?.enableReaderMode(
            context as Activity,
            readerCallback,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_V,
            null
        )
        Log.d("NfcService", "Reader mode enabled")

        awaitClose {
            nfcAdapter?.disableReaderMode(context as Activity)
            Log.d("NfcService", "Reader mode disabled (awaitClose)")
        }
    }
    private fun isResponseOkay(response: ByteArray): Boolean {
        if (response.size < 2) return false
        val sw1 = response[response.size - 2]
        val sw2 = response[response.size - 1]
        return sw1 == 0x90.toByte() && sw2 == 0x00.toByte()
    }

    override suspend fun registerApp() {
        Log.d("NfcService", "registerApp() invoked")
        if (nfcAdapter == null) {
            Log.w("NfcService", "NFC is not available on this device")
        } else {
            Log.d("NfcService", "NFC is available on this device")
        }
    }

    override fun unregisterApp() {
        Log.d("NfcService", "unregisterApp() invoked")
        Log.d("NfcService", "setIdleMode() invoked")
        (context as? Activity)?.let { activity ->
            nfcAdapter?.disableReaderMode(activity)
        }
        context.stopService(Intent(context, HostApduService::class.java))
        Log.d("NfcService", "Idle mode set; service stopped")
    }

    override suspend fun setEmulateMode(data: AddFamilyMemberDataPayload) {
        Log.d("NfcService", "setEmulateMode() invoked with data: $data")
        val intent = Intent(context, HostApduService::class.java)
        (context as? Activity)?.let { activity ->
            nfcAdapter?.disableReaderMode(activity)
        }
        val jsonString = PayloadEncryptor.encrypt(data)
        intent.putExtra("ndefMessage", jsonString)
        context.startService(intent)
        Log.d("NfcService", "Emulate mode set with JSON data: $jsonString")
    }

    override suspend fun setReadMode() {
        Log.d("NfcService", "setReadMode() invoked")
        val callback = NfcAdapter.ReaderCallback {  }
        (context as? Activity)?.let { activity ->
            nfcAdapter?.enableReaderMode(
                activity,
                callback,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or
                        NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V or
                        NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                Bundle().apply {
                    putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 500)
                }
            )
            Log.d("NfcService", "Read mode enabled")
        } ?: Log.e("NfcService", "Context is not an Activity")
    }

}
