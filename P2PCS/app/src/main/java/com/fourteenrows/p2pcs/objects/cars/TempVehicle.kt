package com.fourteenrows.p2pcs.objects.cars

import java.io.Serializable

open class TempVehicle(
    var plate: String,
    var model: String,
    var seats: String,
    var location: String,
    var endDate: String,
    var immatricolazione: String,
    var geoposizione: String
) : Serializable