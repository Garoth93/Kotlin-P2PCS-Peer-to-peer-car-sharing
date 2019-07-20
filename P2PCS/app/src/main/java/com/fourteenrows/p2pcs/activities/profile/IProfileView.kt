package com.fourteenrows.p2pcs.activities.profile

import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView
import com.fourteenrows.p2pcs.objects.user.User

interface IProfileView : IGeneralView {
    fun makeEditTextDialog(
        hint: Int,
        title: Int,
        type: Int,
        positive: Int,
        placeholder: String,
        field: String
    )

    fun replaceData(user: User)
    fun refresh()
}