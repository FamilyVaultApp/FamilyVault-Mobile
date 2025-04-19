package com.github.familyvault.services

interface IAudioRecorderService {
    fun start()
    fun stop()
    fun getAudioBytes(): ByteArray
    fun requestRecordingPermission()
    fun checkRecordingPermission(): Boolean
}