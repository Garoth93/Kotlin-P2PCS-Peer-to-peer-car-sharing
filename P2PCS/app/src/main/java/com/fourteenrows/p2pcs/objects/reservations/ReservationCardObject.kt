package com.fourteenrows.p2pcs.objects.reservations

import java.io.Serializable

open class ReservationCardObject(
    open val cardType: ReservationCardType
) : Serializable