package com.fourteenrows.p2pcs.objects.reservations

import java.io.Serializable

data class PastReservationReservationCardObject(
    override val cardType: ReservationCardType,
    override val vehicleModel: String,
    override val endDate: String,
    override val hours: String,
    override val rid: String,
    override val carId: String,
    val totalCost: Double
) : ActiveReservationReservationCardObject(cardType, vehicleModel, endDate, hours, rid, carId), Serializable