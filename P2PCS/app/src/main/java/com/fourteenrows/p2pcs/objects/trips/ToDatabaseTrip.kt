package com.fourteenrows.p2pcs.objects.trips

import java.io.Serializable

data class ToDatabaseTrip(
    val distance_text: String,
    val distance_value: Long,
    val duration_text: String,
    val duration_value: Long,
    val end: String,
    val participants: Long,
    val start: String,
    val deleted: Boolean,
    val date: String,
    val cid: String
) : Serializable
