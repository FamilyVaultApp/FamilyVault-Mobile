package com.github.familyvault.services

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioPlayerDelegateProtocol
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryOptionDefaultToSpeaker
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.setActive
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.darwin.NSObject

class AudioPlayerService : IAudioPlayerService {
    private var audioPlayer: AVAudioPlayer? = null
    private var listener: AudioPlayerListener? = null

    @OptIn(ExperimentalForeignApi::class)
    override fun play(audioData: ByteArray, onCompletion: (() -> Unit)?) {
        prepareAudioSession()
        audioPlayer = AVAudioPlayer(data = audioData.asData(), null)
        audioPlayer?.prepareToPlay()
        audioPlayer?.play()
        listener = AudioPlayerListener {
            onCompletion?.invoke()
        }
        audioPlayer?.delegate = listener
    }

    override fun stop() {
        audioPlayer?.stop()
        audioPlayer = null
        listener = null
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun prepareAudioSession() {
        AVAudioSession.sharedInstance().apply {
            setCategory(
                AVAudioSessionCategoryPlayAndRecord,
                AVAudioSessionCategoryOptionDefaultToSpeaker,
                null
            )
            setActive(true, null)
        }
    }

}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.asData(): NSData = usePinned {
    NSData.dataWithBytes(it.addressOf(0), size.toULong())
}

class AudioPlayerListener(
    private val didFinish: () -> Unit = {}
) : AVAudioPlayerDelegateProtocol, NSObject() {
    override fun audioPlayerDidFinishPlaying(player: AVAudioPlayer, successfully: Boolean) {
        didFinish()
    }
}