package com.github.familyvault.components

import android.nfc.cardemulation.HostApduService
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.os.Bundle
import android.util.Log
import kotlinx.serialization.json.Json

class MyHostApduService : HostApduService() {
    companion object {
        private val SELECT_APDU = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00) // SELECT command prefix
        private val NDEF_AID = byteArrayOf(0xD2.toByte(), 0x76, 0x00, 0x00, 0x85.toByte(), 0x01, 0x01) // NDEF AID
        private val STATUS_OK = byteArrayOf(0x90.toByte(), 0x00) // Success status
        private val STATUS_FAIL = byteArrayOf(0x6A.toByte(), 0x82.toByte()) // File not found
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray? {
        Log.d("HCE", "Received APDU: ${commandApdu.joinToString { it.toString(16) }}")
        val data = NFCManager.dataToShare ?: return STATUS_FAIL

        // Check if this is a SELECT AID command for our NDEF AID
        if (commandApdu.size >= SELECT_APDU.size + NDEF_AID.size &&
            commandApdu.copyOfRange(0, SELECT_APDU.size).contentEquals(SELECT_APDU) &&
            commandApdu.copyOfRange(SELECT_APDU.size, SELECT_APDU.size + NDEF_AID.size).contentEquals(NDEF_AID)) {
            // Create NDEF message
            val jsonString = Json.encodeToString(data)
            val textRecord = NdefRecord.createTextRecord("en", jsonString)
            val ndefMessage = NdefMessage(arrayOf(textRecord))
            val ndefBytes = ndefMessage.toByteArray()
            // Return NDEF data with success status
            return ndefBytes + STATUS_OK
        }
        return STATUS_FAIL
    }

    override fun onDeactivated(reason: Int) {
        Log.d("HCE", "Deactivated: $reason")
    }
}