package com.github.familyvault

object AppConfig {
    /* Backend */
    const val BACKEND_URL: String = "http://10.7.5.1:8080"
    const val PRIVMX_BRIDGE_URL: String = "http://10.7.5.1:9111"

    const val SECRET: String = "q8v9P2z4J1lZxT6rMb0KdQ"
    const val BACKEND_REQUEST_INTERVAL_LENGTH_MS: Long = 2000

    /* Notifications permission */
    const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    const val NOTIFICATION_CHANNEL_NAME = "FamilyVault"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "FamilyVault Notification channel"

    /* Qr Code */
    val QR_CODE_SIZE: Pair<Int, Int> = 500 to 500
    const val CHAT_THREAD_TAG = "FV_CHAT"
    const val TASK_THREAD_TAG = "FV_TASK"

    /* Chat */
    const val CHAT_MESSAGES_PER_PAGE = 50
    const val CHAT_AUDIO_PLAYER_BAR_COUNT = 10

    /* Audio */
    const val AUDIO_SAMPLE_RATE = 44100

    /* Images */
    const val DEFAULT_COMPRESSION_QUALITY = 25
}