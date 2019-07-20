package com.fourteenrows.p2pcs.activities.general_activity

import com.fourteenrows.p2pcs.objects.quests.ActiveQuest

interface IGeneralActivityPresenter {
    fun notifyCompleteQuest(quest: ActiveQuest)
    fun changeReward()
}