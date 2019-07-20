package com.fourteenrows.p2pcs.objects.cars

import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.util.*

open class Vehicle(
    open val final_availability: Date?,
    open val model: String,
    open val owner: String,
    open val plate: String,
    open val seats: Long,
    open val instant_availability: Boolean,
    open val immatricolazione: String,
    open val location: String,
    open val applicant: String,
    open val geopoint: GeoPoint
) : Serializable