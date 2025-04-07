package com.github.familyvault.services

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.github.familyvault.AppConfig.NOTIFICATION_PERMISSION_REQUEST_CODE

class NotificationService(context: Context): INotificationService {
    private var channelId: String = "FamilyVault"
    private val androidContext = context

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "FamilyVault"
            val descriptionText = "FamilyVault Notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                androidContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun requestNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    androidContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    androidContext as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun checkNotificationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                androidContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun sendNotification(title: String, content: String) {

        val builder = NotificationCompat.Builder(androidContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(androidContext)) {
            if (!checkNotificationPermission()) {
                return
            }
            notify(System.currentTimeMillis().toInt(), builder.build())
        }

    }
}