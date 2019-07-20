package com.fourteenrows.p2pcs.activities.general_activity

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface IGeneralView {
    fun updateToolbar(): Task<DocumentSnapshot>
    fun updateValues()
    fun stopProgressDialog()
    fun startProgressDialog()
    fun makeAlertDialog(message: Int, title: Int, toLogin: Boolean = false)
    fun initializeDrawer()
    fun makeRewardDialog(
        gaiaCoins: Long,
        exp: Long,
        weekPoints: Long,
        itemName: String,
        imageUrl: String,
        instance: GeneralActivityPresenter
    )
}
