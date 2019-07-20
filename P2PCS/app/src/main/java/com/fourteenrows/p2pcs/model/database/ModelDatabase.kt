package com.fourteenrows.p2pcs.model.database

import com.fourteenrows.p2pcs.model.database.reservation.ReservationRequestToDB
import com.fourteenrows.p2pcs.objects.boosters.ActiveBoosterToDB
import com.fourteenrows.p2pcs.objects.cars.FetchedVehicle
import com.fourteenrows.p2pcs.objects.cars.Vehicle
import com.fourteenrows.p2pcs.objects.items.ItemAvatar
import com.fourteenrows.p2pcs.objects.reservations.Reservation
import com.fourteenrows.p2pcs.objects.trips.ToDatabaseTrip
import com.fourteenrows.p2pcs.objects.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

interface ModelDatabase {

    fun activateBooster(bid: String, quantity: Long): Task<Void>

    fun addEmailData(email: String): Task<Void>

    fun addUserData(uid: String?, name: String, surname: String, email: String): Task<Void>

    fun authenticateUser(email: String, pwd: String): Task<AuthResult>

    fun buildUser(it: DocumentSnapshot): User

    fun changeQuest(newQuest: String, qid: String): Task<Void>

    fun checkNewPlate(plate: String): Task<QuerySnapshot>

    fun deactivateBooster(bid: String): Task<Void>

    fun delete(collection: String, document: String): Task<Void>

    fun editVehicle(car: FetchedVehicle): Task<Void>

    fun fetchBusyVehicleOf(date: Long, timeSlot: String): Task<QuerySnapshot>

    fun fetchAvailableVehicles(date: Date): Task<QuerySnapshot>

    fun fetchItemShop(): Task<QuerySnapshot>

    fun fetchUsers(email: String): Task<QuerySnapshot>

    fun getActiveQuests(): Task<QuerySnapshot>

    fun getCarReservations(carId: String): Task<QuerySnapshot>

    fun getCurrentUser(): FirebaseUser?

    fun getQuest(quest: String): Task<DocumentSnapshot>

    fun getReservation(rid: String): Task<DocumentSnapshot>

    fun getTrips(): Task<QuerySnapshot>

    fun getUid(): String?

    fun getUserBoosters(): Task<QuerySnapshot>

    fun getUserBoostersWithBID(bid: String): Task<DocumentSnapshot>

    fun getUserDocument(): Task<DocumentSnapshot>

    fun getUserVehicles(): Task<QuerySnapshot>

    fun insert(collection: String, document: String = "", obj: Any): Task<Void>

    fun insertQuest(newQuest: String, uid: String?): Task<Void>

    fun insertReservation(reservation: Reservation): Task<Void>

    fun insertVehicle(car: Vehicle): Task<DocumentReference>

    fun insertUser(email: String, pwd: String): Task<AuthResult>

    fun isEmailRegistered(email: String): Task<QuerySnapshot>

    fun isEmailVerified(): Boolean

    fun removeUserBooster(bid: String): Task<Void>

    fun sendResetEmail(email: String)

    fun sendResetEmailKnown(): Task<Void>

    fun sendVerificationEmail()

    fun signOut()

    fun updateField(field: String, value: Any): Task<Void>

    fun updateLongUserField(field: String, value: Long): Task<Void>

    fun getOwnedAvatarParts(): Task<QuerySnapshot>
    fun fetchAvatarPieces(): Task<QuerySnapshot>

    fun getBoosters(): Task<QuerySnapshot>
    fun addOwnershipAvatar(iid: String, itemAvatarToDB: ItemAvatar)
    fun addOwnershipBooster(iid: String, itemBoosterToDB: ActiveBoosterToDB): Task<Void>
    fun updateBoosterQuantity(bid: String, quantity: Long): Task<Void>

    fun updateStringUserField(field: String, value: String): Task<Void>
    fun getUidFromEmail(userEMail: String): Task<QuerySnapshot>
    fun getActiveBoosters(): Task<QuerySnapshot>
    fun addUserPoints(exp: Long, gaiaCoins: Long, weekPoints: Long)
    fun addTripData(trip: ToDatabaseTrip): Task<DocumentReference>
    fun getActivePrenotation(): Task<QuerySnapshot>
    fun getCarActiveReservations(cid: String): Task<QuerySnapshot>

    fun updateProgressQuest(qid: String, progress: Long): Task<Void>
    fun resetLastFreeChangeQuest(): Task<Void>
    fun getCar(cid: String): Task<DocumentSnapshot>
    fun pastChangeQuest(): Task<Void>
    fun getBox(box: String): Task<DocumentSnapshot>
    fun getAvatarItem(item: String): Task<DocumentSnapshot>
    fun getBooster(booster: String): Task<DocumentSnapshot>
    fun fetchLeaderboard(): Task<QuerySnapshot>
    fun deleteReservation(rid: String): Task<Void>
    fun hideTrip(tid: String): Task<Void>
    fun updateCarField(cid: String, field: String, input: Any): Task<Void>
    fun getOwner(owner: String): Task<DocumentSnapshot>
    fun addReservationRequest(carId: String, reservationRequest: ReservationRequestToDB): Task<Void>
    fun accept(rid: String): Task<Void>
    fun reject(rid: String): Task<Void>
    fun getUserRequests(): Task<QuerySnapshot>
    fun removeRequest(id: String): Task<Void>
    fun getReservationOf(date: Date): Task<QuerySnapshot>
    fun getCars(): Task<QuerySnapshot>
}