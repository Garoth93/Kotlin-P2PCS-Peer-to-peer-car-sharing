package com.fourteenrows.p2pcs.shop
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
import com.fourteenrows.p2pcs.activities.shop.ShopActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.fourteenrows.p2pcs.model.database.ModelFirebase

@RunWith(AndroidJUnit4::class)
class Shop : UITest() {

    @get:Rule
    var activityRule: ShopRule =
        ShopRule(ShopActivity::class.java)

    private lateinit var no_coins: String
    private lateinit var ok: String
    private lateinit var confirm_pur: String
    private lateinit var conf: String


    @Before
    fun setUp() {
        no_coins = activityRule.activity.baseContext.resources.getString(R.string.not_enough_coins)
        ok = activityRule.activity.baseContext.resources.getString(R.string.ok)
        confirm_pur = activityRule.activity.baseContext.resources.getString(R.string.confirm_purchase)
        conf = activityRule.activity.baseContext.resources.getString(R.string.confirm)
    }

 @Test
 fun testShop() {
    clickChildViewWithId(R.id.shop)
    wait(1)
    //refresh
    clickItemInRecycleView(1, R.id.shop )
     wait(1)
    //messaggio di cambio missione
    onView(ViewMatchers.withText(confirm_pur))
    .inRoot(RootMatchers.isDialog())
    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    //ok
    onView(ViewMatchers.withText(conf))
    .perform(click())
   }

    @Test
    public fun testNoMoney() {
        ModelFirebase().authenticateUser("mecixo@top-mailer.net", "qwerty")
        wait(1)
        //pagina quests
        clickChildViewWithId(R.id.shop)
        wait(1)
        //refresh
        clickItemInRecycleView(1, R.id.shop )
        //messaggio di cambio missione
        onView(ViewMatchers.withText(no_coins))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //ok
        onView(ViewMatchers.withText(conf))
            .perform(click())
    }
}