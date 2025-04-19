package com.github.familyvault.services

interface IAudioRecorderService {
    fun start()
    fun stop() : ByteArray
    fun requestRecordingPermission()
    fun haveRecordingPermission(): Boolean
}