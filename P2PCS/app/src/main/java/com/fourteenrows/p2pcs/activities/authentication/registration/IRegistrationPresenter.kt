package com.fourteenrows.p2pcs.activities.authentication.registration

interface IRegistrationPresenter {
    fun checkLoginValues(email: String, name: String, surname: String, pwd: String, pwd2: String)
    fun checkResetValues(email: String)
    fun insertUser(email: String, name: String, surname: String, pwd: String)
    fun sendResetEmail(email: String)
    fun onFailure()
    fun onSuccess()
}
