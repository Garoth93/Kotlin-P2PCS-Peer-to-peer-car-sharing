package com.fourteenrows.p2pcs.activities.quest

import android.app.Activity
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.objects.quests.ActiveQuest
import com.fourteenrows.p2pcs.objects.quests.QuestAuthorizing
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_quest.*
import java.util.*
import kotlin.collections.ArrayList

class QuestPresenter(toView: IQuestView, private val database: ModelDatabase = ModelFirebase()) :
    IQuestPresenter, IQuestListener {
    private val view = toView
    private val questList = ArrayList<ActiveQuest>()
    private val changeCost = 50L //TODO(To be generalized)

    init {
        view.startProgressDialog()
        getActiveQuest()
    }

    @Synchronized
    private fun addToList(quest: ActiveQuest) {
        questList.add(quest)
        updateRecycler()
    }

    override fun changeQuestRequest(qid: String) {
        database.getUserDocument()
            .addOnSuccessListener {
                val lastFreeChangeQuest = (it.data!!["last_free_change_quest"] as Timestamp).toDate()
                if (ModelDates.isSameDay(lastFreeChangeQuest, Date())) {
                    paidChange(qid)
                } else {
                    view.makeConfirmationDialogFreeChange(qid, (it["exp"] as Long) / 200)
                }
            }
    }

    override fun getActiveQuest() {
        questList.clear()
        database.getActiveQuests()
            .addOnSuccessListener {
                onSuccess(it)
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    private fun getQuestInfo(id: String, data: MutableMap<String, Any>) {
        database.getQuest(id)
            .addOnSuccessListener {
                val map = it.data!!
                addToList(
                    ActiveQuest(
                        map["name"] as String,
                        map["box"] as String,
                        map["gaia_coins"] as Long,
                        map["exp"] as Long,
                        map["category"] as Long,
                        map["target"] as Long,
                        id,
                        (data["activation_time"] as Timestamp).toDate(),
                        (data["completed"] as Timestamp).toDate(),
                        data["progress"] as Long
                    )
                )
            }
    }

    override fun onFailure() {
        view.stopProgressDialog()
    }

    override fun onSuccess(it: QuerySnapshot) {
        val list = it.toMutableList()
        if (it.size() != 0) {
            parseQuest(list)
        } else {
            (view as Activity).message.visibility = android.view.View.VISIBLE
            (view as Activity).recycleView.visibility = android.view.View.GONE
            view.stopProgressDialog()
        }
    }

    private fun paidChange(qid: String) {
        database.getUserDocument()
            .addOnSuccessListener {
                val userGC = it["gaia_coins"] as Long
                if (userGC < changeCost) {
                    view.makeAlertDialog(R.string.not_enough_coins, R.string.error)
                } else {
                    //TODO("Generalize level bounds")
                    view.makeConfirmationDialog(changeCost, userGC, qid, (it["exp"] as Long) / 200)
                }
            }
    }

    private fun parseQuest(quests: MutableList<QueryDocumentSnapshot>) {
        quests.parallelStream()
            .forEach { qds ->
                getQuestInfo(qds.id, qds.data)
            }
    }

    override fun performChangeQuest(cost: Long, userGC: Long, qid: String, level: Long) {
        val newQuest = QuestAuthorizing(level).getQuestFor(questList)
        database.updateLongUserField("gaia_coins", userGC - cost)
            .addOnSuccessListener {
                view.updateToolbar()
                    .addOnSuccessListener {
                        view.updateValues()
                        database.changeQuest(newQuest, qid)
                            .addOnSuccessListener {
                                getActiveQuest()
                            }
                }
            }
    }

    override fun performFreeChangeQuest(qid: String, level: Long) {
        val newQuest = QuestAuthorizing(level).getQuestFor(questList)
        database.updateField("last_free_change_quest", Date())
            .addOnSuccessListener {
                database.changeQuest(newQuest, qid)
                    .addOnSuccessListener {
                        getActiveQuest()
                    }
            }
    }

    @Synchronized
    private fun updateRecycler() {
        view.setRecyclerAdapter(QuestRecyclerAdapter(questList, this))
        if (questList.size > 0) {
            (view as Activity).message.visibility = android.view.View.GONE
        } else {
            (view as Activity).message.visibility = android.view.View.VISIBLE
        }
        view.stopProgressDialog()
    }
}