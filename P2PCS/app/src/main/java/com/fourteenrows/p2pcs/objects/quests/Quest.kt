package com.fourteenrows.p2pcs.objects.quests

import java.io.Serializable

open class Quest(
    override val name: String,
    override val box: String,
    override val gaia_coins: Long,
    override val exp: Long,
    override val category: Long,
    override val target: Long,
    open val qid: String
) : Serializable, ToDatabaseQuest(name, box, gaia_coins, exp, category, target) {
    val questType: QuestTypes by lazy {
        QuestTypes.of(category.toInt())
    }
}
