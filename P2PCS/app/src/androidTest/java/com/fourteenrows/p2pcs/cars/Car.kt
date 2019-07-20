package com.fourteenrows.p2pcs.cars

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.UITest
import com.fourteenrows.p2pcs.activities.car.CarActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Car : UITest() {

    @get:Rule
    var activityRule: CarRule =
        CarRule(CarActivity::class.java)


    private lateinit var allFieldsRequired: String
    private lateinit var success: String
    private lateinit var ok: String


    @Before
    fun clickFirstItem() {
        clickItemInRecycleView(0, R.id.deleteVehicle)
        waitView(withId(R.id.editCar), ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Before
    fun initStrings() {
        allFieldsRequired = activityRule.activity.baseContext.resources.getString(R.string.all_fields_required)
        success = activityRule.activity.baseContext.resources.getString(R.string.success)
        ok = activityRule.activity.baseContext.resources.getString(R.string.ok)
    }

    private fun emptyBase(id: Int) {
        val map: MutableMap<Int, String> = mutableMapOf()

        map.put(id, "")

        writeEditTextAndGetDialogWithId(map, R.id.editVehicleConfirm, allFieldsRequired)
    }

    private fun clickAndEditAvailability() {
        onView(withId(R.id.editVehicleInstant)).perform(click())
        onView(withId(R.id.editVehicleConfirm)).perform(click())
        onView(ViewMatchers.withText(success))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(ok))
            .perform((click()))
    }

    @Test
    fun emptyPlate() {
        emptyBase(R.id.editVehiclePlate)
    }

    @Test
    fun emptyVehicleModel() {
        emptyBase(R.id.editVehicleModel)
    }

    @Test
    fun emptyVehicleSeats() {
        emptyBase(R.id.editVehicleSeats)
    }

    /*@Test
    fun emptyDate() {
        onView(withId(R.id.clearEndDate))
            .perform((click()))
        onView(withId(R.id.editVehicleConfirm))
            .perform((click()))
        onView(ViewMatchers.withText("Compila tutti i campi"))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }*/

    @Test
    fun vaildAvailabilityEdit() {
        clickAndEditAvailability()
        wait(2)
        onView(ViewMatchers.withText(R.string.vehicle_available)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        clickFirstItem()
        clickAndEditAvailability()
    }


}