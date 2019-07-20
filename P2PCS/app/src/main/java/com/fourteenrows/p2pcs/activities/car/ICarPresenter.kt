package com.fourteenrows.p2pcs.activities.car

import java.util.*

interface ICarPresenter {
    fun deleteCar()
    fun previousVehicle()
    fun nextVehicle()
    fun formatDate(value: Date): String
    fun nextNDays(date: Int): Long
    fun updateCarField(cid: String, field: String, input: Any)
    fun checkImmatricolazione(input: String)
    fun checkPlate(input: String)
    fun checkLocation(input: String)
    fun checkSeats(input: String)
    fun checkFinalAvailability(input: Date)
    fun checkInstantAvailability(input: Boolean)
    fun getCid(): String
    fun checkModel(input: String)
    fun getIndex(): Int
}
