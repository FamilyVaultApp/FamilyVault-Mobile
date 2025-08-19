package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import com.github.familyvault.utils.MimeTypeParser
import io.ktor.utils.io.core.toByteArray
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import platform.CoreGraphics.CGContextScaleCTM
import platform.CoreGraphics.CGContextTranslateCTM
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.PDFKit.PDFDocument
import platform.PDFKit.kPDFDisplayBoxMediaBox
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIImageJPEGRepresentation
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
                    UTTypeArchive,
                    UTTypeImage
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

    @OptIn(ExperimentalForeignApi::class)
    override fun getDocumentPreviewPageFromUri(uriString: String): ByteArray {
        val url = NSURL.fileURLWithPath(uriString)
        val pdf = PDFDocument(url)
        val page = pdf.pageAtIndex(0u)
        val bounds = page?.boundsForBox(kPDFDisplayBoxMediaBox)

        val renderer = UIGraphicsImageRenderer(bounds!!)
        val image = renderer.imageWithActions { it ->
            UIColor.whiteColor.set()
            it?.fillRect(page.boundsForBox(kPDFDisplayBoxMediaBox))

            val context = it!!.CGContext
            CGContextTranslateCTM(context, 0.0, (bounds.useContents { size.height }))
            CGContextScaleCTM(context, 1.0, (-1.0))
            page.drawWithBox(kPDFDisplayBoxMediaBox, context)
        }

        val data = UIImageJPEGRepresentation(image, 0.95)
        return data?.toByteArray()!!
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
