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
import com.github.familyvault.services.ImagePickerService
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin {
            androidContext(this@MainActivity)
        }

        get<ImagePickerService>().initializeWithActivity(this@MainActivity)

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
