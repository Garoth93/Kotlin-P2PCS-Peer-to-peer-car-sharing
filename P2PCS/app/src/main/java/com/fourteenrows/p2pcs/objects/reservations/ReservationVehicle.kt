package com.fourteenrows.p2pcs.objects.reservations

import java.io.Serializable

data class ReservationVehicle(
    var carId: String,
    var date: Long,
    var model: String,
    var zone: String,
    var owner: String
) : Serializable