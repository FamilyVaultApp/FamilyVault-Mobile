package com.github.familyvault.services

interface IAudioRecorder {
    fun start(outputFilePath: String)
    fun stop()
}