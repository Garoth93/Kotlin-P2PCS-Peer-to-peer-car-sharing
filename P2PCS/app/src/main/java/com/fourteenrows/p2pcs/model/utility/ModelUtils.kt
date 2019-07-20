package com.fourteenrows.p2pcs.model.utility

import java.text.SimpleDateFormat
import java.util.*

object ModelUtils {
    fun getTimeSlots(): Array<String> {
        return arrayOf(
            "00:00 - 06:00",
            "06:00 - 12:00",
            "12:00 - 18:00",
            "18:00 - 00:00"
        )
    }

    fun fixDate(date: String): Long {
        val time = date.toLong()
        return Date(time - time % (24 * 60 * 60 * 1000)).time
    }

    fun formatDate(date: Date): String {
        val dateFormat = "dd MMMM yyyy"
        val toLocale = SimpleDateFormat(dateFormat, Locale.ITALIAN)
        return toLocale.format(date)
    }
}