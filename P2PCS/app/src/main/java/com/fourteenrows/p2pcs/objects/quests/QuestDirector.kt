package com.fourteenrows.p2pcs.objects.quests

import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivityPresenter
import com.fourteenrows.p2pcs.activities.general_activity.IGeneralActivityPresenter
import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.google.firebase.Timestamp

class QuestDirector(view: IGeneralView) {
    private val database = ModelFirebase()
    private val quests = ArrayList<ActiveQuest>()
    private var ready = false
    private val listener: IGeneralActivityPresenter = GeneralActivityPresenter(view, database)

    init {
        database.getActiveQuests()
            .addOnSuccessListener {
                it.forEach { aq ->
                    database.getQuest(aq.id)
                        .addOnSuccessListener { questIt ->
                            addToList(
                                ActiveQuest(
                                    questIt["name"] as String,
                                    questIt["box"] as String,
                                    questIt["gaia_coins"] as Long,
                                    questIt["exp"] as Long,
                                    questIt["category"] as Long,
                                    questIt["target"] as Long,
                                    questIt.id,
                                    (aq["activation_time"] as Timestamp).toDate(),
                                    (aq["completed"] as Timestamp).toDate(),
                                    aq["progress"] as Long
                                )
                            )
                        }
                }
                ready = true
            }
    }

    @Synchronized
    private fun addToList(activeQuest: ActiveQuest) = quests.add(activeQuest)

    fun addTripProgress(distance: Long, participants: ArrayList<String>, durate: Long) {
        if (!ready) {
            database.getActiveQuests()
                .addOnSuccessListener {
                    it.forEach { aq ->
                        database.getQuest(aq.id)
                            .addOnSuccessListener { questIt ->
                                val quest = ActiveQuest(
                                    questIt["name"] as String,
                                    questIt["box"] as String,
                                    questIt["gaia_coins"] as Long,
                                    questIt["exp"] as Long,
                                    questIt["category"] as Long,
                                    questIt["target"] as Long,
                                    questIt.id,
                                    (aq["activation_time"] as Timestamp).toDate(),
                                    (aq["completed"] as Timestamp).toDate(),
                                    aq["progress"] as Long
                                )
                                performAddTripProgress(quest, distance, participants, durate)
                            }
                    }
                }
        } else {
            quests.forEach {
                performAddTripProgress(it, distance, participants, durate)
            }
        }
    }

    private fun performAddTripProgress(
        quest: ActiveQuest,
        distance: Long,
        participants: ArrayList<String>,
        durate: Long
    ) {
        var isChanged = true
        when (quest.category.toInt()) {
            QuestTypes.totalKm.ordinal -> quest.progress += distance
            QuestTypes.km.ordinal -> if (quest.progress < distance) quest.progress = distance
            QuestTypes.totalPerson.ordinal -> quest.progress += participants.size
            QuestTypes.person.ordinal -> if (quest.progress < participants.size) quest.progress =
                participants.size.toLong()
            QuestTypes.totalTrip.ordinal, QuestTypes.trip.ordinal -> ++quest.progress
            else -> isChanged = false
        }
        if (isChanged) {
            checkCompleted(quest)
        }
    }

    private fun checkCompleted(quest: ActiveQuest) {
        if (quest.target <= quest.progress) {
            listener.notifyCompleteQuest(quest)
            database.removeQuest(quest.qid)
        } else {
            database.updateProgressQuest(quest.qid, quest.progress)
        }
    }

    fun addReservationProgress() {
        quests.forEach {
            var isChanged = true
            when (it.category.toInt()) {
                QuestTypes.totalReservation.ordinal -> {
                    ++it.progress
                    quests.forEach { q ->
                        if (q.category.toInt() == QuestTypes.trip.ordinal)
                            q.progress = 0
                    }
                }
                else -> isChanged = false
            }
            if (isChanged) {
                //checkCompleted(quest)
            }
        }
    }
}
