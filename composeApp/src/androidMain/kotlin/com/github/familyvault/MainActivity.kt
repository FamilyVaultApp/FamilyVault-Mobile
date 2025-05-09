package com.github.familyvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.github.familyvault.services.FileOpenerService
import com.github.familyvault.services.ImagePickerService
import com.github.familyvault.services.DocumentPickerService
import com.github.familyvault.services.IFileOpenerService
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val androidSpecificModule = module {
            single { FileOpenerService(androidContext()) } bind IFileOpenerService::class
        }
        
        initKoin {
            androidContext(this@MainActivity)
            modules(androidSpecificModule)
        }

        val imagePickerService = get<ImagePickerService>()
        imagePickerService.initializeWithActivity(this@MainActivity)
        
        val documentPickerService = get<DocumentPickerService>()
        documentPickerService.initializeWithActivity(this@MainActivity)

        setContent {
            SystemBarColorChanger()
            App()
        }
    }

    @Composable
    fun SystemBarColorChanger() {
        val isLightTheme = !isSystemInDarkTheme()

        val alphaValue = 0.03f
        val scrimLightTheme = Color.Black.copy(alphaValue).toArgb()
        val scrimDarkTheme = Color.White.copy(alphaValue).toArgb()
        val systemBarsStyleLight = SystemBarStyle.light(scrimLightTheme, scrimLightTheme)
        val systemBarsStyleDark = SystemBarStyle.dark(scrimDarkTheme)

        LaunchedEffect(isLightTheme) {
            if (isLightTheme) {
                enableEdgeToEdge(
                    statusBarStyle = systemBarsStyleLight,
                    navigationBarStyle = systemBarsStyleLight,
                )
            } else {
                enableEdgeToEdge(
                    statusBarStyle = systemBarsStyleDark,
                    navigationBarStyle = systemBarsStyleDark,
                )
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
