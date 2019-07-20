package com.fourteenrows.p2pcs.activities.car.add_car

import android.net.Uri
import com.fourteenrows.p2pcs.objects.cars.Vehicle
import java.util.*

interface IAddCarPresenter {
    fun checkPlateAlreadyExists(car: Vehicle)
    fun formatDate(value: Date): String
    fun nextNDays(date: Int): Long
    fun onSuccess(car: Vehicle)
    fun onFailure()
    fun checkVehicleValues(
        plate: String,
        model: String,
        seats: String,
        location: String,
        endDate: String,
        immatricolazione: String
    )
    fun setImageUri(imageUri: Uri)
}