package com.github.familyvault.services

interface IAudioRecorderService {
    fun start(outputFilePath: String)
    fun stop()
    fun retrieveFilePath() : String
    fun requestRecordingPermission()
    fun checkRecordingPermission(): Boolean
}