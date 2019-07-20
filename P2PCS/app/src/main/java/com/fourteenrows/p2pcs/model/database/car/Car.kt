package com.fourteenrows.p2pcs.model.database.car

import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.objects.cars.FetchedVehicle
import com.fourteenrows.p2pcs.objects.cars.Vehicle
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Car(private val database: FirebaseFirestore, private val authorizer: FirebaseAuth) {

    fun checkNewPlate(plate: String) = database
        .collection("Cars")
        .whereEqualTo("plate", plate)
        .get()

    fun editVehicle(car: FetchedVehicle): Task<Void> {
        val toInsert = Vehicle(
            car.final_availability,
            car.model,
            car.owner,
            car.plate,
            car.seats,
            car.instant_availability,
            car.immatricolazione,
            car.location,
            car.applicant,
            car.geopoint
        )
        return database.collection("Cars").document(car.cid).set(toInsert)
    }

    fun fetchBusyVehicleOf(date: Long, timeSlot: String) = database
        .collection("Dates")
        .whereEqualTo("start_slot", ModelDates.setSlotToDate(Date(date), timeSlot))
        .get()

    fun fetchAvailableVehicles(date: Date) = database
        .collection("Cars")
        .whereEqualTo("instant_availability", true)
        .whereGreaterThan("final_availability", date)
        .get()


    fun getUserVehicles() = database
        .collection("Cars")
        .whereEqualTo("owner", authorizer.uid)
        .get()


    fun insertVehicle(car: Vehicle) = database
        .collection("Cars")
        .add(car)


    fun getCarActiveReservations(cid: String) = database
        .collection("Dates")
        .whereEqualTo("car", cid)
        .get()

    fun getCarReservations(carId: String) = database
        .collection("Dates")
        .whereEqualTo("Cars", carId)
        .get()

    fun getCar(cid: String) = database
        .collection("Cars")
        .document(cid)
        .get()

}