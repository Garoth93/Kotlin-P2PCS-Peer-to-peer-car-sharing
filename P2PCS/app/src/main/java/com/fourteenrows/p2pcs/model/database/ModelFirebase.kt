package com.fourteenrows.p2pcs.model.database

import com.fourteenrows.p2pcs.model.database.authentication.Authentication
import com.fourteenrows.p2pcs.model.database.avatar.Avatar
import com.fourteenrows.p2pcs.model.database.booster.Booster
import com.fourteenrows.p2pcs.model.database.car.Car
import com.fourteenrows.p2pcs.model.database.quest.Quest
import com.fourteenrows.p2pcs.model.database.reservation.Reservation
import com.fourteenrows.p2pcs.model.database.reservation.ReservationRequestToDB
import com.fourteenrows.p2pcs.model.database.shop.Shop
import com.fourteenrows.p2pcs.model.database.trip.Trip
import com.fourteenrows.p2pcs.model.database.user.User
import com.fourteenrows.p2pcs.model.database.utils.Utils
import com.fourteenrows.p2pcs.objects.boosters.ActiveBoosterToDB
import com.fourteenrows.p2pcs.objects.cars.FetchedVehicle
import com.fourteenrows.p2pcs.objects.cars.Vehicle
import com.fourteenrows.p2pcs.objects.items.ItemAvatar
import com.fourteenrows.p2pcs.objects.trips.ToDatabaseTrip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class ModelFirebase : ModelDatabase {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val authentication = Authentication(firestore, auth)
    private val avatar = Avatar(firestore, auth)
    private val booster = Booster(firestore, auth)
    private val car = Car(firestore, auth)
    private val quest = Quest(firestore, auth)
    private val reservation = Reservation(firestore, auth)
    private val shop = Shop(firestore)
    private val trip = Trip(firestore, auth)
    private val user = User(firestore, auth)
    private val utils = Utils(firestore, auth)

    override fun activateBooster(bid: String, quantity: Long) = booster.activateBooster(bid, quantity)

    override fun addEmailData(email: String) = authentication.addEmailData(email)

    override fun addUserData(uid: String?, name: String, surname: String, email: String) =
        user.addUserData(uid, name, surname, email)

    override fun authenticateUser(email: String, pwd: String) = authentication.authenticateUser(email, pwd)

    override fun buildUser(it: DocumentSnapshot) = user.buildUser(it)

    override fun changeQuest(newQuest: String, qid: String) = quest.changeQuest(newQuest, qid)

    override fun checkNewPlate(plate: String) = car.checkNewPlate(plate)

    override fun deactivateBooster(bid: String) = booster.deactivateBooster(bid)

    override fun delete(collection: String, document: String) = utils.delete(collection, document)

    override fun editVehicle(car: FetchedVehicle) = this.car.editVehicle(car)

    override fun fetchBusyVehicleOf(date: Long, timeSlot: String) = car.fetchBusyVehicleOf(date, timeSlot)

    override fun fetchAvailableVehicles(date: Date) = car.fetchAvailableVehicles(date)

    override fun fetchItemShop() = shop.fetchItemShop()

    override fun fetchUsers(email: String) = user.fetchUsers(email)

    override fun getActiveQuests() = quest.getActiveQuests()

    override fun getCarReservations(carId: String) = car.getCarReservations(carId)

    override fun getCurrentUser() = user.getCurrentUser()

    override fun getQuest(quest: String) = this.quest.getQuest(quest)

    override fun getReservation(rid: String) = reservation.getReservation(rid)

    override fun getTrips() = trip.getTrips()

    override fun getUid() = user.getUid()

    override fun getUserBoosters() = booster.getUserBoosters()

    override fun getUserBoostersWithBID(bid: String) = booster.getUserBoostersWithBID(bid)

    override fun getUserDocument() = user.getUserDocument()

    override fun getUserVehicles() = car.getUserVehicles()

    override fun insert(collection: String, document: String, obj: Any) = utils.insert(collection, document, obj)

    override fun insertQuest(newQuest: String, uid: String?) = quest.insertQuest(newQuest, uid)

    override fun insertReservation(reservation: com.fourteenrows.p2pcs.objects.reservations.Reservation) =
        this.reservation.insertReservation(reservation)

    override fun insertVehicle(car: Vehicle) = this.car.insertVehicle(car)

    override fun insertUser(email: String, pwd: String) = authentication.insertUser(email, pwd)

    override fun isEmailRegistered(email: String) = authentication.isEmailRegistered(email)

    override fun isEmailVerified() = authentication.isEmailVerified()

    override fun removeUserBooster(bid: String) = booster.removeUserBooster(bid)

    override fun sendResetEmail(email: String) = authentication.sendResetEmail(email)

    override fun sendResetEmailKnown() = authentication.sendResetEmailKnown()

    override fun sendVerificationEmail() = authentication.sendVerificationEmail()

    override fun signOut() = authentication.signOut()

    override fun updateField(field: String, value: Any) = user.updateField(field, value)

    override fun updateLongUserField(field: String, value: Long) = user.updateLongUserField(field, value)

    override fun getOwnedAvatarParts() = avatar.getOwnedAvatarParts()

    override fun fetchAvatarPieces() = avatar.fetchAvatarPieces()

    override fun getBoosters() = booster.getBoosters()

    override fun addOwnershipAvatar(iid: String, itemAvatarToDB: ItemAvatar) =
        avatar.addOwnershipAvatar(iid, itemAvatarToDB)

    override fun addOwnershipBooster(iid: String, itemBoosterToDB: ActiveBoosterToDB) =
        booster.addOwnershipBooster(iid, itemBoosterToDB)

    override fun updateBoosterQuantity(bid: String, quantity: Long) = booster.updateBoosterQuantity(bid, quantity)

    override fun updateStringUserField(field: String, value: String) = user.updateStringUserField(field, value)

    override fun getUidFromEmail(userEMail: String) = user.getUidFromEmail(userEMail)

    override fun getActiveBoosters() = booster.getActiveBoosters()

    override fun addUserPoints(exp: Long, gaiaCoins: Long, weekPoints: Long) =
        user.addUserPoints(exp, gaiaCoins, weekPoints)

    override fun addTripData(trip: ToDatabaseTrip) = this.trip.addTripData(trip)

    override fun getActivePrenotation() = reservation.getActivePrenotation()

    override fun getCarActiveReservations(cid: String) = car.getCarActiveReservations(cid)

    override fun updateProgressQuest(qid: String, progress: Long) = quest.updateProgressQuest(qid, progress)

    override fun resetLastFreeChangeQuest() = quest.resetLastFreeChangeQuest()

    override fun pastChangeQuest() = quest.pastChangeQuest()

    override fun getCar(cid: String) = car.getCar(cid)


    fun removeQuest(qid: String) = firestore
        .collection("User")
        .document(user.getUid()!!)
        .collection("ActiveQuests")
        .document(qid)
        .delete()

    override fun getBox(box: String) = firestore
        .collection("Box")
        .document(box)
        .get()

    override fun getAvatarItem(item: String) = firestore
        .collection("Avatar")
        .document(item)
        .get()

    override fun getBooster(booster: String) = firestore
        .collection("Boosters")
        .document(booster)
        .get()

    override fun fetchLeaderboard() = firestore
        .collection("User")
        .orderBy("week_points", Query.Direction.DESCENDING)
        .get()

    override fun deleteReservation(rid: String) = firestore
        .collection("Dates")
        .document(rid)
        .update("deleted", true)

    override fun hideTrip(tid: String) = firestore
        .collection("User")
        .document(getUid()!!)
        .collection("Trips")
        .document(tid)
        .update("deleted", true)

    override fun updateCarField(cid: String, field: String, input: Any) = firestore
        .collection("Cars")
        .document(cid)
        .update(field, input)

    override fun getOwner(owner: String) = user.getOwner(owner)

    override fun addReservationRequest(carId: String, reservationRequest: ReservationRequestToDB) = firestore
        .collection("Requests")
        .document(carId)
        .set(reservationRequest)

    override fun accept(rid: String) = firestore
        .collection("Requests")
        .document(rid)
        .update("accepted", true)

    override fun reject(rid: String) = firestore
        .collection("Requests")
        .document(rid)
        .update("accepted", false)

    override fun getUserRequests() = firestore
        .collection("Requests")
        .whereEqualTo("applicant", getUid()!!)
        .get()

    override fun removeRequest(id: String) = firestore
        .collection("Requests")
        .document(id)
        .delete()

    override fun getReservationOf(date: Date) = firestore.collection("Dates")
        .whereGreaterThanOrEqualTo("start_slot", date)
        .get()

    override fun getCars() = firestore
        .collection("Cars")
        .get()
}