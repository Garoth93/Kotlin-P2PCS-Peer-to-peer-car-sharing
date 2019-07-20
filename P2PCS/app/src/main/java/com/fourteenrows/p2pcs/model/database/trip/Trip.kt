package com.fourteenrows.p2pcs.model.database.trip

import com.fourteenrows.p2pcs.objects.trips.ToDatabaseTrip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Trip(private val database: FirebaseFirestore, private val authorizer: FirebaseAuth) {

    fun addTripData(trip: ToDatabaseTrip) = database
        .collection("User")
        .document(authorizer.uid!!)
        .collection("Trips")
        .add(trip)

    fun getTrips() = database
        .collection("User")
        .document(authorizer.uid!!)
        .collection("Trips")
        .get()
}