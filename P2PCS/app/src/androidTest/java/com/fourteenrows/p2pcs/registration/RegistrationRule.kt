package com.fourteenrows.p2pcs.Registration

import androidx.test.rule.ActivityTestRule
import com.fourteenrows.p2pcs.activities.authentication.registration.RegistrationActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
class RegistrationRule(activityClass: Class<RegistrationActivity>) : ActivityTestRule<RegistrationActivity>(activityClass) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        ModelFirebase().signOut()
        Thread.sleep(3000)
    }
}