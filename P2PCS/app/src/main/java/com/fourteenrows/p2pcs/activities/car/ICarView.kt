package com.fourteenrows.p2pcs.activities.car

import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView
import com.fourteenrows.p2pcs.objects.cars.FetchedVehicle

interface ICarView : IGeneralView {
    fun showErrorCanNotEdit()
    fun loadVehicle(vehicle: FetchedVehicle)
    fun makeEditTextDialog(hint: Int, title: Int, type: Int, positive: Int, placeholder: String, field: String)
    fun carPresents()
    fun showErrorCanNotDelete()
}