package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import com.github.familyvault.utils.MimeTypeParser
import io.ktor.utils.io.core.toByteArray
import kotlinx.cinterop.BetaInteropApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeArchive
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.UTTypeMovie
import platform.UniformTypeIdentifiers.UTTypePDF
import platform.UniformTypeIdentifiers.UTTypePresentation
import platform.UniformTypeIdentifiers.UTTypeSpreadsheet
import platform.UniformTypeIdentifiers.UTTypeText
import platform.darwin.NSObject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DocumentPickerService : IDocumentPickerService {
    private var continuation: Continuation<List<ByteArray>>? = null
    private val selectedDocumentUrls = mutableStateListOf<String>()
    private var isInitialized = false
    private var documentPickerViewController: UIDocumentPickerViewController? = null
    private val pickerController = DocumentPickerController(selectedDocumentUrls) {
        continuation?.resume(getSelectedDocumentAsByteArrays())
        continuation = null
        documentPickerViewController = null
    }

    companion object {
        private const val TAG = "DocumentPickerService"
    }

    fun initializeWithActivity() {
    }

    override fun openDocumentPicker() {
        if (documentPickerViewController == null) documentPickerViewController =
            UIDocumentPickerViewController(
                forOpeningContentTypes = listOf(
                    UTTypeSpreadsheet,
                    UTTypePresentation,
                    UTTypePDF,
                    UTTypeText,
                    UTTypeMovie,
                    UTTypeArchive
                )
            )
        documentPickerViewController?.allowsMultipleSelection = true
        documentPickerViewController?.setDelegate(pickerController)
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            documentPickerViewController!!,
            true,
            null
        )
    }

    override suspend fun pickDocumentsAndReturnByteArrays(): List<ByteArray> =
        suspendCoroutine { cont ->
            continuation = cont
            try {
                openDocumentPicker()
            } catch (e: Exception) {
                cont.resume(emptyList())
            }
        }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        return selectedDocumentUrls.firstOrNull { uri ->
            uri == uriString
        }.let { it ->
            return@let runBlocking(Dispatchers.IO) {
                suspendCoroutine { cont ->
                    cont.resume(it?.toByteArray())
                }
            }
        }
    }

    override fun getSelectedDocumentAsByteArrays(): List<ByteArray> {
        return selectedDocumentUrls.map { url ->
            runBlocking {
                suspendCoroutine { cont ->
                    val data: NSData = NSData.dataWithContentsOfURL(NSURL.fileURLWithPath(url))!!
                    cont.resume(data.toByteArray())
                }
            }
        }
    }

    override fun getSelectedDocumentUrls(): List<String> {
        return selectedDocumentUrls
    }

    override fun clearSelectedDocuments() {
        selectedDocumentUrls.clear()
    }

    override fun removeSelectedDocument(uri: String) {
        selectedDocumentUrls.remove(uri)
    }

    override fun getDocumentNameFromUri(uriString: String): String? {
        return NSURL.fileURLWithPath(uriString).lastPathComponent()
    }

    override fun getDocumentMimeTypeFromUri(uriString: String): String? {
        return MimeTypeParser.getMimeType(
            NSURL.fileURLWithPath(uriString).pathExtension().toString()
        )
    }

    override fun getDocumentPreviewPageFromUri(uriString: String): ByteArray {
        return runBlocking {
            suspendCoroutine { cont ->
                val data: NSData = NSData.dataWithContentsOfURL(NSURL.fileURLWithPath(uriString))!!
                cont.resume(data.toByteArray())
            }
        }
    }
}

@OptIn(BetaInteropApi::class)
class DocumentPickerController(
    val selectedUris: MutableList<String>,
    val onFinish: () -> Unit
) : NSObject(),
    UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val pickedDocumentsURLs =
            didPickDocumentsAtURLs.filterIsInstance<NSURL>().map { it.path ?: "" }

        selectedUris.clear()
        selectedUris.addAll(pickedDocumentsURLs)
        controller.dismissViewControllerAnimated(true) {
            onFinish()
        }
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        selectedUris.clear()
        controller.dismissViewControllerAnimated(true) {
            onFinish()
        }
    }
}
