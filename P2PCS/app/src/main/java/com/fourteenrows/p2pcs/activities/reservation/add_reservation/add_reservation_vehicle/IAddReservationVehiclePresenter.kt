package com.fourteenrows.p2pcs.activities.reservation.add_reservation.add_reservation_vehicle

interface IAddReservationVehiclePresenter {
    fun fetchVehicles()
    fun updateData(field: String, input: String)
}