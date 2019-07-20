package com.fourteenrows.p2pcs.activities.reservation.add_reservation

import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView
import com.fourteenrows.p2pcs.objects.reservations.ReservationVehicle

interface IAddReservationView : IGeneralView {
    fun changeView(message: Int, title: Int)
    fun chooseVehicle(date: String, zone: String)
    fun makeCalendarDialog()
    fun makeRadioDialog()
    fun refresh()
    fun refillContent(vehicle: ReservationVehicle)
    fun sendMail(email: String)
}