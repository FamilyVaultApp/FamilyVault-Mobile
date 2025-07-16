package com.github.familyvault

import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.getAppDatabase
import com.github.familyvault.database.getDatabaseBuilder
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
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun getPlatformModules(): Module = module{
    single { getAppDatabase(getDatabaseBuilder()) }.bind<AppDatabase>()

    /* QrCode */
    factory { QrCodeService() }.bind<IQRCodeService>()
    factory { QrCodeGenerator() }.bind<IQrCodeGenerator>()

    /* Notifications */
    factory { NotificationService() }.bind<INotificationService>()

    /* NFC */
    factory { NfcService() }.bind<INfcService>()

    /* File operations */
    factory { FileOpenerService() }.bind<IFileOpenerService>()

    /* Audio service */
    factory { AudioPlayerService() }.bind<IAudioPlayerService>()
    factory { AudioRecorderService() }.bind<IAudioRecorderService>()

    single { ImagePickerService() }.bind<IImagePickerService>()
    single { DocumentPickerService() }.bind<IDocumentPickerService>()
}