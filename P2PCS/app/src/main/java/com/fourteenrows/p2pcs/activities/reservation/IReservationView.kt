package com.fourteenrows.p2pcs.activities.reservation

interface IReservationView {
    fun makeAlertDialogDismiss(message: Int, title: Int)
    fun makeConfirmationDialog(rid: String)
    fun resetView()
    fun setRecyclerAdapter(adapter: ReservationRecyclerAdapter)
    fun makeConfirmationArchivedDialog(rid: String)
}