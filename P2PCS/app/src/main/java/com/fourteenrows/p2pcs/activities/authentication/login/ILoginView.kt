package com.fourteenrows.p2pcs.activities.authentication.login

import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView

interface ILoginView : IGeneralView {
    fun makeEditTextDialog()
    fun updateToMainView()
}