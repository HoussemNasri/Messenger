package com.nasri.messenger.ui.chats

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * A utility class to handle date formatting.
 * */
class ChatsDateFormatter {
    companion object {
        /**
         * Returns a user-friendly formatted date used primarily in message delivery
         * @throws IllegalArgumentException when the given date is after the current date
         * */
        fun formatMessageDeliveryDate(timestamp: Long): String {
            val now = LocalDateTime.now()
            val date =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
            if (now.isBefore(date)) {
                throw IllegalArgumentException("Got you! time traveler")
            }
            // Message delivered on the same year
            if (now.year == date.year) {
                return when {
                    // Message delivered on the same day
                    now.dayOfYear == date.dayOfYear -> date.format(DateTimeFormatter.ofPattern("HH:mm"))
                    //Message delivered yesterday
                    ChronoUnit.DAYS.between(date, now) == 1L -> "Yesterday"
                    //Message delivered this week
                    ChronoUnit.DAYS.between(date, now) <= 7 ->
                        date.format(DateTimeFormatter.ofPattern("E"))
                    //Message delivered any time this year
                    else -> date.format(DateTimeFormatter.ofPattern("MMM dd"))
                }
            }
            // Message delivered in previous years
            return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        }
    }
}