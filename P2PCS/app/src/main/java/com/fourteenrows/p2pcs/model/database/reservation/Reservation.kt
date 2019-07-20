package com.fourteenrows.p2pcs.model.database.reservation

import com.fourteenrows.p2pcs.objects.reservations.Reservation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class Reservation(private val database: FirebaseFirestore, private val authorizer: FirebaseAuth) {

    fun getActivePrenotation(): Task<QuerySnapshot> {
        val date = Date()
        return database
            .collection("Dates")
            .whereEqualTo("owner", authorizer.uid!!)
            .whereGreaterThan("end_slot", date)
            .whereLessThan("start_slot", date)
            .get()
    }

    fun getReservation(rid: String) = database
        .collection("Dates")
        .document(rid)
        .get()


    fun insertReservation(reservation: Reservation) = database
        .collection("Dates")
        .document()
        .set(reservation)
}