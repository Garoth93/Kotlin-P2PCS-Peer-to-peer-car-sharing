package com.fourteenrows.p2pcs.objects.trips

class TripErrorRecycler(
    override var start: String = "",
    override var end: String = "",
    override var participants: Long = 0,
    override val distance_string: String = "",
    override val distance_value: Long = 0,
    override val duration_text: String = "",
    override val duration_value: Long = 0,
    override val deleted: Boolean = false,
    override val tid: String = "",
    val message: String
) : Trip(start, end, participants, distance_string, distance_value, duration_text, duration_value, deleted, tid)