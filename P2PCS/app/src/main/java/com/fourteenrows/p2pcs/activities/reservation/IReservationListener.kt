package com.fourteenrows.p2pcs.activities.reservation

import com.fourteenrows.p2pcs.objects.reservations.ReservationCardObject

interface IReservationListener {
    fun onSuccess(reservation: ArrayList<ReservationCardObject>)
    fun onFailure()
    fun confirmDeletion(rid: String)
}