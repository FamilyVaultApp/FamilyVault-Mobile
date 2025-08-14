package com.github.familyvault

import android.content.Context
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
import com.github.familyvault.services.DocumentPickerService
import com.github.familyvault.utils.UnpackCerts
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import java.net.URI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val path = extractCerts(this)
        initKoin(path) {
            androidContext(this@MainActivity)
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

    private fun extractCerts(context: Context): String {
        return URI(context.filesDir.path).resolve("files/cacert.pem").path.also { path ->
            println(path)
            UnpackCerts.extractCerts(path)
        }
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
