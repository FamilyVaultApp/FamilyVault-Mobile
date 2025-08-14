package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.github.familyvault.models.ImageSize
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skia.impl.use
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSItemProvider
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject
import platform.posix.memcpy
import kotlin.coroutines.resume


class ImagePickerService : IImagePickerService {
    private var continuation: Continuation<List<ByteArray>>? = null

    private val selectedImages = mutableStateListOf<PHPickerResult>()
    private val pickerController = PickerController(selectedImages) {
        continuation?.resume(getSelectedImageAsByteArrays())
        continuation = null
        cont = null
    }
    private val config = PHPickerConfiguration().apply {
        selectionLimit = 0
        filter = PHPickerFilter.imagesFilter
    }
    private val scope = CoroutineScope(Dispatchers.Default)
    private var cont: PHPickerViewController? = null

    init {
        clearSelectedImages()
    }

    override fun openMediaPickerForSelectingImages() {
        if (cont == null) cont = PHPickerViewController(config)
        cont!!.setDelegate(pickerController)
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            cont!!,
            true,
            null
        )
    }

    override suspend fun pickImagesAndReturnByteArrays(): List<ByteArray> =
        suspendCoroutine { cont ->
            continuation = cont
            openMediaPickerForSelectingImages()
        }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        return selectedImages.firstOrNull { phResult ->
            phResult.ID == uriString
        }?.let { phResult ->
            return@let runBlocking(Dispatchers.IO) {
                suspendCoroutine { cont ->
                    phResult.itemProvider.loadDataRepresentationForTypeIdentifier(UTTypeImage.identifier){ data, error ->
                        cont.resume(data!!.toByteArray())
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun getSelectedImageAsByteArrays(): List<ByteArray> {
        return selectedImages.map { phResult ->
            runBlocking {
                suspendCoroutine { cont ->
                    phResult.itemProvider.loadDataRepresentationForTypeIdentifier(
                        UTTypeImage.identifier,
                    ) { p1: NSData?,
                        p2: NSError? ->
                        val image = p1!!
                        cont.resume(image.toByteArray())
                    }
                }
            }
        }
    }

    override fun getSelectedImageUrls(): List<String> = selectedImages.map { phResult ->
        phResult.ID
    }

    override fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap {
        return Image.makeFromEncoded(imageBytes).use {
            it.toComposeImageBitmap()
        }
    }

    override fun clearSelectedImages() {
        selectedImages.clear()
        cont = null
    }

    override fun removeSelectedImage(uri: String) {
        selectedImages.removeAll {
            it.ID == uri
        }
    }

    override fun compressAndRotateImage(
        imageByteArray: ByteArray,
        compressionQuality: Int?
    ): ByteArray {
        return Image.makeFromEncoded(imageByteArray).use {
            it.compress(quality = compressionQuality ?: 92)
        }
    }

    override fun getImageAsByteArraySize(image: ByteArray): ImageSize {
        val imageBitmap = getBitmapFromBytes(image)
        return ImageSize(imageBitmap.height, imageBitmap.width)
    }

    private fun Image.compress(quality: Int): ByteArray {
        return encodeToData(EncodedImageFormat.JPEG, quality)!!.bytes
    }

    //TODO: Check if this rotation method is needed
//    private fun Image.fixRotation(): Image = this.apply{
//
//        Bitmap.makeFromImage(Image.makeFromEncoded(imageBytes))
//    }

}

@OptIn(BetaInteropApi::class)
class PickerController(
    val selectedUris: MutableList<PHPickerResult>,
    val onFinish: () -> Unit
) : NSObject(),
    PHPickerViewControllerDelegateProtocol {

    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>
    ) {
        @Suppress("UNCHECKED_CAST")
        didFinishPicking as List<PHPickerResult>
        val itemsToAdd = didFinishPicking.filter {
            it.itemProvider.hasItemConformingToTypeIdentifier(UTTypeImage.identifier)
        }
        selectedUris.clear()
        selectedUris.addAll(itemsToAdd)

        picker.dismissViewControllerAnimated(true) {
            onFinish()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray = memScoped {
    ByteArray(length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), bytes, length)
        }
    }
}

val NSItemProvider.ID get() = "$suggestedName/$hash"
val PHPickerResult.ID get() = this.assetIdentifier ?: itemProvider.ID