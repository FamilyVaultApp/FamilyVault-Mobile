package com.github.familyvault.services

import com.github.familyvault.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AudioRecorderService(
) : IAudioRecorderService {

    private var isRecording = false
    private var recordingJob: Job? = null

    private val bufferSize = 0

    companion object {
        private const val RECORDING_PERMISSION_REQUEST_CODE = 1002
        private val SAMPLE_RATE = AppConfig.AUDIO_SAMPLE_RATE
    }

    override fun start() {
        TODO("Method not yet implemented")
    }

    override fun stop() : ByteArray {
        TODO("Method not yet implemented")
    }

    override fun requestRecordingPermission() {
        TODO("Method not yet implemented")
    }

    override fun haveRecordingPermission(): Boolean {
        TODO("Method not yet implemented")
    }
}