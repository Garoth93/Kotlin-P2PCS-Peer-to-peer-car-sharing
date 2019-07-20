package com.fourteenrows.p2pcs.activities.reservation.add_reservation

interface IAddReservationPresenter {
    fun checkValues(date: String, zone: String)
    fun insertReservation()
    fun getOwner(owner: String)
    fun getUid(): String?
}