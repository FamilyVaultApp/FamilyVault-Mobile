package com.github.familyvault

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.client.createPrivMxClient
import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.createAppDatabase
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.services.QRCodeService
import com.github.familyvault.utils.IQrCodeGenerator
import com.github.familyvault.utils.QrCodeGenerator
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun getPlatformModules() = module {
    single { createPrivMxClient(get()) }.bind<IPrivMxClient>()
    single { createAppDatabase(get()) }.bind<AppDatabase>()
    factory { QRCodeService(get()) }.bind<IQRCodeService>()
    factory { QrCodeGenerator() }.bind<IQrCodeGenerator>()
}
