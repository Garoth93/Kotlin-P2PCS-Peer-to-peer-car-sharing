package com.fourteenrows.p2pcs.reservations
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.UITest
import com.fourteenrows.p2pcs.activities.reservation.ReservationActivity
import com.fourteenrows.p2pcs.activities.car.CarActivity
import androidx.test.espresso.contrib.PickerActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import org.hamcrest.core.AllOf.allOf

@RunWith(AndroidJUnit4::class)
class Reservations : UITest() {

    @get:Rule
    var activityRule: ReservationsRule =
        ReservationsRule(ReservationActivity::class.java)

    private lateinit var ok: String
    private lateinit var conf: String
    private lateinit var selDateTZone: String
    private lateinit var tZoneField: String
    private lateinit var carField: String
    private lateinit var confDelete: String
    private lateinit var resSuccesful: String
    private lateinit var allFieldsFull: String
    private lateinit var evviva: String


    @Before
    fun setUp() {
        ok = activityRule.activity.baseContext.resources.getString(R.string.ok)
        conf = activityRule.activity.baseContext.resources.getString(R.string.confirm)
        resSuccesful = activityRule.activity.baseContext.resources.getString(R.string.reservation_successful)
        tZoneField = activityRule.activity.baseContext.resources.getString(R.string.reservation_zone)
        confDelete = activityRule.activity.baseContext.resources.getString(R.string.reservation_delete)
        allFieldsFull = activityRule.activity.baseContext.resources.getString(R.string.all_fields_required)
        selDateTZone = activityRule.activity.baseContext.resources.getString(R.string.reservation_fill)
    }


    @Test
    fun allFields() {
        clickChildViewWithId(R.id.addReservation)
        wait(2)
        onView(withId(R.id.addReservation))
            .perform((click()))
        onView(withId(R.id.reservationConfirm))
            .perform((click()))
        onView(ViewMatchers.withText(allFieldsFull))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(ok))
            .perform(click())


    }

    @Test
    fun dateTimeFirst() {
        clickChildViewWithId(R.id.addReservation)
        wait(2)
        onView(withId(R.id.addReservation))
            .perform((click()))
        onView(withId(R.id.reservationCar))
            .perform((click()))
        onView(ViewMatchers.withText(selDateTZone))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(ok))
            .perform(click())

    }

    @Test
    fun insertReservation() {
        wait(2)
        clickChildViewWithId(R.id.addReservation)
        wait(2)
        onView(withId(R.id.addReservation))
            .perform((click()))
        onView(withId(R.id.reservationDate))
            .perform((click()))
        onView(ViewMatchers.withText(ok))
            .perform(click())
        onView(withId(R.id.reservationZone))
            .perform((click()))
        onView(ViewMatchers.withText("Scegli fascia"))
            .perform(click())
        onView(ViewMatchers.withText("00:00 - 06:00"))
            .perform(click())
        onView(withId(R.id.reservationCar))
           .perform((click()))
        clickItemInRecycleView(1, R.id.selectedVehicle)
        onView(withId(R.id.reservationConfirm))
            .perform((click()))
        onView(ViewMatchers.withText(resSuccesful))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        wait(1)
        onView(ViewMatchers.withText(ok))
            .perform(click())
    }

    @Test
    fun deleteReservation() {
        clickChildViewWithId(R.id.delete_icon)
        wait(2)
        clickItemInRecycleView(3, R.id.delete_icon)
        onView(ViewMatchers.withText("Alfa MiTo"))
            .perform((ViewActions.swipeLeft()))
        onView(ViewMatchers.withText(confDelete))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(conf))
            .perform(click())
    }

}
