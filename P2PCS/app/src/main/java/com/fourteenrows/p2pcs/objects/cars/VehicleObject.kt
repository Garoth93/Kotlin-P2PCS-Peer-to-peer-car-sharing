package com.fourteenrows.p2pcs.objects.cars

import java.io.Serializable
import kotlin.random.Random

data class VehicleObject(
    val model: String,
    val seats: Long,
    val cid: String,
    val owner: String,
    val distance: Int = Random.nextInt(0, 50),
    val immatricolazione: String
) : Serializable