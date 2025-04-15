package com.github.familyvault.services

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.*

class AudioPlayerService(
    private val context: Context
) : IAudioPlayerService {

    private var audioTrack: AudioTrack? = null
    private var playingJob: Job? = null

    override fun play(audioData: ByteArray, onCompletion: (() -> Unit)?) {
        stop()

        val sampleRate = 44100
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        val totalSamples = audioData.size / 2

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
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
                    CoroutineScope(Dispatchers.Main).launch {
                        onCompletion?.invoke()
                    }
                }

                override fun onPeriodicNotification(track: AudioTrack?) {}
            })

            play()

            playingJob = CoroutineScope(Dispatchers.IO).launch {
                write(audioData, 0, audioData.size)
            }
        }
    }

    override fun stop() {
        playingJob?.cancel()
        playingJob = null

        audioTrack?.apply {
            try {
                stop()
            } catch (_: Exception) {
            }
            release()
        }
        audioTrack = null
    }
}