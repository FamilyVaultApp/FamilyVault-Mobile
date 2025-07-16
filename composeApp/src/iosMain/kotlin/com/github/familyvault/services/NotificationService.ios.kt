package com.github.familyvault.services

import com.github.familyvault.AppConfig

class NotificationService(): INotificationService {
    private var channelId: String = AppConfig.NOTIFICATION_CHANNEL_NAME

    init {

    }

    override fun requestNotificationsPermission() {
        TODO("Method not yet implemented")
    }

    override fun checkNotificationPermission(): Boolean {
        TODO("Method not yet implemented")
    }

    override fun sendNotification(title: String, content: String) {
        TODO("Method not yet implemented")
    }
}