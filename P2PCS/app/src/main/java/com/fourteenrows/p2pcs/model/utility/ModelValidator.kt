package com.fourteenrows.p2pcs.model.utility

import java.util.regex.Pattern

object ModelValidator {

    fun checkNumberOfSeats(seats: Long): Boolean {
        return seats > 0
    }

    fun checkValueIsEmail(value: String): Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(value).matches()
    }

    fun checkValueIsPlate(value: String): Boolean {
        val PLATE_PATTERN = Pattern.compile(
            "[A-Z]{2}(\\d){3}[A-Z]{2}"
        )
        return PLATE_PATTERN.matcher(value).matches()
    }

    fun checkValueIsEmpty(value: String): Boolean {
        return value.isNotEmpty()
    }

    fun checkStringLength(value: String, length: Int): Boolean {
        return value.length >= length
    }

    fun checkStringsEqual(value1: String, value2: String): Boolean {
        return value1 == value2
    }
}