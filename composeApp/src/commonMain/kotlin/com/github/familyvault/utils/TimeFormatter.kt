package com.github.familyvault.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class DateParts(val day: Int, val month: Int, val year: Int)

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

    fun formatDateParts(timestamp: Long): DateParts {
        val fileDateTime = Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val fileDate = fileDateTime.date
        return DateParts(day = fileDate.dayOfMonth, month = fileDate.month.value, year = fileDate.year)
    }
}