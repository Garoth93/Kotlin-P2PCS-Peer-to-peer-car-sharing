package com.fourteenrows.p2pcs.objects.quests

import java.util.*

class ActiveQuest(
    override val name: String,
    override val box: String,
    override val gaia_coins: Long,
    override val exp: Long,
    override val category: Long,
    override val target: Long,
    override val qid: String,
    val activationTime: Date,
    val completed: Date,
    var progress: Long
) : Quest(name, box, gaia_coins, exp, category, target, qid) {

    fun getState(): String? {
        return when (questType) {
            QuestTypes.trip -> "$progress viaggi in una prenotazione di $target"
            QuestTypes.totalTrip -> "$progress viaggi totali di $target"
            QuestTypes.totalKm -> "$progress kilometri totali di $target"
            QuestTypes.km -> "$progress kilometri in un viaggio di $target"
            QuestTypes.person -> "$progress passeggeri in un viaggio di $target"
            QuestTypes.totalPerson -> "$progress  di $target"
            QuestTypes.totalReservation -> "$progress prenotazioni totali di $target"
            QuestTypes.none -> throw java.lang.Exception("Quest non utilizzabile")
            else -> throw Exception("Descrizione quest non gestita")
        }
    }
}
