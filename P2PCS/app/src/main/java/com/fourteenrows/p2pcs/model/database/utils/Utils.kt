package com.fourteenrows.p2pcs.model.database.utils

import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Utils(private val database: FirebaseFirestore, private val authorizer: FirebaseAuth) {

    fun delete(collection: String, document: String) = database
        .collection(collection)
        .document(document)
        .delete()

    fun insert(collection: String, document: String, obj: Any) =
        if (!ModelValidator.checkValueIsEmpty(document)) {
            database.collection(collection).document().set(obj)
        } else {
            database.collection(collection).document(document).set(obj)
        }
}