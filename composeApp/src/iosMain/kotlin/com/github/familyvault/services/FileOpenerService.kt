package com.github.familyvault.services

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

class FileOpenerService() : IFileOpenerService {
    private val TAG = "FileOpenerService"
    private val defaultManager = NSFileManager.defaultManager

    @OptIn(ExperimentalForeignApi::class)
    override fun openFileWithExternalViewer(
        fileBytes: ByteArray, mimeType: String, fileName: String
    ): Boolean {
        val fileUrl = createTempFile(fileBytes, fileName)
        val fileNSUrl = NSURL.fileURLWithPath(fileUrl)

        val documentInteractionController =
            UIDocumentInteractionController.interactionControllerWithURL(fileNSUrl)
        documentInteractionController.delegate = DocumentInteractionController(
            UIApplication.sharedApplication.keyWindow?.rootViewController!!
        )

        return documentInteractionController.presentPreviewAnimated(true)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun downloadFile(fileBytes: ByteArray, fileName: String): String? {
        val documentDirectory = defaultManager.URLForDirectory(
            NSDocumentDirectory, NSUserDomainMask, null, true, null
        )

        val downloadsDirPath = documentDirectory?.URLByAppendingPathComponent("Downloads")
        val filePath = downloadsDirPath?.URLByAppendingPathComponent(fileName)?.relativePath

        if (!defaultManager.fileExistsAtPath(downloadsDirPath?.path!!)) {
            defaultManager.createDirectoryAtURL(
                downloadsDirPath,
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
        }
        if (!defaultManager.fileExistsAtPath(filePath!!)) {
            defaultManager.createFileAtPath(
                path = filePath, contents = fileBytes.toNSData(), attributes = null
            )
        }
        return filePath
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun createTempFile(fileBytes: ByteArray, fileName: String): String {
        val applicationSupportDir = defaultManager.URLForDirectory(
            NSApplicationSupportDirectory, NSUserDomainMask, null, true, null
        )

        val downloadsDirPath = applicationSupportDir?.URLByAppendingPathComponent("Cache")
        val filePath = downloadsDirPath?.URLByAppendingPathComponent(fileName)?.relativePath

        if (!defaultManager.fileExistsAtPath(downloadsDirPath?.path!!)) {
            defaultManager.createDirectoryAtURL(
                downloadsDirPath,
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
        }

        if (!defaultManager.fileExistsAtPath(filePath!!)) {
            defaultManager.createFileAtPath(
                path = filePath, contents = fileBytes.toNSData(), attributes = null
            )
        }

        return filePath
    }
}

@OptIn(BetaInteropApi::class)
class DocumentInteractionController(
    val viewContoller: UIViewController
) : NSObject(), UIDocumentInteractionControllerDelegateProtocol {
    override fun documentInteractionControllerViewControllerForPreview(controller: UIDocumentInteractionController): UIViewController {
        return viewContoller
    }
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData {
    return usePinned {
        NSData.dataWithBytes(
            bytes = it.addressOf(0), size.toULong()
        )
    }
}