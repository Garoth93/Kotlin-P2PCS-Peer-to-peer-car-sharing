package com.fourteenrows.p2pcs.model.utility

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

object ModelDates {
    val MAX_DATE = Date(32500915200000)

    //Returns XX:00 - YY:00
    fun getSlotString(start: Timestamp, end: Timestamp): String {
        var res = ""
        val dateFormat = "kk:mm"
        val toLocale = SimpleDateFormat(dateFormat, Locale.ITALIAN)
        val startSlot = toLocale.format(start.toDate())
        val endSlot = toLocale.format(end.toDate())
        res += startSlot
        res += " - "
        res += endSlot
        return res
    }

    fun isInThePast(end: Timestamp) =
        end.toDate().toInstant() < Timestamp.now().toDate().toInstant()

    fun isSameDay(dateA: Date, dateB: Date): Boolean {
        val fmt = SimpleDateFormat("yyyyMMdd", Locale.ITALIAN)
        return fmt.format(dateA) == fmt.format(dateB)
    }

    fun toLocaleTimeFormat(date: Date): String {
        val dateFormat = "dd MMMM yyyy"
        val toLocale = SimpleDateFormat(dateFormat, Locale.ITALIAN)
        return toLocale.format(date)
    }

    fun toTinyTimeSpanFormat(date: Date): String {
        val dateFormat = "kk:mm"
        val toLocale = SimpleDateFormat(dateFormat, Locale.ITALIAN)
        return toLocale.format(date)
    }

    fun nextDayOf(date: Date): Date {
        return Date.from(
            date.toInstant()
                .plus(1, ChronoUnit.DAYS)
        )
    }

    fun nextDayOf(date: Long, timeslot: String): Date {
        val start = timeslot.split(" - ")
        val time = start[0].split(":")
        val minutes = (time[0].toLong() * 60L) + time[1].toLong()
        return Date.from(
            Date(date).toInstant()
                .plus(minutes, ChronoUnit.MINUTES)
        )
    }

    fun nextNDays(days: Int) =
        Date.from(truncateDateToDay(Date()).toInstant().plus(days.toLong(), ChronoUnit.DAYS)).time

    fun setSlotToDate(date: Date, slot: String): Date {
        val start = slot.split(" - ")
        val time = start[0].split(":")
        val minutes = (time[0].toLong() * 60L) + time[1].toLong()
        val toDate = Date.from(
            date.toInstant().truncatedTo(ChronoUnit.DAYS)
                .plus(minutes, ChronoUnit.MINUTES)
        )!!
        toDate.hours -= 2
        return toDate
    }

    fun setSlotToDateStart(date: Date, slot: String): Date {
        val start = slot.split(" - ")
        val time = start[0].split(":")
        val minutes = (time[0].toLong() * 60L) + time[1].toLong()
        val toDate = Date.from(
            date.toInstant().truncatedTo(ChronoUnit.DAYS)
                .plus(minutes, ChronoUnit.MINUTES)
        )!!
        toDate.hours -= 2
        return toDate
    }

    fun setSlotToDateEnd(date: Date, slot: String): Date {
        val start = slot.split(" - ")
        val time = start[1].split(":")
        val minutes = (time[0].toLong() * 60L) + time[1].toLong()
        val toDate = Date.from(
            date.toInstant().truncatedTo(ChronoUnit.DAYS)
                .plus(minutes, ChronoUnit.MINUTES)
        )!!
        toDate.hours -= 2
        return toDate
    }

    fun truncateDateToDay(date: Date): Date =
        Date.from(date.toInstant().truncatedTo(ChronoUnit.DAYS))

    fun endTimeSlotOf(date: Date): Date {
        val slots = ModelUtils.getTimeSlots()
        val dateFormat = "HH"
        val toLocale = SimpleDateFormat(dateFormat, Locale.ITALIAN)
        val dateHour = toLocale.format(date)
        val bounds = ArrayList<Int>()
        slots.forEach { slot -> bounds.add((slot.split(" - ")[1]).split(":")[0].toInt()) }
        var target = 0
        for (bound in bounds) {
            if (dateHour.toInt() < bound) {
                target = bound
                break
            }
        }
        val format = "yyyy-MM-dd"
        var hh = target.toString()
        var toConvert = date
        val ONE_DAY = 86400L
        if (hh.length == 1) {
            hh = "0" + hh
            toConvert = Date.from(toConvert.toInstant().plusSeconds(ONE_DAY))
        }
        val stringDate = SimpleDateFormat(format, Locale.ITALIAN).format(toConvert) + "T" + hh + ":00:00"
        val date = Date.from(LocalDateTime.parse(stringDate).atZone(ZoneId.systemDefault()).toInstant())
        date.toInstant().plusSeconds(ONE_DAY)
        return date
    }
}