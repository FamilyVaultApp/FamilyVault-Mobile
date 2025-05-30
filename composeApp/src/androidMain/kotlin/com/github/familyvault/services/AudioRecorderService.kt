package com.github.familyvault.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import com.github.familyvault.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AudioRecorderService(
    private val context: Context
) : IAudioRecorderService {

    private var isRecording = false
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private var outputStream = ByteArrayOutputStream()

    private val bufferSize = AudioRecord.getMinBufferSize(
        SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    companion object {
        private const val RECORDING_PERMISSION_REQUEST_CODE = 1002
        private val SAMPLE_RATE = AppConfig.AUDIO_SAMPLE_RATE
    }

    override fun start() {
        if (isRecording || !haveRecordingPermission())
        {
            return
        }

        outputStream = ByteArrayOutputStream()

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()
        isRecording = true

        recordingJob = CoroutineScope(Dispatchers.IO).launch {
            val buffer = ByteArray(bufferSize)
            while (isRecording) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    outputStream.write(buffer, 0, read)
                }
            }
        }
    }

    override fun stop() : ByteArray {
        if (!isRecording)
        {
            return ByteArray(0)
        }
        recordingJob?.cancel()

        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null

        outputStream.flush()
        outputStream.close()

        isRecording = false

        return outputStream.toByteArray()
    }

    override fun requestRecordingPermission() {
        if (!haveRecordingPermission()) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORDING_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun haveRecordingPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
}