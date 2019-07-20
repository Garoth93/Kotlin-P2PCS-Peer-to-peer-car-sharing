package com.fourteenrows.p2pcs.activities.car

import android.app.Activity
import android.view.View
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.model.utility.ModelUtils
import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.fourteenrows.p2pcs.objects.cars.FetchedVehicle
import com.fourteenrows.p2pcs.services.ReservationService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_car.*
import java.util.*
import java.util.stream.Collectors

class CarPresenter(
    private val view: CarActivity,
    private val database: ModelDatabase = ModelFirebase(),
    private val index: Int = 0
) :
    ICarPresenter {
    private val cars = Cars()
    private val NULL_DATE = Date(32500915200000)

    init {
        view.startProgressDialog()
        loadVehicles(index)
    }

    fun loadVehicles(index: Int = 0) {
        cars.clear()
        database.getUserVehicles()
            .addOnSuccessListener {
                if (it.isEmpty)
                    view.stopProgressDialog()
                else {
                    it.documents.forEach { car ->
                        database.getCarActiveReservations(car.id)
                            .addOnSuccessListener { activeReservations ->
                                val map = car.data!!.toMap() as MutableMap
                                val fv = FetchedVehicle(
                                    Date((map["final_availability"] as Timestamp).toDate().time),
                                    map["model"] as String,
                                    database.getUid()!!,
                                    map["plate"] as String,
                                    map["seats"] as Long,
                                    map["instant_availability"] as Boolean,
                                    map["immatricolazione"] as String,
                                    map["location"] as String,
                                    car.id,
                                    true,
                                    map["applicant"] as String,
                                    map["geopoint"] as GeoPoint
                                )
                                if (!activeReservations.isEmpty)
                                    fv.editable = false
                                cars.add(fv)
                                if (cars.size() != 0) {
                                    view.carPresents()
                                    if (cars.size() > index)
                                        cars.index = index
                                    else cars.setFirst()
                                    view.loadVehicle(cars.get()!!)
                                } else {
                                    (view as Activity).mainConstraint.visibility = View.GONE
                                    (view as Activity).mainCard.visibility = View.GONE
                                    (view as Activity).messageNoCar.visibility = View.GONE
                                }
                            }
                    }
                    view.stopProgressDialog()
                }
            }
    }

    override fun deleteCar() {
        database.getCarActiveReservations(cars.get()!!.cid)
            .addOnSuccessListener { activeReservations ->
                if (activeReservations.isEmpty) {
                    database.delete("Cars", cars.get()!!.cid)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                view.makeAlertDialog(R.string.vehicle_delete_success, R.string.success)
                            } else {
                                view.makeAlertDialog(R.string.vehicle_delete_error_db, R.string.error)
                            }
                            loadVehicles()
                            updateService()
                        }
                } else {
                    view.showErrorCanNotDelete()
                }
            }
    }


    private fun updateService() {
        database.getUserVehicles()
            .addOnSuccessListener { cars ->
                val cids = cars.documents
                    .map {
                        it.id
                    }.stream()
                    .collect(Collectors.toList())
                ReservationService.getInstance(view.applicationContext)
                    .updateOwnCarListeners(ArrayList(cids))
            }
    }

    override fun previousVehicle() {
        view.loadVehicle(cars.prev())
    }

    override fun nextVehicle() {
        view.loadVehicle(cars.next())
    }

    override fun formatDate(value: Date) = ModelUtils.formatDate(value)

    override fun nextNDays(date: Int) = ModelDates.nextNDays(date)

    override fun updateCarField(cid: String, field: String, input: Any) {
        database.updateCarField(cid, field, input)
            .addOnCompleteListener {
                database.getUserVehicles()
                    .addOnSuccessListener {
                        it.documents.forEach { car ->
                            val map = car.data!!.toMap() as MutableMap
                            if (cars.get()!!.cid == car.id)
                                cars.update(
                                    FetchedVehicle(
                                        Date((map["final_availability"] as Timestamp).toDate().time),
                                        map["model"] as String,
                                        database.getUid()!!,
                                        map["plate"] as String,
                                        map["seats"] as Long,
                                        map["instant_availability"] as Boolean,
                                        map["immatricolazione"] as String,
                                        map["location"] as String,
                                        car.id,
                                        true,
                                        map["applicant"] as String,
                                        map["geopoint"] as GeoPoint
                                    )
                                )
                        }
                        view.loadVehicle(cars.get()!!)
                    }
            }
    }

    override fun getCid() = cars.get()!!.cid

    override fun checkSeats(input: String) {
        if (input.toLongOrNull() == null || !ModelValidator.checkNumberOfSeats(input.toLong())) {
            view.makeAlertDialog(R.string.number_of_seats, R.string.error)
            return
        }
        updateCarField(cars.get()!!.cid, "seats", input.toLong())
    }

    override fun checkModel(input: String) {
        if (!ModelValidator.checkValueIsEmpty(input)) {
            view.makeAlertDialog(R.string.field_required, R.string.error)
            return
        }
        updateCarField(cars.get()!!.cid, "model", input)
    }

    override fun checkFinalAvailability(input: Date) {
        if (Date(0) != input) {
            updateCarField(cars.get()!!.cid, "final_availability", input)
        } else {
            updateCarField(cars.get()!!.cid, "final_availability", NULL_DATE)
        }
    }

    override fun checkImmatricolazione(input: String) {
        if (input.toLongOrNull() != null
            && input.toInt() > 1980
            && input.toInt() <= Calendar.getInstance().get(Calendar.YEAR)
        ) {
            updateCarField(cars.get()!!.cid, "immatricolazione", input)
        } else {
            view.makeAlertDialog(R.string.annoNonValido, R.string.error)
        }
    }

    override fun checkInstantAvailability(input: Boolean) {
        val car = cars.get()
        if (car != null && car.instant_availability != input)
            updateCarField(cars.get()!!.cid, "instant_availability", input)
    }

    override fun checkPlate(input: String) {
        if (!ModelValidator.checkValueIsPlate(input)) {
            view.makeAlertDialog(R.string.plate_not_plate, R.string.error)
            return
        } else {
            database.checkNewPlate(input)
                .addOnSuccessListener {
                    if (it.size() > 0) {
                        view.makeAlertDialog(R.string.plate_already_in, R.string.error)
                    } else {
                        updateCarField(cars.get()!!.cid, "plate", input)
                    }
                }
        }
    }

    override fun checkLocation(input: String) {
        if (!ModelValidator.checkValueIsEmpty(input)) {
            view.makeAlertDialog(R.string.field_required, R.string.error)
            return
        }
        updateCarField(cars.get()!!.cid, "location", input)
    }

    override fun getIndex() = cars.index

}