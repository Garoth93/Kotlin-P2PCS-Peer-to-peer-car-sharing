package com.fourteenrows.p2pcs.objects.reservations

import java.io.Serializable
import java.util.*

class Reservation(
    val carId: String,
    val model: String,
    val owner: String,
    var start_slot: Date,
    var end_slot: Date,
    var deleted: Boolean
) : Serializable