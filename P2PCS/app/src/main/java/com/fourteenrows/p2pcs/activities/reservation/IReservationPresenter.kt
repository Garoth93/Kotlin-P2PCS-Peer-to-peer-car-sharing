package com.fourteenrows.p2pcs.activities.reservation

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface IReservationPresenter {
    fun fetchReservations()
    fun getReservation(rid: String): Task<DocumentSnapshot>
    fun deleteReservation(rid: String)
    fun hideReservation(rid: String)
}