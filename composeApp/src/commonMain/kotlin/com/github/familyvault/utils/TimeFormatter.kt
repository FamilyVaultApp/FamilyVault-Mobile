package com.github.familyvault.utils

import kotlinx.datetime.LocalDateTime

object TimeFormatter {
    fun formatTime(time: LocalDateTime): String =
        "${time.hour}:${addZeroToMinuteIfNeeded(time.minute)}"

    fun addZeroToMinuteIfNeeded(minute: Int): String {
        var minuteString = minute.toString()

        if (minuteString.length == 1) {
            minuteString = "0$minuteString"
        }

        return minuteString
    }
}