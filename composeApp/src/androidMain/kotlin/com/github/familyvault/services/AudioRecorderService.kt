package com.github.familyvault.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat

class AudioRecorderService(
    private val context: Context
) : IAudioRecorderService {

    private var recorder: MediaRecorder? = null
    private var isRecording = false

    companion object {
        private const val RECORDING_PERMISSION_REQUEST_CODE = 1002
    }

    @Suppress("DEPRECATION")
    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    override fun start(outputFilePath: String) {
        if (isRecording) return

        recorder = createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFilePath)

            prepare()
            start()
        }

        isRecording = true
    }

    override fun stop() {
        if (!isRecording) return

        try {
            recorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            recorder = null
            isRecording = false
        }
    }

    override fun retrieveFilePath() : String {
        return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString() + "/FamilyVault_Recording" + System.currentTimeMillis().toInt()
    }

    override fun requestRecordingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!checkRecordingPermission()) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    RECORDING_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun checkRecordingPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
                )
    }
}