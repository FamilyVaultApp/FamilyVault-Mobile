package com.github.familyvault.services

interface IMediaPickerService {
    val selectedMediaUrl: MutableList<String>

    fun pickMedia()
    fun getBytesFromUri(uriString: String): ByteArray?
}