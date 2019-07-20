package com.fourteenrows.p2pcs.activities.quest

import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView

interface IQuestView : IGeneralView {
    fun makeConfirmationDialog(cost: Long, userGC: Long, qid: String, level: Long)
    fun makeConfirmationDialogFreeChange(qid: String, level: Long)
    fun setRecyclerAdapter(adapter: QuestRecyclerAdapter)
}