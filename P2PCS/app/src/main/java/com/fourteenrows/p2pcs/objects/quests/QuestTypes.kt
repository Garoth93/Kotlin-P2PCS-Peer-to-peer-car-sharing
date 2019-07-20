package com.fourteenrows.p2pcs.objects.quests

enum class QuestTypes {
    trip,
    totalTrip,
    totalKm,
    km,
    person,
    totalPerson,
    totalReservation,
    none;

    companion object {
        fun of(x: Int): QuestTypes {
            return when (x) {
                0 -> trip
                1 -> totalTrip
                2 -> totalKm
                3 -> km
                4 -> person
                5 -> totalPerson
                6 -> totalReservation
                else -> throw Exception("Can't parse passed number")
            }
        }
    }
}