package com.fourteenrows.p2pcs.model.database.shop

import com.google.firebase.firestore.FirebaseFirestore

class Shop(private val database: FirebaseFirestore) {

    fun fetchItemShop() = database
        .collection("Items")
        .get()


}