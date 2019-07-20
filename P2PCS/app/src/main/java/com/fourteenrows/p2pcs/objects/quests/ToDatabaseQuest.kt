package com.fourteenrows.p2pcs.objects.quests

import java.io.Serializable

open class ToDatabaseQuest(
    open val name: String,
    open val box: String,
    open val gaia_coins: Long,
    open val exp: Long,
    open val category: Long,
    open val target: Long
) : Serializable
