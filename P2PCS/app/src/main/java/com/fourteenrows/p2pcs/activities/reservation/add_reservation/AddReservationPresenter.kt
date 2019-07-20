package com.fourteenrows.p2pcs.activities.reservation.add_reservation

import android.app.Activity
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.database.reservation.ReservationRequestToDB
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.fourteenrows.p2pcs.objects.reservations.Reservation
import com.fourteenrows.p2pcs.objects.reservations.ReservationVehicle
import com.google.firebase.Timestamp
import java.util.*

class AddReservationPresenter(toView: AddReservationActivity, private val database: ModelDatabase = ModelFirebase()) :
    IAddReservationPresenter, IAddReservationListener {
    private var view = toView
    var vehicle: ReservationVehicle?

    init {
        if ((view as Activity).intent.extras != null) {
            vehicle = (view as Activity).intent.getSerializableExtra("vehicle") as ReservationVehicle
            view.refillContent(vehicle!!)
        } else {
            vehicle = null
        }
    }

    override fun checkValues(date: String, zone: String) {
        if (!ModelValidator.checkValueIsEmpty(date) || !ModelValidator.checkValueIsEmpty(zone)) {
            view.makeAlertDialog(R.string.reservation_fill, R.string.error)
            return
        }
        view.chooseVehicle(date, zone)
    }

    override fun insertReservation() {
        if (vehicle == null) {
            view.makeAlertDialog(R.string.all_fields_required, R.string.error)
        } else {
            view.startProgressDialog()
            checkIfAlreadyReserved(
                Reservation(
                    vehicle!!.carId,
                    vehicle!!.model,
                    getUid()!!,
                    ModelDates.setSlotToDate(Date(vehicle!!.date), vehicle!!.zone),
                    ModelDates.setSlotToDateEnd(Date(vehicle!!.date), vehicle!!.zone),
                    false
                )
            )
        }
    }

    override fun getUid() = database.getUid()

    override fun getOwner(owner: String) {
        database.getOwner(owner)
            .addOnSuccessListener {
                val mail = it.data!!["mail"] as String
                view.sendMail(mail)
            }
    }

    private fun checkIfAlreadyReserved(reservation: Reservation) {
        database.getCarActiveReservations(reservation.carId)
            .addOnSuccessListener { it ->
                var found = false
                it.forEach { docs ->
                    val map = docs.data
                    if ((map["start_slot"] as Timestamp).toDate() == reservation.start_slot)
                        found = true
                }
                if (!found) {
                    database.getUserDocument()
                        .addOnSuccessListener {
                            database.addReservationRequest(
                                reservation.carId,
                                ReservationRequestToDB(
                                    reservation.model,
                                    it["name"] as String,
                                    reservation.start_slot,
                                    reservation.end_slot,
                                    getUid()!!
                                )
                            ).addOnSuccessListener {
                                view.changeView(R.string.yea_conferma, R.string.success)
                            }
                            view.stopProgressDialog()
                        }
                        .addOnFailureListener {
                            view.stopProgressDialog()
                        }
                    /*database.insertReservation(reservation)
                        .addOnSuccessListener {
                            onSuccess()
                        }*/
                }
            }
    }

    override fun onSuccess() {
        view.stopProgressDialog()
        view.changeView(R.string.reservation_successful, R.string.success)
    }
}