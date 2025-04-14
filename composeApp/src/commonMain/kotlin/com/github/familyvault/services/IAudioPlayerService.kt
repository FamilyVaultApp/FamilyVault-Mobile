package com.github.familyvault.services

interface IAudioPlayerService {
    fun play(audioData: ByteArray)
    fun stop()
}