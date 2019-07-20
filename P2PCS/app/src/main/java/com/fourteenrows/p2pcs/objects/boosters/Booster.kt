package com.fourteenrows.p2pcs.objects.boosters

import java.io.Serializable

open class Booster(
    open val name: String,
    open val category: Long,
    open val multiplicator: Long,
    open val description: String
) : Serializable {

    val boosterTypes: BoosterTypes by lazy {
        BoosterTypes.of(category.toInt())
    }
}

