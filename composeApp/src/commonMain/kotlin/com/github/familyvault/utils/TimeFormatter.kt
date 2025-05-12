package com.github.familyvault.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

    fun formatDate(timestamp: Long): String {
        val fileDateTime = Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val fileDate = fileDateTime.date

        val monthName = fileDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
        return "${fileDate.dayOfMonth} $monthName ${fileDate.year}"
    }
}