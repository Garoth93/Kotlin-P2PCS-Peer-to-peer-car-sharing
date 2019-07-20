package com.fourteenrows.p2pcs.activities.general_activity

import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.objects.items.Item
import com.fourteenrows.p2pcs.objects.quests.ActiveQuest
import kotlin.random.Random

@Suppress("UNCHECKED_CAST")
class GeneralActivityPresenter(private val view: IGeneralView, private val database: ModelDatabase) :
    IGeneralActivityPresenter, IItemListener {
    private lateinit var quest: ActiveQuest
    private lateinit var selectedItem: String
    private lateinit var box: Map<String, Map<String, Any>>

    override fun notifyCompleteQuest(quest: ActiveQuest) {
        this.quest = quest
        requestRandomItem()
    }

    private fun requestRandomItem() {
        database.getBox(quest.box)
            .addOnSuccessListener {
                box = (it.data as Map<String, Map<String, Any>>?)!!
                if (box.isEmpty()) throw Exception("Empty box")
                var chooseAvatar = Random.nextInt(0, 2) == 0
                chooseAvatar = false
                if (chooseAvatar)
                    ItemGetter(this).getAvatar(box as Map<String, ArrayList<String>>, "")
                else
                    ItemGetter(this).getBooster(box as Map<String, ArrayList<String>>, "")
            }
    }

    override fun showItem(item: Item) {
        selectedItem = item.iid
        view.makeRewardDialog(
            quest.gaia_coins,
            quest.exp,
            quest.exp,
            item.name,
            "",
            this
        ) //TODO("Set image al posto di null")
    }

    override fun changeReward() {
        if (box.size != 1) {
            requestRandomItem()
        }
    }
}