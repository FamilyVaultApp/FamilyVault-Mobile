package com.github.familyvault.services

import com.github.familyvault.AppConfig
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.AVFAudio.AVAudioRecorder
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.AVFAudio.AVSampleRateKey
import platform.AVFAudio.setActive
import platform.CoreAudioTypes.kAudioFormatOpus
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithContentsOfFile
import platform.posix.memcpy
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalForeignApi::class)
class AudioRecorderService() : IAudioRecorderService{
    private var recorder: AVAudioRecorder? = null
    private var isRecording = false

    //TODO: This should be available from common source
    companion object {
        private const val RECORDING_PERMISSION_REQUEST_CODE = 1002
        private val SAMPLE_RATE = AppConfig.AUDIO_SAMPLE_RATE
        private val AUDIO_SETTINGS = mapOf<Any?,Any?>(
            AVFormatIDKey to kAudioFormatOpus,
            AVSampleRateKey to SAMPLE_RATE,
            AVNumberOfChannelsKey to 1.toUInt(),
        )
    }


    @OptIn(ExperimentalUuidApi::class)
    override fun start() {
        if (isRecording || !haveRecordingPermission()) return

        //TODO: This file name should be correct prepared after implement local cache for audio files
        val fileName = Uuid.random()
        val file = NSFileManager.defaultManager.URLForDirectory(
            NSApplicationSupportDirectory,
            NSUserDomainMask,
            null,
            true,
            null
        )?.URLByAppendingPathComponent("files/audio/recorded/$fileName.opus")
        NSFileManager.defaultManager.createDirectoryAtURL(file!!.URLByDeletingLastPathComponent!!,true,null,null)

        prepareSession()
        recorder = AVAudioRecorder(file, AUDIO_SETTINGS, null)
        recorder?.let { recorder ->
            recorder.prepareToRecord()
            recorder.record()
            isRecording = true
        }
    }

    override fun stop() : ByteArray {
        recorder?.stop()
        return NSData.dataWithContentsOfFile(recorder!!.url.path!!)!!.toByteArray()
    }

    override fun requestRecordingPermission() {
        AVAudioSession.sharedInstance().requestRecordPermission {}
    }

    override fun haveRecordingPermission(): Boolean {
        return AVAudioSession.sharedInstance().recordPermission == AVAudioSessionRecordPermissionGranted
    }

    private fun prepareSession(){
        AVAudioSession.sharedInstance().apply {
            setCategory(
                AVAudioSessionCategoryPlayAndRecord,
                null
            )
            setActive(true,null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray = memScoped {
    ByteArray(length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), bytes, length)
        }
    }
}