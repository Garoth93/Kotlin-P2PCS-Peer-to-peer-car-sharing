package com.fourteenrows.p2pcs.cars

import androidx.test.rule.ActivityTestRule
import com.fourteenrows.p2pcs.activities.car.CarActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import java.lang.Thread.sleep


class CarRule(activityClass: Class<CarActivity>) : ActivityTestRule<CarActivity>(activityClass) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        ModelFirebase().authenticateUser("fourteenrows@gmail.com", "testacc")
        sleep(3000)
    }

    /*
override fun afterActivityFinished() {
    super.afterActivityFinished()
    ModelFirebase.signOut()
}

override fun getActivityIntent(): Intent {
// add some custom extras and stuff
    return Intent()
}

override fun afterActivityLaunched() {
    super.afterActivityLaunched()
    // maybe you want to do something here
}
*/
}