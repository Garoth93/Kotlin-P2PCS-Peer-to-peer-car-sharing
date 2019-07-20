package com.fourteenrows.p2pcs.activities.authentication.login

interface ILoginPresenter {
    fun authenticateUser(email: String, pwd: String)
    fun checkLoginValues(email: String, pwd: String)
    fun checkResetValues(email: String)
    fun onFailure(message: Int, title: Int)
    fun onSuccess()
    fun sendResetEmail(email: String)
}
