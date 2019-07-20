package com.fourteenrows.p2pcs.objects.user

import java.io.Serializable
import java.util.*

class User(val name: String,
           val surname: String,
           val mail: String,
           val exp: Long = 0,
           val gaia_coins: Long = 0,
           val week_points: Long = 0,
           val last_free_change_quest: Date,
           val last_daily_new_quest: Date
) : Serializable {

    fun canShop(cost: Long) = gaia_coins > cost
}