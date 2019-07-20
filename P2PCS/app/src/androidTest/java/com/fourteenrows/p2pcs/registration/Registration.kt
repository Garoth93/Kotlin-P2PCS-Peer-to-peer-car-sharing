package com.fourteenrows.p2pcs.Registration

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.UITest
import com.fourteenrows.p2pcs.activities.authentication.registration.RegistrationActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Registration : UITest() {

    @get:Rule
    var activityRule: RegistrationRule =
        RegistrationRule(RegistrationActivity::class.java)

    private lateinit var allFieldsRequired: String
    private lateinit var passwordLength: String
    private lateinit var emailAlreadyIn: String
    private lateinit var emailNotEmail: String
    private lateinit var passwordsNotMatching: String
    private lateinit var registrationSuccessful: String

    @Before
    fun initStrings() {
        allFieldsRequired = activityRule.activity.baseContext.resources.getString(R.string.all_fields_required)
        passwordLength = activityRule.activity.baseContext.resources.getString(R.string.password_length)
        emailAlreadyIn = activityRule.activity.baseContext.resources.getString(R.string.email_already_in)
        emailNotEmail = activityRule.activity.baseContext.resources.getString(R.string.email_not_email)
        passwordsNotMatching = activityRule.activity.baseContext.resources.getString(R.string.passwords_not_matching)
        registrationSuccessful = activityRule.activity.baseContext.resources.getString(R.string.registration_successful)
    }

    private fun emptyExcept(itemToRemove: Int) {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.registrationName, name)
        map.put(R.id.registrationSurname, surname)
        map.put(R.id.registrationEmail, email)
        map.put(R.id.registrationPassword1, password)
        map.put(R.id.registrationPassword2, password)

        map.remove(itemToRemove)

        writeEditTextAndGetDialogWithId(map,
            R.id.registrationButton,
            allFieldsRequired
        )
    }

    @Test
    fun emptyName() {
        emptyExcept(R.id.registrationName)
    }

    @Test
    fun emptySurname() {
        emptyExcept(R.id.registrationSurname)
    }

    @Test
    fun emptyEmail() {
        emptyExcept(R.id.registrationEmail)
    }

    @Test
    fun emptyPassword1() {
        emptyExcept(R.id.registrationPassword1)
    }

    @Test
    fun emptyPassword2() {
        emptyExcept(R.id.registrationPassword2)
    }

    @Test
    fun passwordLength() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.registrationName, name)
        map.put(R.id.registrationSurname, surname)
        map.put(R.id.registrationEmail, email)
        map.put(R.id.registrationPassword1, "passw")
        map.put(R.id.registrationPassword2, "passw")

        writeEditTextAndGetDialogWithId(map,
            R.id.registrationButton,
            passwordLength
        )
    }

    @Test
    fun emailAlreadyPresent() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.registrationName, name)
        map.put(R.id.registrationSurname, surname)
        map.put(R.id.registrationEmail, defaultEmail)
        map.put(R.id.registrationPassword1, password)
        map.put(R.id.registrationPassword2, password)

        writeEditTextAndGetDialogWithId(map,
            R.id.registrationButton,
            emailAlreadyIn
        )
    }

    @Test
    fun invalidEmail() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.registrationName, name)
        map.put(R.id.registrationSurname, surname)
        map.put(R.id.registrationEmail, notVaildEmail)
        map.put(R.id.registrationPassword1, password)
        map.put(R.id.registrationPassword2, password)

        writeEditTextAndGetDialogWithId(map,
            R.id.registrationButton,
            emailNotEmail
        )
    }

    @Test
    fun differentPasswords() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.registrationName, name)
        map.put(R.id.registrationSurname, name)
        map.put(R.id.registrationEmail, email)
        map.put(R.id.registrationPassword1, password)
        map.put(R.id.registrationPassword2, password + "123")

        writeEditTextAndGetDialogWithId(map,
            R.id.registrationButton,
            passwordsNotMatching
        )
    }

    /*
    @Test
    fun vaildRegistration() {
        val randEmail = generateEmail()
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.registrationName, name)
        map.put(R.id.registrationSurname, surname)
        map.put(R.id.registrationEmail, randEmail)
        map.put(R.id.registrationPassword1, password)
        map.put(R.id.registrationPassword2, password)

        writeEditTextAndGetDialogWithId(
            map,
            R.id.registrationButton,
            registrationSuccessful
        )
    }
    */
}