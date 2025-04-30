package com.github.familyvault.models

data class ImageSize(val height: Int, val width: Int) {
    companion object {
        val Unspecified = ImageSize(300, 300)
    }

    val aspectRatio: Float
        get() = if (height == 0) 1F else width.toFloat() / height.toFloat()
}