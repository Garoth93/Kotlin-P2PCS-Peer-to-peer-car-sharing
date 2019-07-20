package com.fourteenrows.p2pcs.unitTests

import com.fourteenrows.p2pcs.model.utility.ModelUtils
import junit.framework.Assert.assertEquals
import org.junit.Test

class Utils {

    @Test
    fun getTimeSlots() {
        val right = arrayOf(
            "00:00 - 06:00",
            "06:00 - 12:00",
            "12:00 - 18:00",
            "18:00 - 00:00"
        )
        val result = ModelUtils.getTimeSlots()
        assertEquals(right.size, result.size)
        for (i in 0 until result.size)
            assertEquals(result[i], right[i])
    }

}