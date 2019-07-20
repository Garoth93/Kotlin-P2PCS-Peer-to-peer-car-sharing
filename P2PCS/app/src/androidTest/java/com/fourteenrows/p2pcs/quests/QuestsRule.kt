package com.fourteenrows.p2pcs.quests


import androidx.test.rule.ActivityTestRule
import com.fourteenrows.p2pcs.activities.quest.QuestActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import java.lang.Thread.sleep

class QuestsRule(activityClass: Class<QuestActivity>) : ActivityTestRule<QuestActivity>(activityClass) {
    override fun beforeActivityLaunched() {
        ModelFirebase().authenticateUser("fourteenrows@gmail.com", "testacc")
        super.beforeActivityLaunched()
        sleep(3000)
    }
}