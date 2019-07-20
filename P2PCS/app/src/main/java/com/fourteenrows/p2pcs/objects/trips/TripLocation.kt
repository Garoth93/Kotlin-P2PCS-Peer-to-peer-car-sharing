package com.fourteenrows.p2pcs.objects.trips

import java.io.Serializable

data class TripLocation(
    val main: String,
    val secondary: String,
    val type: String
) : Serializable