package com.github.familyvault

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.client.PrivMxClient
import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.createAppDatabase
import com.github.familyvault.models.AndroidCompilationFlags
import com.github.familyvault.models.ICompilationFlags
import com.github.familyvault.services.AudioPlayerService
import com.github.familyvault.services.AudioRecorderService
import com.github.familyvault.services.DocumentPickerService
import com.github.familyvault.services.FileOpenerService
import com.github.familyvault.services.IAudioPlayerService
import com.github.familyvault.services.IAudioRecorderService
import com.github.familyvault.services.IDocumentPickerService
import com.github.familyvault.services.IFileOpenerService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.services.INfcService
import com.github.familyvault.services.INotificationService
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.services.ImagePickerService
import com.github.familyvault.services.NfcService
import com.github.familyvault.services.NotificationService
import com.github.familyvault.services.QrCodeService
import com.github.familyvault.utils.IQrCodeGenerator
import com.github.familyvault.utils.QrCodeGenerator
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun getPlatformModules() = module {
    /* PrivMx */
    single { PrivMxClient() }.bind<IPrivMxClient>()

    /* Local database */
    single { createAppDatabase(get()) }.bind<AppDatabase>()

    /* QrCode */
    factory { QrCodeService(get()) }.bind<IQRCodeService>()
    factory { QrCodeGenerator() }.bind<IQrCodeGenerator>()

    /* Notifications */
    factory { NotificationService(get()) }.bind<INotificationService>()

    /* NFC */
    factory { NfcService(get()) }.bind<INfcService>()

    /* File operations */
    factory { FileOpenerService(get()) }.bind<IFileOpenerService>()

    /* Audio service */
    factory { AudioPlayerService() }.bind<IAudioPlayerService>()
    factory { AudioRecorderService(get()) }.bind<IAudioRecorderService>()

    single { ImagePickerService() }.bind<IImagePickerService>()
    single { DocumentPickerService() }.bind<IDocumentPickerService>()

    /* Compilation flags */
    single { AndroidCompilationFlags() }.bind<ICompilationFlags>()
}
