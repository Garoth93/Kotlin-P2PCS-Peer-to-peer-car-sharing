package com.fourteenrows.p2pcs.model.database.booster

import com.fourteenrows.p2pcs.objects.boosters.ActiveBoosterToDB
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Booster(private val database: FirebaseFirestore, private val authorizer: FirebaseAuth) {

    fun addOwnershipBooster(iid: String, itemBoosterToDB: ActiveBoosterToDB) =
        database
            .collection("User")
            .document(authorizer.uid!!)
            .collection("Boosters")
            .document(iid)
            .set(itemBoosterToDB)

    fun activateBooster(bid: String, quantity: Long): Task<Void> {
        //active booster
        database
            .collection("User")
            .document(authorizer.uid!!)
            .collection("Boosters")
            .document(bid)
            .update("active", true)
        //set new quantity
        return database
            .collection("User")
            .document(authorizer.uid!!)
            .collection("Boosters")
            .document(bid)
            .update("quantity", quantity)
    }

    fun deactivateBooster(bid: String) = database
        .collection("User")
        .document(authorizer.uid!!)
        .collection("Boosters")
        .document(bid)
        .update("active", false)

    fun getUserBoosters() = database
        .collection("User")
        .document(authorizer.uid!!)
        .collection("Boosters")
        .get()

    fun getUserBoostersWithBID(bid: String) =
        database
            .collection("User")
            .document(authorizer.uid!!)
            .collection("Boosters")
            .document(bid)
            .get()

    fun removeUserBooster(bid: String) = database
        .collection("User")
        .document(authorizer.uid!!)
        .collection("Boosters")
        .document(bid)
        .delete()

    fun getBoosters() =
        database
            .collection("Boosters")
            .get()


    fun updateBoosterQuantity(bid: String, quantity: Long) =
        database
            .collection("User")
            .document(authorizer.uid!!)
            .collection("Boosters")
            .document(bid)
            .update("quantity", quantity)

    fun getActiveBoosters() =
        database
            .collection("User")
            .document(authorizer.uid!!)
            .collection("Boosters")
            .whereEqualTo("active", true)
            .get()


}