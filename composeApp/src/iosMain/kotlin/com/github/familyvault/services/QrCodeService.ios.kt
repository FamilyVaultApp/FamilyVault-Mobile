package com.github.familyvault.services

import com.github.familyvault.exceptions.QrCodeBadScanException
import com.github.familyvault.exceptions.QrCodeCancellationException
import com.github.familyvault.exceptions.QrCodeScannerErrorException
import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.models.QrCodeScanResponse
import com.github.familyvault.models.enums.QrCodeScanResponseStatus
import com.github.familyvault.utils.PayloadDecryptor
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureMetadataOutput
import platform.AVFoundation.AVCaptureMetadataOutputObjectsDelegateProtocol
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.AVMetadataMachineReadableCodeObject
import platform.AVFoundation.AVMetadataObject
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QrCodeService() : IQRCodeService {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun scanQRCode(): QrCodeScanResponse {
////        TODO("Method not yet implemented")
        return suspendCoroutine { continuation ->
            val controller = QrCodeViewController{
                continuation.resume(
                    it
                )
            }
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                controller,
                true,
                null
            )
        }
    }

    //TODO: This method can be in common source
    override suspend fun scanPayload(): AddFamilyMemberDataPayload {
        val scannedResult = scanQRCode()

        if (scannedResult.status == QrCodeScanResponseStatus.CANCELED) {
            throw QrCodeCancellationException()
        }
        if (scannedResult.status == QrCodeScanResponseStatus.ERROR) {
            //TODO: Here can be error when no device for scan QrCode
//            if (scannedResult.error is MlKitException && scannedResult.error.errorCode == MlKitException.CODE_SCANNER_UNAVAILABLE) {
//                throw QrCodeScannerNotInstalledException(scannedResult.error.toString())
//            } else {
                throw QrCodeScannerErrorException(scannedResult.error?.toString())
//            }
        }
        if (scannedResult.content == null) {
            throw QrCodeBadScanException("Scanned code is null")
        }

        return try {
            PayloadDecryptor.decrypt(scannedResult.content)
        } catch (e: Exception) {
            throw QrCodeBadScanException(e.toString())
        }
    }
}

class QrCodeViewController(
    private val onResult: (response: QrCodeScanResponse)-> Unit
) : UIViewController(null,null), AVCaptureMetadataOutputObjectsDelegateProtocol{
    private var session: AVCaptureSession? = null
    private lateinit var previewLayer: AVCaptureVideoPreviewLayer

    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLoad() {
        super.viewDidLoad()
        session = AVCaptureSession()
        val device = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        if (device == null) {
            TODO("return QRCodeScanResponse.Error, no device for scan")
        }
        try {
            val input = AVCaptureDeviceInput(device = device, null)
            val output = AVCaptureMetadataOutput()

            session!!.beginConfiguration()

            if (session!!.canAddInput(input)) {
                session!!.addInput(input)
            }

            if (session!!.canAddOutput(output)) {
                session!!.addOutput(output)
                output.setMetadataObjectsDelegate(this, dispatch_get_main_queue())
                output.metadataObjectTypes = listOf(AVMetadataObjectTypeQRCode)
            }
            session!!.commitConfiguration()

            previewLayer = AVCaptureVideoPreviewLayer(session = session!!)
            previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
            previewLayer.frame = view.bounds
            view.layer.addSublayer(previewLayer)

            session!!.startRunning()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun failed() {
        val ac =  UIAlertController.alertControllerWithTitle(
            title = "Scanning not supported",
            message = "Your device does not support scanning a code from an item. Please use a device with a camera.",
            preferredStyle = UIAlertControllerStyleAlert
        )
        ac.addAction(
            UIAlertAction.actionWithTitle(title= "OK", style = UIAlertActionStyleDefault, null),
        )
        presentViewController(ac,true,null)
        session = null
    }

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)

        if (!session!!.isRunning()) {
            session!!.startRunning()
        }
    }

    override fun viewWillDisappear(animated: Boolean) {
        super.viewWillDisappear(animated)

        if (session!!.isRunning()) {
            session!!.stopRunning()
        }
    }

    override fun prefersStatusBarHidden(): Boolean {
        return true
    }

    //TODO: Find and implement this option
//    override var supportedInterfaceOrientations: UIInterfaceOrientationMask {
//        return .portrait
//    }

    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection
    ) {
        println("QR code scan result")
        @Suppress("UNCHECKED_CAST")
        ((didOutputMetadataObjects as List<AVMetadataObject>).firstOrNull{
            it.type == AVMetadataObjectTypeQRCode
        } as? AVMetadataMachineReadableCodeObject)?.let{ result ->
            session?.stopRunning()
            dismissViewControllerAnimated(true,null)
            onResult(QrCodeScanResponse.success(result.stringValue))
        }
    }
}