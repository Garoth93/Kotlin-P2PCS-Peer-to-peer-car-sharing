package com.fourteenrows.p2pcs.activities.quest

interface IQuestPresenter {
    fun changeQuestRequest(qid: String)
    fun getActiveQuest()
    fun performChangeQuest(cost: Long, userGC: Long, qid: String, level: Long)
    fun performFreeChangeQuest(qid: String, level: Long)
}