package com.fourteenrows.p2pcs.loginlogout

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.UITest
import com.fourteenrows.p2pcs.activities.authentication.login.LoginActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Login : UITest() {

    @get:Rule
    var activityRule: LoginRule =
        LoginRule(LoginActivity::class.java)

    private lateinit var emailNotEmail: String
    private lateinit var allFieldsRequired: String
    private lateinit var notVerified: String
    private lateinit var resetEmailSent: String
    private lateinit var fieldRequired: String
    private lateinit var wrongCredentialsStr: String
    private lateinit var notRegistered: String

    @Before
    fun initStrings() {
        emailNotEmail = activityRule.activity.baseContext.resources.getString(R.string.email_not_email)
        allFieldsRequired = activityRule.activity.baseContext.resources.getString(R.string.all_fields_required)
        notVerified = activityRule.activity.baseContext.resources.getString(R.string.not_verified)
        resetEmailSent = activityRule.activity.baseContext.resources.getString(R.string.reset_email_sent)
        fieldRequired = activityRule.activity.baseContext.resources.getString(R.string.field_required)
        wrongCredentialsStr = activityRule.activity.baseContext.resources.getString(R.string.wrong_credentials)
        notRegistered = activityRule.activity.baseContext.resources.getString(R.string.not_registered)
    }

    @Test
    fun wrongCredentials() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.loginEmail, defaultEmail)
        map.put(R.id.loginPassword, wrongPassword)

        writeEditTextAndGetDialogWithId(map,
            R.id.loginButton,
            wrongCredentialsStr
        )
    }

    @Test
    fun invalidEmail() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.loginEmail, notVaildEmail)
        map.put(R.id.loginPassword, defaultEmailPassword)

        writeEditTextAndGetDialogWithId(map,
            R.id.loginButton,
            emailNotEmail
        )
    }

    @Test
    fun emailNotCompiled() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.loginPassword, wrongPassword)

        writeEditTextAndGetDialogWithId(map,
            R.id.loginButton,
            allFieldsRequired
        )
    }

    @Test
    fun passwordNotCompiled() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.loginEmail, defaultEmail)

        writeEditTextAndGetDialogWithId(map,
            R.id.loginButton,
            allFieldsRequired
        )
    }

    @Test
    fun notVerifiedEmail() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.loginEmail, notVerifiedEmail)
        map.put(R.id.loginPassword, notVerifiedEmailPassword)

        writeEditTextAndGetDialogWithId(map,
            R.id.loginButton,
            notVerified
        )
    }

    @Test
    fun validLogin() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.loginEmail, defaultEmail)
        map.put(R.id.loginPassword, defaultEmailPassword)

        writeEditTextAndChangeView(map,
            R.id.loginButton,
            R.id.leaderboardButton
        )
    }

    @Test
    fun notRegisteredResetPassword() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.alertTextField, generateEmail())

        writeEditTextInAlertAndGetDialog(map,
            R.id.forgot,
            reset,
            notRegistered
        )
    }

    @Test
    fun emptyEmailResetPassword() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.alertTextField, "")

        writeEditTextInAlertAndGetDialog(map,
            R.id.forgot,
            reset,
            fieldRequired
        )
    }

    @Test
    fun notEmailResetPassword() {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(R.id.alertTextField, notVaildEmail)

        writeEditTextInAlertAndGetDialog(map,
            R.id.forgot,
            reset,
            emailNotEmail
        )
    }
}