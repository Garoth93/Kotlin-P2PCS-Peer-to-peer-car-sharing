package com.fourteenrows.p2pcs.cars

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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
class AddRemoveCar : UITest() {

    @get:Rule
    var activityRule: CarRule =
        CarRule(CarActivity::class.java)

    private lateinit var carAlreadyThere: String
    private lateinit var carDeleted: String
    private lateinit var sureDelete: String
    private lateinit var conf: String

    @Before
    fun setUp() {
        carAlreadyThere = activityRule.activity.baseContext.resources.getString(R.string.plate_already_in)
        carDeleted = activityRule.activity.baseContext.resources.getString(R.string.vehicle_delete_success)
        sureDelete = activityRule.activity.baseContext.resources.getString(R.string.confirmation_vehicle)
        conf = activityRule.activity.baseContext.resources.getString(R.string.confirm)
    }

    @Test
    fun addCar() {
        clickChildViewWithId(R.id.addVehicle)
        wait(2)
        onView(withId(R.id.addVehicle))
            .perform((click()))
        wait(1)
        onView(withId(R.id.vehiclePlate)).perform(ViewActions.typeText("FR123FR"))
        onView(withId(R.id.vehicleModel)).perform(ViewActions.typeText("testcar"))
        onView(withId(R.id.vehicleSeats)).perform(ViewActions.typeText("2"))
        wait(1)
        onView(withId(R.id.vehicleConfirm))
            .perform((click()))
    }

    @Test
    fun carAlreadyIn() {
        addCar()
        onView(ViewMatchers.withText(carAlreadyThere))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun deleteCar() {
        clickChildViewWithId(R.id.deleteVehicle)
        wait(2)
        onView(ViewMatchers.withText("FR123FR"))
            .perform((ViewActions.swipeLeft()))
        onView(ViewMatchers.withText(sureDelete))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(conf))
            .perform(click())


    }


}
