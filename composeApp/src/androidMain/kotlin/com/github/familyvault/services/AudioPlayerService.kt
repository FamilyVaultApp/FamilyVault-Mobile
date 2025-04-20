package com.github.familyvault.services

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.github.familyvault.AppConfig
import kotlinx.coroutines.*

class AudioPlayerService : IAudioPlayerService {

    private var audioTrack: AudioTrack? = null
    private var playingJob: Job? = null

    private val audioPlayerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun play(audioData: ByteArray, onCompletion: (() -> Unit)?) {
        stop()

        val sampleRate = AppConfig.AUDIO_SAMPLE_RATE
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        val totalSamples = audioData.size / 2

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(audioFormat)
                    .setSampleRate(sampleRate)
                    .setChannelMask(channelConfig)
                    .build()
            )
            .setBufferSizeInBytes(minBufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack?.apply {
            setNotificationMarkerPosition(totalSamples)
            setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(track: AudioTrack?) {
                    audioPlayerScope.launch {
                        stop()
                        onCompletion?.invoke()
                    }
                }

                override fun onPeriodicNotification(track: AudioTrack?) {}
            })

            play()

            playingJob = audioPlayerScope.launch(Dispatchers.IO) {
                write(audioData, 0, audioData.size)
            }
        }
    }

    override fun stop() {
        playingJob?.cancel()
        playingJob = null

        audioTrack?.apply {
            stop()
            release()
        }
        audioTrack = null
    }
}
