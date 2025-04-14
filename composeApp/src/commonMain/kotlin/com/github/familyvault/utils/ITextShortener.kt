package com.github.familyvault.utils

interface ITextShortener {
    fun shortenText(text: String, size: Int = 50): String
}