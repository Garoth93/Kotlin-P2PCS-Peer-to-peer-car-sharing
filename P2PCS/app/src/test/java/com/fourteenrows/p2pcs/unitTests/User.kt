package com.fourteenrows.p2pcs.unitTests

import com.fourteenrows.p2pcs.model.utility.ModelValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class User {

    @Test
    fun checkValueIsEmail()
    {
        assertTrue(ModelValidator.checkValueIsEmail("bbianchind@gmail.com"))
        assertFalse(ModelValidator.checkValueIsEmail("not mail"))
    }

    @Test
    fun checkValueIsEmpty()
    {
        assertFalse(ModelValidator.checkValueIsEmpty(""))
        assertTrue(ModelValidator.checkValueIsEmpty("not empty"))
    }

    @Test
    fun checkStringLength()
    {
        assertTrue(ModelValidator.checkStringLength("string", 6))
        assertTrue(ModelValidator.checkStringLength("longer string", 6))
        assertFalse(ModelValidator.checkStringLength("string", 7))
    }

    @Test
    fun checkStringsEqual()
    {
        assertTrue(ModelValidator.checkStringsEqual("string", "string"))
        assertFalse(ModelValidator.checkStringsEqual("string", "another string"))
    }
}