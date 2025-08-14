package com.github.familyvault.utils

import familyvault.composeapp.generated.resources.Res
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.runBlocking
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.dataWithBytes
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
actual object UnpackCerts {
    @OptIn(ExperimentalForeignApi::class)
    actual fun extractCerts(toFile: String){
        val url = NSURL.fileURLWithPath(toFile)
        runBlocking {
            val content = Res.readBytes("files/cacert.pem").asData()
            NSFileManager.defaultManager.createDirectoryAtURL(url.URLByDeletingLastPathComponent!!,true,null,null)
            NSFileManager.defaultManager.createFileAtPath(url.path!!, content,null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.asData(): NSData = usePinned {
    NSData.dataWithBytes(it.addressOf(0), size.toULong())
}