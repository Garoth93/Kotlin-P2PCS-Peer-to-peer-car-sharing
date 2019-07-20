package com.fourteenrows.p2pcs.model.database.user

import com.fourteenrows.p2pcs.objects.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class User(
    private val database: FirebaseFirestore,
    private val authorizer: FirebaseAuth
) {

    fun addUserData(uid: String?, name: String, surname: String, email: String): Task<Void> {
        val user = User(name, surname, email, 0, 0, 0, Date(0), Date(0))
        return database.collection("User").document(uid!!).set(user)
    }

    fun getCurrentUser() = authorizer.currentUser

    fun fetchUsers(email: String) = database
        .collection("User")
        .get()

    fun buildUser(it: DocumentSnapshot) = User(
        it.data!!["name"]!!.toString(),
        it.data!!["surname"]!!.toString(),
        it.data!!["mail"]!!.toString(),
        it.data!!["exp"] as Long,
        it.data!!["gaia_coins"] as Long,
        it.data!!["week_points"] as Long,
        (it.data!!["last_free_change_quest"] as Timestamp).toDate(),
        (it.data!!["last_daily_new_quest"] as Timestamp).toDate()
    )

    fun updateStringUserField(field: String, value: String) = database
        .collection("User")
        .document(getUid()!!)
        .update(field, value)

    fun getUidFromEmail(userEMail: String) =
        database
            .collection("User")
            .whereEqualTo("mail", userEMail)
            .get()

    fun addUserPoints(exp: Long, gaiaCoins: Long, weekPoints: Long) {
        val docRef = database
            .collection("User")
            .document(getUid()!!)
        docRef.update("exp", exp)
        docRef.update("gaia_coins", gaiaCoins)
        docRef.update("week_points", weekPoints)

    }

    fun updateLongUserField(field: String, value: Long) = database
        .collection("User")
        .document(getUid()!!)
        .update(field, value)


    fun getUid() = authorizer.uid


    fun getUserDocument() = database
        .collection("User")
        .document(getUid()!!)
        .get()


    fun updateField(field: String, value: Any) = database
        .collection("User")
        .document(getUid()!!)
        .update(field, value)

    fun getOwner(owner: String) = database
        .collection("User")
        .document(owner)
        .get()

}