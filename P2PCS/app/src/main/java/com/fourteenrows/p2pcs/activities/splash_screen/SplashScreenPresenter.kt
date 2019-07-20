package com.fourteenrows.p2pcs.activities.splash_screen

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.fourteenrows.p2pcs.activities.authentication.login.LoginActivity
import com.fourteenrows.p2pcs.activities.reservation.ReservationActivity
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.objects.quests.QuestAuthorizing
import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.ArrayList

class SplashScreenPresenter(
    toView: ISplashScreenView,
    private val database: ModelDatabase = ModelFirebase()
) :
    ISplashScreenPresenter {

    private val view = toView

    init {
        lateinit var intent: Intent
        if (verifyUserIsLoggedIn()) {
            getNewDailyQuest()
            view.updateToolbar()
                .addOnSuccessListener {
                    intent = Intent((view as Activity).baseContext, ReservationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity((view as Activity), intent, null)
                }
        } else {
            intent = Intent((view as Activity).baseContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity((view as Activity), intent, null)
        }
    }

    private fun getNewDailyQuest() {
        database.getUserDocument()
            .addOnSuccessListener {
                val lastDailyNewQuest = (it.data!!["last_daily_new_quest"] as Timestamp).toDate()
                if (!ModelDates.isSameDay(lastDailyNewQuest, Date())) {
                    getNewQuest()
                }
            }
    }

    private fun getNewQuest() {
        database.getActiveQuests()
            .addOnSuccessListener {
                val activeQuests = ArrayList<String>()
                it.documents.forEach { aq ->
                    activeQuests.add(aq.id)
                }
                if (activeQuests.size < 4) {
                    val questAuthorizer =
                        QuestAuthorizing(1) //TODO(USE REAL LEVEL)
                    val choosenQuest = questAuthorizer.getQuestForWithStrings(activeQuests)
                    database.insertQuest(choosenQuest, database.getUid())
                }
            }
    }

    override fun verifyUserIsLoggedIn(): Boolean = database.getCurrentUser() != null
}