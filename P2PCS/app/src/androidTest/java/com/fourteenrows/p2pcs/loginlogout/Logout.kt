package com.fourteenrows.p2pcs.loginlogout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.UITest
import com.fourteenrows.p2pcs.activities.authentication.login.LoginActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Logout : UITest() {

    @get:Rule
    var activityRule: LoginRule =
        LoginRule(LoginActivity::class.java)

    @Test
    fun testLogout(){
        ModelFirebase().authenticateUser("mecixo@top-mailer.net", "qwerty")
        wait(1)
        ModelFirebase().signOut()
    }
}