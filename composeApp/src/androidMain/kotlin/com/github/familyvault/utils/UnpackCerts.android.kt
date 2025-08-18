package com.github.familyvault.utils

import familyvault.composeapp.generated.resources.Res
import kotlinx.coroutines.runBlocking
import java.io.File


actual object UnpackCerts {
    actual fun extractCerts(toFile: String){
        val file = File(toFile)
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
        runBlocking {
            val data = Res.readBytes("files/cacert.pem")
            file.outputStream().apply {
                write(data)
                close()
            }
        }
    }
}