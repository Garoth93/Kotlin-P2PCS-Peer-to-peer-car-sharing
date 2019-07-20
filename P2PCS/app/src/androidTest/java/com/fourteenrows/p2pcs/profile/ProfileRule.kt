package com.fourteenrows.p2pcs.Profile

import androidx.test.rule.ActivityTestRule
import com.fourteenrows.p2pcs.activities.profile.ProfileActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import java.lang.Thread.sleep


class ProfileRule(activityClass: Class<ProfileActivity>) : ActivityTestRule<ProfileActivity>(activityClass) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        ModelFirebase().authenticateUser("fourteenrows@gmail.com", "testacc")
        sleep(3000)
    }
}