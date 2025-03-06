package com.github.familyvault

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.client.createPrivMxClient
import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.createAppDatabase
import com.github.familyvault.services.IQRCodeScannerService
import com.github.familyvault.services.QRCodeScannerService
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun getPlatformModules() = module {
    single { createPrivMxClient(get()) }.bind<IPrivMxClient>()
    single { createAppDatabase(get()) }.bind<AppDatabase>()
    single { QRCodeScannerService(get()) }.bind<IQRCodeScannerService>()
}
