package com.fourteenrows.p2pcs.activities.trip

import android.content.Intent
import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView

interface ITripView : IGeneralView {
    fun gotoAdd(intent: Intent)
    fun showErrorNoReservationActive()
    fun makeConfirmationDialog(tid: String)
    fun setRecyclerAdapter(adapter: TripRecyclerAdapter)
}