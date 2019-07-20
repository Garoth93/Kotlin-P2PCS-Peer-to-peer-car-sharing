package com.fourteenrows.p2pcs.activities.reservation.add_reservation.add_reservation_vehicle

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.reservation.add_reservation.AddReservationActivity
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.model.utility.ModelUtils
import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.fourteenrows.p2pcs.objects.cars.VehicleObject
import com.fourteenrows.p2pcs.objects.reservations.ReservationVehicle
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_add_reservation_vehicle.*
import java.util.*
import kotlin.collections.ArrayList

class AddReservationVehiclePresenter(
    toView: AddReservationVehicleActivity,
    private val database: ModelDatabase = ModelFirebase()
) :
    IAddReservationVehiclePresenter, IAddReservationVehicleListener {

    private var view = toView
    var vehicle: ReservationVehicle
    init {
        vehicle = view.intent.getSerializableExtra("vehicle") as ReservationVehicle
        view.startProgressDialog()
        fetchVehicles()
    }

    override fun fetchVehicles() {
        val date = ModelUtils.fixDate(vehicle.date.toString())
        val timeSlot = vehicle.zone
        val startSlot = ModelDates.setSlotToDateStart(Date(date), timeSlot)
        val endSlot = ModelDates.setSlotToDateEnd(Date(date), timeSlot)

        database.fetchAvailableVehicles(ModelDates.nextDayOf(date, timeSlot))
            .addOnSuccessListener { it ->
                val availableVehicles = ArrayList<String>()
                it.documents.forEach { v ->
                    availableVehicles.add(v.id)
                }

                database.getReservationOf(Date(date))
                    .addOnSuccessListener { qs1 ->

                        val dateDocs = qs1.documents
                        /* Because firestore fà where SOLO SU UN CAZZO DI CAMPO ALLA VOLTA!!!!!!!!!!!!!! */
                        FirebaseFirestore.getInstance()
                            .collection("Dates")
                            .whereLessThanOrEqualTo("end_slot", ModelDates.nextDayOf(Date(date)))
                            .get()
                            .addOnSuccessListener { qs2 ->
                                dateDocs.removeIf { d ->
                                    !(qs2.documents.map { it ->
                                        it.id
                                    }.contains(d.id))
                                }

                                val busyVehicle = ArrayList<String>()
                                dateDocs.forEach { dates ->
                                    val start = (dates["start_slot"] as Timestamp).toDate()
                                    val end = (dates["end_slot"] as Timestamp).toDate()

                                    if (!(start < startSlot && end < endSlot) &&
                                        !(start > startSlot && start < endSlot) &&
                                        !(end > startSlot && end < endSlot)
                                    ) {
                                        busyVehicle.add(dates["carId"] as String)
                                    }
                                }
                                availableVehicles.removeIf { cid ->
                                    busyVehicle.contains(cid)
                                }

                                val cars = ArrayList<Map<String, Any>>()
                                database.getCars()
                                    .addOnSuccessListener {
                                        it.documents.forEach { c ->
                                            if (availableVehicles.contains(c.id)) {
                                                val map = c.data!!.toMap() as MutableMap
                                                if (map["owner"] as String != database.getUid()) {
                                                    map["cid"] = c.id
                                                    cars.add(map)
                                                }
                                            }
                                        }
                                        onSuccess(getVehicles(cars))
                                    }
                            }
                    }
            }
    }

    private fun getVehicles(map: ArrayList<Map<String, Any>>): ArrayList<VehicleObject> {
        val vehicles = ArrayList<VehicleObject>()
        val longitude = view.intent.getDoubleExtra("longitude", 45.411154)
        val latitude = view.intent.getDoubleExtra("latitude", 11.887554)
        val here = GeoPoint(latitude, longitude)

        map.forEach {
            val geoPoint = it["geopoint"] as GeoPoint

            vehicles.add(
                VehicleObject(
                    it["model"] as String,
                    it["seats"] as Long,
                    it["cid"] as String,
                    it["owner"] as String,
                    getDistance(geoPoint, here).toInt(),
                    it["immatricolazione"] as String
                )
            )
        }
        return vehicles
    }

    private fun getDistance(geoPoint: GeoPoint, here: GeoPoint): Double {
        val earthRadiusKm = 4;
        val dLat = (here.latitude - geoPoint.latitude) * Math.PI / 180
        val dLon = (here.longitude - geoPoint.longitude) * Math.PI / 180
        val lat1 = geoPoint.latitude * Math.PI / 180
        val lat2 = here.latitude * Math.PI / 180
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadiusKm * c
    }

    override fun onSuccess(reservation: ArrayList<VehicleObject>) {
        view.stopProgressDialog()
        if (reservation.size != 0) {
            view.setRecyclerAdapter(AddReservationVehicleRecyclerAdapter(reservation, this))
        } else {
            (view as Activity).message.visibility = android.view.View.VISIBLE
            (view as Activity).recycleView.visibility = android.view.View.GONE
        }
    }

    override fun onFailure() {
        TODO("not implemented")
    }

    override fun selectVehicle(cid: String, model: String, owner: String) {
        val intent = Intent(view, AddReservationActivity::class.java)
        vehicle.carId = cid
        vehicle.model = model
        vehicle.owner = owner
        intent.removeExtra("vehicle")
        intent.putExtra("vehicle", vehicle)
        startActivity(view, intent, null)
    }

    override fun updateData(field: String, input: String) {
        if (!ModelValidator.checkValueIsEmpty(input)) {
            view.makeAlertDialog(R.string.field_required, R.string.error)
            return
        }
        database.updateField(field, input)
        view.refresh()
    }
}