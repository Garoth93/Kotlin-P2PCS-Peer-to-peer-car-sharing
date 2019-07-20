package com.fourteenrows.p2pcs.Profile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.UITest
import com.fourteenrows.p2pcs.activities.profile.ProfileActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Profile : UITest() {

    @get:Rule
    var activityRule: ProfileRule =
        ProfileRule(ProfileActivity::class.java)

    private lateinit var ok: String
    private lateinit var fieldRequired: String
    private lateinit var registrationSuccessful: String
    private lateinit var passwordChange: String

    @Before
    fun initStrings() {
        ok = activityRule.activity.baseContext.resources.getString(R.string.ok)
        fieldRequired = activityRule.activity.baseContext.resources.getString(R.string.field_required)
        passwordChange = activityRule.activity.baseContext.resources.getString(R.string.password_change)
    }

    @Test
    fun emptyName() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.alertTextField, "")

        writeEditTextInAlertAndGetDialog(map,
            R.id.btnImmatricolazione,
            confirm,
            fieldRequired
        )
    }

    @Test
    fun emptySurname() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.alertTextField, "")

        writeEditTextInAlertAndGetDialog(map,
            R.id.btnTarga,
            confirm,
            fieldRequired
        )
    }

    /*
    @Test
    fun sendEmailPassword() {
        sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.changePassword))
            .perform((ViewActions.click()))
        Espresso.onView(ViewMatchers.withText(passwordChange))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(ok))
            .perform((ViewActions.click()))
    }
    */
}