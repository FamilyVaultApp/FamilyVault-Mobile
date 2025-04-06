package com.github.familyvault.services

interface INotificationService {
    fun checkNotificationPermission(): Boolean
    fun requestNotificationsPermission()
    fun sendNotification(title: String, content: String)
}