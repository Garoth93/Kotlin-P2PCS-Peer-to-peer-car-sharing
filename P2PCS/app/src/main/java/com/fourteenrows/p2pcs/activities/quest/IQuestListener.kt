package com.fourteenrows.p2pcs.activities.quest

import com.google.firebase.firestore.QuerySnapshot

interface IQuestListener {
    fun changeQuestRequest(qid: String)
    fun onFailure()
    fun onSuccess(it: QuerySnapshot)
}