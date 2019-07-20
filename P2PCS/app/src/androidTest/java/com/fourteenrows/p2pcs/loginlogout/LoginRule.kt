package com.fourteenrows.p2pcs.loginlogout

import androidx.test.rule.ActivityTestRule
import com.fourteenrows.p2pcs.activities.authentication.login.LoginActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import java.lang.Thread.sleep


class LoginRule(activityClass: Class<LoginActivity>) : ActivityTestRule<LoginActivity>(activityClass) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        ModelFirebase().signOut()

        ModelFirebase().signOut()
        sleep(3000)
    }
}