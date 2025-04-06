package com.github.familyvault.services

import androidx.compose.runtime.Composable

interface INotificationService {
    fun sendNotification(title: String, content: String)
}