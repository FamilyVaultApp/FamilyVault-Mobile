package com.github.familyvault.services

import androidx.compose.runtime.Composable

interface INotificationService {

    @Composable
    fun sendNotification(title: String, content: String)
}