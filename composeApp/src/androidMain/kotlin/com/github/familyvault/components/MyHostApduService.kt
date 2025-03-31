package com.github.familyvault.components

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.serialization.json.Json

class MyHostApduService : HostApduService() {

    companion object {
        // Prefiks komendy SELECT APDU (część stała)
        private val SELECT_APDU = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)
        // Przykładowy NDEF AID (zgodny z NFC Forum Type 4 Tag)
        private val NDEF_AID = byteArrayOf(0xD2.toByte(), 0x76, 0x00, 0x00, 0x85.toByte(), 0x01, 0x01)
        // Słowo statusu potwierdzające powodzenie: 0x90 0x00
        private val STATUS_OK = byteArrayOf(0x90.toByte(), 0x00)
        // Słowo statusu błędu, np. gdy żądany plik (dane) nie są dostępne
        private val STATUS_FAIL = byteArrayOf(0x6A.toByte(), 0x82.toByte())
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        Log.d("MyHostApduService", "Otrzymano APDU: ${commandApdu.toHexString()}")

        // Pobierz dane do wysłania – załóżmy, że NFCManager.dataToShare ma typ AddFamilyMemberDataPayload
        val dataToShare: AddFamilyMemberDataPayload? = NFCManager.dataToShare
        if (dataToShare == null) {
            Log.d("MyHostApduService", "Brak danych do udostępnienia")
            return STATUS_FAIL
        }

        // Sprawdź, czy komenda jest komendą SELECT z naszym AID.
        // Oczekujemy, że pełna komenda SELECT będzie zawierała:
        // SELECT_APDU + NDEF_AID (pomijając dodatkowe bajty Lc i Le, które mogą być obecne)
        if (commandApdu.size >= SELECT_APDU.size + NDEF_AID.size &&
            commandApdu.sliceArray(0 until SELECT_APDU.size).contentEquals(SELECT_APDU) &&
            commandApdu.sliceArray(SELECT_APDU.size until SELECT_APDU.size + NDEF_AID.size)
                .contentEquals(NDEF_AID)
        ) {
            try {
                // Serializuj obiekt payload do JSON
                val jsonString = Json.encodeToString(dataToShare)
                Log.d("MyHostApduService", "Serializowany payload: $jsonString")
                // Utwórz rekord NDEF typu tekstowego (kodowanie UTF-8, język "en")
                val textRecord = NdefRecord.createTextRecord("en", jsonString)
                // Utwórz wiadomość NDEF z pojedynczym rekordem
                val ndefMessage = NdefMessage(arrayOf(textRecord))
                val ndefBytes = ndefMessage.toByteArray()
                Log.d("MyHostApduService", "NDEF message bytes: ${ndefBytes.toHexString()}")
                // Dołącz słowo statusu OK do wiadomości i zwróć wynik
                return ndefBytes + STATUS_OK
            } catch (e: Exception) {
                Log.e("MyHostApduService", "Błąd przy przetwarzaniu payload: ${e.localizedMessage}")
                return STATUS_FAIL
            }
        }
        Log.d("MyHostApduService", "Nie rozpoznano komendy SELECT dla naszego AID")
        return STATUS_FAIL
    }

    override fun onDeactivated(reason: Int) {
        Log.d("MyHostApduService", "Usługa HCE dezaktywowana, powód: $reason")
    }
}

// Rozszerzenie do konwersji tablicy bajtów na ciąg hex
fun ByteArray.toHexString(): String = joinToString(separator = "") { String.format("%02X", it) }
