package com.fourteenrows.p2pcs.objects.cars

import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.util.*

data class FetchedVehicle(
    override var final_availability: Date?,
    override var model: String,
    override var owner: String,
    override var plate: String,
    override var seats: Long,
    override var instant_availability: Boolean,
    override val immatricolazione: String,
    override val location: String,
    var cid: String,
    var editable: Boolean = true,
    override val applicant: String,
    override val geopoint: GeoPoint
) : Vehicle(
    final_availability,
    model,
    owner,
    plate,
    seats,
    instant_availability,
    immatricolazione,
    location,
    applicant,
    geopoint
),
    Serializable {
}