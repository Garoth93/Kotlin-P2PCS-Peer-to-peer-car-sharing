package com.fourteenrows.p2pcs.model.database.quest

import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.objects.quests.ActiveQuestDB
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Quest(private val database: FirebaseFirestore, private val authorizer: FirebaseAuth) {

    fun changeQuest(newQuest: String, qid: String): Task<Void> {
        //delete old quest
        database
            .collection("User")
            .document(authorizer.uid!!)
            .collection("ActiveQuests")
            .document(qid)
            .delete()
        //add new quest
        return insertQuest(newQuest, null)
    }

    fun getActiveQuests() = database
        .collection("User")
        .document(authorizer.uid!!)
        .collection("ActiveQuests")
        .get()

    fun getQuest(quest: String) = database
        .collection("Quests")
        .document(quest)
        .get()


    fun insertQuest(newQuest: String, uid: String?): Task<Void> {
        val toUid = uid ?: authorizer.uid!!
        return database
            .collection("User")
            .document(toUid)
            .collection("ActiveQuests")
            .document(newQuest)
            .set(ActiveQuestDB(Date(), ModelDates.MAX_DATE, 0))
    }


    fun updateProgressQuest(qid: String, progress: Long) = database
        .collection("User")
        .document(authorizer.uid!!)
        .collection("ActiveQuests")
        .document(qid)
        .update("progress", progress)

    fun resetLastFreeChangeQuest() = database.collection("User")
        .document(authorizer.uid!!)
        .update("last_free_change_quest", Date(0))

    fun pastChangeQuest() = database.collection("User")
        .document(authorizer.uid!!)
        .update("last_free_change_quest", Date())


}