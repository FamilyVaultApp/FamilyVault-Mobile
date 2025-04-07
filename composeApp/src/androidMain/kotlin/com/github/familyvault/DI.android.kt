package com.github.familyvault

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.client.PrivMxClient
import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.createAppDatabase
import com.github.familyvault.services.INfcService
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.services.NfcService
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

    /* NFC */
    factory {NfcService(get()) }.bind<INfcService>()
}
