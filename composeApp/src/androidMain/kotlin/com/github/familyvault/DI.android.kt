package com.github.familyvault

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.client.PrivMxClient
import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.createAppDatabase
import com.github.familyvault.services.AudioPlayerService
import com.github.familyvault.services.AudioRecorderService
import com.github.familyvault.services.IAudioPlayerService
import com.github.familyvault.services.IAudioRecorderService
import com.github.familyvault.services.IMediaPickerService
import com.github.familyvault.services.INfcService
import com.github.familyvault.services.INotificationService
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.services.MediaPickerService
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

    /* Audio service */
    factory { AudioPlayerService() }.bind<IAudioPlayerService>()
    factory { AudioRecorderService(get()) }.bind<IAudioRecorderService>()

    single { MediaPickerService() }.bind<IMediaPickerService>()
}
