package com.fourteenrows.p2pcs.quests

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.UITest
import com.fourteenrows.p2pcs.activities.quest.QuestActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.fourteenrows.p2pcs.model.database.ModelFirebase

@RunWith(AndroidJUnit4::class)
class Quests : UITest() {

    @get:Rule
    var activityRule: QuestsRule =
        QuestsRule(QuestActivity::class.java)

    private lateinit var first: String
    private lateinit var confirmation: String
    private lateinit var refreshed: String
    private lateinit var conf: String
    private lateinit var no_coins: String
    private lateinit var ok: String

    @Before
    fun setUp() {
        first = activityRule.activity.baseContext.resources.getString(R.string.quest_free_change)
        confirmation = activityRule.activity.baseContext.resources.getString(R.string.quest_cost_change)
        refreshed = activityRule.activity.baseContext.resources.getString(R.string.quest_change)
        conf = activityRule.activity.baseContext.resources.getString(R.string.confirm)
        no_coins = activityRule.activity.baseContext.resources.getString(R.string.not_enough_coins)
        ok = activityRule.activity.baseContext.resources.getString(R.string.ok)
    }


   @Test
    public fun testRefresh() {
       ModelFirebase().pastChangeQuest()
        //pagina quests
        clickChildViewWithId(R.id.changeQuest)
        wait(1)
        //refresh
        clickItemInRecycleView(0, R.id.changeQuest)
        //messaggio di cambio missione
       wait(1)
        onView(ViewMatchers.withText(confirmation))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //ok
        onView(ViewMatchers.withText(conf))
            .perform(click())
    }


    @Test
    public fun testNoMoney() {
        ModelFirebase().authenticateUser("mecixo@top-mailer.net", "qwerty")
        ModelFirebase().pastChangeQuest()
    //pagina quests
    clickChildViewWithId(R.id.changeQuest)
    wait(1)
    //refresh
    clickItemInRecycleView(0, R.id.changeQuest )
    //messaggio di cambio missione
    onView(ViewMatchers.withText(no_coins))
    .inRoot(RootMatchers.isDialog())
    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    //ok
    onView(ViewMatchers.withText(ok))
    .perform(click())
   }

    @Test
    public fun testFirstRefresh() {
        ModelFirebase().resetLastFreeChangeQuest()
        //pagina quests
        clickChildViewWithId(R.id.changeQuest)
        //ModelFirebase().resetLastFreeChangeQuest()
        wait(1)
        //refresh
        clickItemInRecycleView(0, R.id.changeQuest)
        //messaggio di cambio missione
        onView(ViewMatchers.withText(first))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //ok
        onView(ViewMatchers.withText(conf))
            .perform(click())

    }
}