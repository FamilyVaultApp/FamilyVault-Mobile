package com.github.familyvault.services

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Build

class AudioPlayer(
    private val context: Context
) : IAudioPlayer {

    private var player: MediaPlayer? = null

    @SuppressLint("NewApi")
    override fun playFile(filePath: String) {
        stop()

        player = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaPlayer(context)
        } else {
            @Suppress("DEPRECATION")
            MediaPlayer()
        }

        try {
            player?.apply {
                setDataSource(filePath)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        player?.apply {
            if (isPlaying) stop()
            release()
        }
        player = null
    }
}