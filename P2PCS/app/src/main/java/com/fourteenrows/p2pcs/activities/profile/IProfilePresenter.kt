package com.fourteenrows.p2pcs.activities.profile

import com.fourteenrows.p2pcs.objects.user.User

interface IProfilePresenter {
    fun loadUserData()
    fun sendReset()
    fun updateData(field: String, input: String)
    fun loadUser()
    fun sendResetEmailKnown()
    fun getSuccessful(user: User)
    fun onSuccess(message: Int, title: Int)
    fun getUid(): String
}