import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)

            implementation(libs.privmx.endpoint.extra)
            implementation(libs.privmx.endpoint)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.play.services.code.scanner)
            implementation(libs.zxing)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.runtime)
            implementation(compose.ui)

            implementation(libs.voyager.bottom.sheet.navigator)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.voyager.transitions)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.navigation.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.protobuf)

            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }
    }
}

android {
    namespace = "com.github.familyvault"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].jniLibs.srcDirs("src/main/jniLibs")
    defaultConfig {
        applicationId = "com.github.familyvault"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}