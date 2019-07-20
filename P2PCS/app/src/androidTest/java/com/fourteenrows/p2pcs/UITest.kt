package com.fourteenrows.p2pcs

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.FailureHandler
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.junit.Assert.fail
import java.lang.Thread.sleep

open class UITest {

    val name: String = "test"
    val surname: String = "test"
    val email: String = "test@test.com"
    val password: String = "password"
    val defaultEmail: String = "fourteenrows@gmail.com"
    val defaultEmailPassword: String = "testacc"
    val notVerifiedEmail: String = "fego@topmailer.info"
    val notVerifiedEmailPassword: String = "qwerty"
    val wrongPassword: String = "wrong"
    val reset: String = "RESET"
    val notVaildEmail: String = "not vaild mail"
    val confirm: String = "CONFERMA"


    private class FindViewFailureHandler : FailureHandler {
        override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
            throw NoMatchingViewException()
        }

        internal inner class NoMatchingViewException : RuntimeException()
    }

    data class clickChildViewWithId(var id: Int) : ViewAction {

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    }


    private fun checkView(viewMatcher: Matcher<View>, viewAssertion: ViewAssertion): Boolean {
        try {
            onView(viewMatcher)
                .withFailureHandler(FindViewFailureHandler())
                .check(viewAssertion)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    protected fun wait(seconds: Int) {
        sleep(seconds.toLong() * 1000)
    }

    protected fun waitView(viewMatcher: Matcher<View>, viewAssertion: ViewAssertion, attemptsLimit: Int = 100) {
        var attemptsLimitTmp = attemptsLimit
        while (!checkView(viewMatcher, viewAssertion) && attemptsLimitTmp > 0) {
            attemptsLimitTmp--
            sleep(50)
        }
        if (attemptsLimitTmp == 0)
            fail("Cannot find view [$viewMatcher] with view assertion [$viewAssertion]")
    }

    protected fun generateEmail(): String {
        val charPool: List<Char> = ('a'..'z') + ('0'..'9')
        val randomString = (1..6)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

        return "$randomString@gmail.com"
    }

    private fun writeEditTextAndPressButtonWithId(boxesId: Map<Int, String>, buttonId: Int) {
        for ((key, value) in boxesId) {
            onView(withId(key))
                .perform(replaceText(value), closeSoftKeyboard())
        }
        onView(withId(buttonId))
            .perform((click()))
    }

    private fun writeEditTextAndPressButtonWithString(boxesId: Map<Int, String>, buttonString: String) {
        for ((key, value) in boxesId) {
            onView(withId(key))
                .perform(replaceText(value), closeSoftKeyboard())
        }
        onView(withText(buttonString))
            .perform((click()))
    }

    protected fun writeEditTextAndGetDialogWithId(
        boxesId: Map<Int, String>,
        buttonId: Int,
        dialogString: String?,
        sleep: Int = 0
    ) {
        writeEditTextAndPressButtonWithId(boxesId, buttonId)
        wait(sleep)
        if (dialogString != null) {
            onView(withText(dialogString))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        }
    }

    protected fun writeEditTextAndGetDialogWithString(
        boxesId: Map<Int, String>,
        buttonString: String,
        dialogString: String?,
        sleep: Int = 0
    ) {
        writeEditTextAndPressButtonWithString(boxesId, buttonString)
        wait(sleep)
        if (dialogString != null) {
            onView(withText(dialogString))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        }
    }

    protected fun writeEditTextAndChangeView(boxesId: Map<Int, String>, buttonId: Int, stringId: Int, sleep: Int = 0) {
        writeEditTextAndPressButtonWithId(boxesId, buttonId)
        wait(sleep)
        waitView(withId(stringId), matches(isDisplayed()))
    }

    protected fun writeEditTextInAlertAndGetDialog(
        boxesId: Map<Int, String>,
        showDialogId: Int,
        buttonString: String,
        dialogString: String?,
        sleep: Int = 0
    ) {
        onView(withId(showDialogId))
            .perform((click()))
        writeEditTextAndGetDialogWithString(boxesId, buttonString, dialogString, sleep)
    }

    protected fun clickItemInRecycleView(position: Int, id: Int) {
        wait(2)
        onView(withId(R.id.recycleView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        clickChildViewWithId(id)
                    )
            )
    }

}
