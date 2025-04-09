package com.github.familyvault.services

interface IAudioPlayerService {
    fun playFile(filePath: String)
    fun stop()
}