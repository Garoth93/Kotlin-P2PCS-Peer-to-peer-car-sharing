package com.fourteenrows.p2pcs.activities.car

import com.fourteenrows.p2pcs.objects.cars.FetchedVehicle

class Cars {
    private val cars = ArrayList<FetchedVehicle>()
    var index = 0

    fun clear() {
        cars.clear()
        index = 0
    }

    fun add(fv: FetchedVehicle) = cars.add(fv)

    fun get(): FetchedVehicle? {
        if (cars.size > 0)
            return cars[index]
        return null
    }

    fun size() = cars.size

    fun setFirst() {
        index = 0
    }

    fun prev(): FetchedVehicle {
        if (--index < 0)
            index = cars.size - 1
        return cars[index]
    }

    fun next(): FetchedVehicle {
        if (++index == cars.size)
            index = 0
        return cars[index]
    }

    fun update(fetchedVehicle: FetchedVehicle) {
        cars[index] = fetchedVehicle
    }

}