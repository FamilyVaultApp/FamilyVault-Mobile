package com.github.familyvault.services

import android.content.Context
import android.media.MediaRecorder
import android.os.Build

class AudioRecorder(
    private val context: Context
) : IAudioRecorder {

    private var recorder: MediaRecorder? = null

    @Suppress("DEPRECATION")
    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    override fun start(outputFilePath: String) {
        recorder = createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFilePath)

            prepare()
            start()
        }
    }

    override fun stop() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }
}