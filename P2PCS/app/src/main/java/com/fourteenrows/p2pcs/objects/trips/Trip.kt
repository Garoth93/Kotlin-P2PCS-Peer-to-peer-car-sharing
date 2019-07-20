package com.fourteenrows.p2pcs.objects.trips

import java.io.Serializable

open class Trip(
    open var start: String = "",
    open var end: String = "",
    open var participants: Long = 0,
    open val distance_string: String = "",
    open val distance_value: Long = 0,
    open val duration_text: String = "",
    open val duration_value: Long = 0,
    open val deleted: Boolean = false,
    open val date: String = "",
    open val tid: String = "",
    open var cid: String = ""
) : Serializable