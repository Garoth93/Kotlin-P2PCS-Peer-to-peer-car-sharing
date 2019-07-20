package com.fourteenrows.p2pcs.unitTests

import com.fourteenrows.p2pcs.model.utility.ModelValidator
import junit.framework.Assert.assertEquals
import org.junit.Test

class Validator {

    @Test
    fun checkNumberOfSeats() {
        assertEquals(ModelValidator.checkNumberOfSeats(1), true)
        assertEquals(ModelValidator.checkNumberOfSeats(-1), false)
        assertEquals(ModelValidator.checkNumberOfSeats(0), false)
    }

    @Test
    fun checkValueIsEmail() {
        assertEquals(ModelValidator.checkValueIsEmail("action@action.action"), true)
        assertEquals(ModelValidator.checkValueIsEmail("action@action."), false)
        assertEquals(ModelValidator.checkValueIsEmail("action@action"), false)
        assertEquals(ModelValidator.checkValueIsEmail("action@.sd"), false)
        assertEquals(ModelValidator.checkValueIsEmail("action@"), false)
        assertEquals(ModelValidator.checkValueIsEmail("@action.action"), false)
        assertEquals(ModelValidator.checkValueIsEmail("@"), false)
        assertEquals(ModelValidator.checkValueIsEmail("action.action"), false)
        assertEquals(ModelValidator.checkValueIsEmail("as d@sd"), false)
        assertEquals(ModelValidator.checkValueIsEmail("action@action .action"), false)
        assertEquals(ModelValidator.checkValueIsEmail(""), false)
    }

    @Test
    fun checkValueIsPlate() {
        assertEquals(ModelValidator.checkValueIsPlate("AA123AA"), true)
        assertEquals(ModelValidator.checkValueIsPlate("AA123A"), false)
        assertEquals(ModelValidator.checkValueIsPlate("A123AA"), false)
        assertEquals(ModelValidator.checkValueIsPlate("123"), false)
        assertEquals(ModelValidator.checkValueIsPlate("AA13AA"), false)
        assertEquals(ModelValidator.checkValueIsPlate("AA1AA"), false)
        assertEquals(ModelValidator.checkValueIsPlate("AAAA"), false)
        assertEquals(ModelValidator.checkValueIsPlate("A1AA123A"), false)
        assertEquals(ModelValidator.checkValueIsPlate("12AAA12"), false)
        assertEquals(ModelValidator.checkValueIsPlate("A 123AA"), false)
    }

    @Test
    fun checkValueIsEmpty() {
        assertEquals(ModelValidator.checkValueIsEmpty("action"), true)
        assertEquals(ModelValidator.checkValueIsEmpty(""), false)
        assertEquals(ModelValidator.checkValueIsEmpty(" "), true)
    }

    @Test
    fun checkStringLength() {
        assertEquals(ModelValidator.checkStringLength("action", 3), true)
        assertEquals(ModelValidator.checkStringLength("action", 2), true)
        assertEquals(ModelValidator.checkStringLength("action", 4), false)
        assertEquals(ModelValidator.checkStringLength("", 0), true)
    }

    @Test
    fun checkStringsEqual() {
        assertEquals(ModelValidator.checkStringsEqual("action", "action"), true)
        assertEquals(ModelValidator.checkStringsEqual("action", "as"), false)
        assertEquals(ModelValidator.checkStringsEqual("sd", "action"), false)
        assertEquals(ModelValidator.checkStringsEqual("", ""), true)
    }
}