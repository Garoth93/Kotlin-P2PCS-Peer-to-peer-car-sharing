package com.fourteenrows.p2pcs.activities.car.add_car

import android.net.Uri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.model.utility.ModelUtils
import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.fourteenrows.p2pcs.model.volley.VolleySingleton
import com.fourteenrows.p2pcs.objects.cars.Vehicle
import com.fourteenrows.p2pcs.services.ReservationService
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_car.*
import java.util.*
import java.util.stream.Collectors

class AddCarPresenter(toView: AddCarActivity, private val database: ModelDatabase = ModelFirebase()) :
    IAddCarPresenter {

    private var view = toView
    private var uri: Uri? = null
    private val NULL_DATE = Date(32500915200000)

    override fun checkPlateAlreadyExists(car: Vehicle) {
        view.startProgressDialog()
        database.checkNewPlate(car.plate)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.documents.size > 0) {
                        onFailure()
                    } else {
                        onSuccess(car)
                    }
                }
            }
    }

    override fun setImageUri(imageUri: Uri) {
        uri = imageUri
    }

    override fun checkVehicleValues(
        plate: String,
        model: String,
        seats: String,
        location: String,
        endDate: String,
        immatricolazione: String
    ) {
        arrayOf(plate, model, seats, location)
            .forEach {
                if (!ModelValidator.checkValueIsEmpty(it)) {
                    view.makeAlertDialog(R.string.all_fields_required, R.string.error)
                    return
                }
            }

        if (!ModelValidator.checkValueIsPlate(plate)) {
            view.makeAlertDialog(R.string.plate_not_plate, R.string.error)
            return
        }

        if (!ModelValidator.checkValueIsEmpty(immatricolazione)
            && immatricolazione.toInt() > 1980
            && immatricolazione.toInt() <= Calendar.getInstance().get(Calendar.YEAR)
        ) {
            view.makeAlertDialog(R.string.errore_immatricolazione, R.string.error)
            return
        }

        if (!ModelValidator.checkNumberOfSeats(seats.toLong())) {
            view.makeAlertDialog(R.string.number_of_seats, R.string.error)
            return
        }

        val date =
            if (ModelValidator.checkValueIsEmpty(endDate)) {
                ModelDates.truncateDateToDay(Date(endDate.toLong()))
            } else {
                NULL_DATE
            }
        val point = "45.411154, 11.887554"
        val url =
            "https://maps.googleapis.com/maps/api/geocode/json?address=${location.replace(
                " ",
                "+"
            )}&language=it&key=AIzaSyCOC1mSTaQw8jLdmN8VD5nP_s2Bn2jqi6w"
        val jsonOR = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                val status = it.getString("status")
                if (status == "OK") {
                    val locs =
                        it.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                    val lat = locs.getDouble("lat")
                    val lng = locs.getDouble("lng")

                    val car = Vehicle(
                        date,
                        model,
                        database.getUid()!!,
                        plate,
                        seats.toLong(),
                        true,
                        immatricolazione,
                        location,
                        "",
                        GeoPoint(lat, lng)
                    )
                    checkPlateAlreadyExists(car)
                } else
                    view.makeAlertDialog(R.string.noLocation, R.string.error)

            }, Response.ErrorListener {
                view.makeAlertDialog(R.string.location_error, R.string.error)
            })
        VolleySingleton.getInstance(view).addToRequestQueue(jsonOR)
    }

    override fun formatDate(value: Date) = ModelUtils.formatDate(value)

    override fun nextNDays(date: Int) = ModelDates.nextNDays(date)

    override fun onFailure() {
        view.stopProgressDialog()
        view.makeAlertDialog(R.string.plate_already_in, R.string.error)
        return
    }

    override fun onSuccess(car: Vehicle) {
        database.insertVehicle(car)
            .addOnSuccessListener {
                if (uri != null) {
                    FirebaseStorage.getInstance()
                        .getReference(ImageHelper().getNameOf(it.id))
                        .putFile(uri!!)
                        .addOnSuccessListener { _ ->
                            ImageHelper().setCarImage(view.imageCar, it.id)
                        }
                }
                view.stopProgressDialog()
                view.changeView(R.string.vehicle_insert, R.string.success)
                updateService()
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
}