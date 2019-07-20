package com.fourteenrows.p2pcs.objects.boosters

import java.util.stream.Collectors

class ActiveBoosterContainer : Iterator<ActiveBooster> {
    override fun hasNext(): Boolean = list.size > index + 1

    override fun next(): ActiveBooster = list[++index]

    private var list: ArrayList<ActiveBooster> = ArrayList<ActiveBooster>()
    private val forTrip: Array<Long> = arrayOf(0L, 1L, 2L, 3L, 4L)
    private var index = 0

    fun size() = list.size

    fun add(ab: ActiveBooster) {
        list.add(ab)
    }

    fun keepOnlyBoosterTripOnly() {
        list.removeIf {
            it.category in forTrip
        }
    }

    fun getSpentBoosters() = list
        .stream()
        .filter {
            it.quantity == 0L
        }
        .collect(Collectors.toList())

}