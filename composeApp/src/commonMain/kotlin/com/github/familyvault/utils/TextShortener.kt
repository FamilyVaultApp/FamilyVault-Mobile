package com.github.familyvault.utils

object TextShortener : ITextShortener {
    override fun shortenText(text: String, size: Int): String =
        if (text.length > size) text.substring(0, size) + "..." else text
}