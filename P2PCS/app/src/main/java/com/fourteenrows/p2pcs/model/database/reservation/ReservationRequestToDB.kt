package com.fourteenrows.p2pcs.model.database.reservation

import java.util.*

class ReservationRequestToDB(
    val model: String,
    val username: String,
    val startDate: Date,
    val endDate: Date,
    val applicant: String
)