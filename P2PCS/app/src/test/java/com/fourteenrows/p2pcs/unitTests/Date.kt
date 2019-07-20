package com.fourteenrows.p2pcs.unitTests

import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.model.utility.ModelUtils
import junit.framework.Assert
import org.junit.Assert.*
import org.junit.Test
import java.time.temporal.ChronoUnit
import java.util.Date

class Date {

    @Test
    fun fixDate() {
        Assert.assertEquals(ModelUtils.fixDate("1234124123"), 1209600000)
        Assert.assertEquals(ModelUtils.fixDate("0"), 0)
    }

    @Test
    fun formatDate() {
        Assert.assertEquals(ModelUtils.formatDate(Date(1234556732L)), "15 gennaio 1970")
        Assert.assertEquals(ModelUtils.formatDate(Date(-1234556732L)), "17 dicembre 1969")
        Assert.assertEquals(ModelUtils.formatDate(Date(0)), "01 gennaio 1970")
    }

    @Test
    fun isInThePast() {
        assertTrue(ModelDates.isInThePast())
        assertFalse(ModelDates.isInThePast())
    }

    @Test
    fun toLocaleTimeFormat() {
        assertEquals(ModelDates.toLocaleTimeFormat(Date(0)), "01 gennaio 1970")
        val date = Date(1231231231L)
        assertEquals(ModelDates.toLocaleTimeFormat(date), "15 gennaio 1970")
    }

    @Test
    fun nextDayOf() {
        assertEquals(ModelDates.nextDayOf(1234567, "06 - 06"), Date(22834567))
        assertEquals(ModelDates.nextDayOf(1234567, "12"), Date(44434567))
        assertEquals(ModelDates.nextDayOf(1234567, "00AAAA"), Date(1234567))
    }

    @Test
    fun nextNDays() {
        assertEquals(
            ModelDates.truncateDateToDay(
                Date.from(
                    Date().toInstant()
                        .plus(3L, ChronoUnit.DAYS)
                )
            ).time, ModelDates.nextNDays(3)
        )
        assertEquals(
            ModelDates.truncateDateToDay(
                Date.from(
                    Date().toInstant()
                        .plus(0L, ChronoUnit.DAYS)
                )
            ).time, ModelDates.nextNDays(0)
        )
    }

    @Test
    fun TruncaDateAt() {
        assertEquals(ModelDates.truncateDateToDay(Date(555555555L)), Date(518400000))
        assertEquals(ModelDates.truncateDateToDay(Date(0)), Date(0))
    }

    @Test
    fun endTimeSlotOf() {
        val date = Date(1559720886000L)
        val res = ModelDates.endTimeSlotOf(date)
        assertEquals(res.time, 1559728800000)

    }

}