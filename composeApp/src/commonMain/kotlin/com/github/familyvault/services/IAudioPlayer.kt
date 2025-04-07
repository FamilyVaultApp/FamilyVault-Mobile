package com.github.familyvault.services

interface IAudioPlayer {
    fun playFile(filePath: String)
    fun stop()
}