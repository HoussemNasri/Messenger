package com.nasri.messenger.ui.chats

import timber.log.Timber
import java.lang.IllegalArgumentException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MessengerDateFormatter {
    companion object {
        /**
         *
         * @throws IllegalArgumentException when the given date is after the current date
         * */
        fun formatMessageDeliveryDate(timestamp: Long): String {
            val now = LocalDateTime.now()
            val date =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
            if (now.isBefore(date)) {
                throw IllegalArgumentException("Got you! time traveler")
            }
            if (now.year == date.year) {
                return when {
                    now.dayOfYear == date.dayOfYear -> date.format(DateTimeFormatter.ofPattern("HH:mm"))

                    ChronoUnit.DAYS.between(date, now) == 1L -> "Yesterday"

                    ChronoUnit.DAYS.between(date, now) <= 7 ->
                        date.format(DateTimeFormatter.ofPattern("E"))

                    else -> date.format(DateTimeFormatter.ofPattern("MMM dd"))
                }
            }
            return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        }
    }
}