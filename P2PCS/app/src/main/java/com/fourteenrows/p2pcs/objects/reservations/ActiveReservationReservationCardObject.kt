package com.fourteenrows.p2pcs.objects.reservations

import java.io.Serializable

open class ActiveReservationReservationCardObject(
    override val cardType: ReservationCardType,
    open val vehicleModel: String,
    open val endDate: String,
    open val hours: String,
    open val rid: String,
    open val carId: String,
    open val confirmed: Boolean = true
) : ReservationCardObject(cardType), Serializable