package com.fourteenrows.p2pcs.model.database.avatar

import com.fourteenrows.p2pcs.objects.items.ItemAvatar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Avatar(private val database: FirebaseFirestore, private val auth: FirebaseAuth) {

    fun getOwnedAvatarParts() =
        database
            .collection("User")
            .document(auth.uid!!)
            .collection("Avatar")
            .get()

    fun fetchAvatarPieces() =
        database
            .collection("Avatar")
            .get()

    fun addOwnershipAvatar(iid: String, itemAvatarToDB: ItemAvatar) {
        database
            .collection("User")
            .document(auth.uid!!)
            .collection("Avatar")
            .document(iid)
            .set(itemAvatarToDB)
    }
}