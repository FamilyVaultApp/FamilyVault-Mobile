package com.github.familyvault.backend.client

import android.content.Context

fun createPrivMxClient(context: Context): IPrivMxClient {
    val certsPath = context.getDir("certs", Context.MODE_PRIVATE)
    return AndroidPrivMxClient(certsPath.absolutePath)
}