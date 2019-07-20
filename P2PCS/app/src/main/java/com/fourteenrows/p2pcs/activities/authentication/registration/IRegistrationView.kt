package com.fourteenrows.p2pcs.activities.authentication.registration

import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView

interface IRegistrationView : IGeneralView {
    fun makeEditTextDialog()
    fun updateViewData()
    fun changeView(message: Int, title: Int)
}