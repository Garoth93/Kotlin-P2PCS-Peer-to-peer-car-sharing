package com.fourteenrows.p2pcs.activities.reservation.add_reservation.add_reservation_vehicle

import com.fourteenrows.p2pcs.objects.cars.VehicleObject

interface IAddReservationVehicleListener {
    fun onFailure()
    fun onSuccess(reservation: ArrayList<VehicleObject>)
    fun selectVehicle(cid: String, model: String, owner: String)
}
