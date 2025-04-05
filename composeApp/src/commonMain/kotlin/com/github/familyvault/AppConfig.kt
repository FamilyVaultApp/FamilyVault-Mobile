package com.github.familyvault

object AppConfig {
    /* Backend */
    const val BACKEND_URL: String = "http://10.7.5.1:8080"
    const val PRIVMX_BRIDGE_URL: String = "http://10.7.5.1:9111"

    const val SECRET: String = "q8v9P2z4J1lZxT6rMb0KdQ"
    const val BACKEND_REQUEST_INTERVAL_LENGTH_MS: Long = 2000

    /* Qr Code */
    val QR_CODE_SIZE: Pair<Int, Int> = 500 to 500
    const val CHAT_THREAD_TAG = "FV_CHAT"

    /* Chat */
    val CHAT_MESSAGES_PER_PAGE = 50
}