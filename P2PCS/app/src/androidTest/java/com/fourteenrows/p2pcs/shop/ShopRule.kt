package com.fourteenrows.p2pcs.shop


import androidx.test.rule.ActivityTestRule
import com.fourteenrows.p2pcs.activities.shop.ShopActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import java.lang.Thread.sleep

class ShopRule(activityClass: Class<ShopActivity>) : ActivityTestRule<ShopActivity>(activityClass) {
    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        ModelFirebase().authenticateUser("fourteenrows@gmail.com", "testacc")
        sleep(3000)
    }
}