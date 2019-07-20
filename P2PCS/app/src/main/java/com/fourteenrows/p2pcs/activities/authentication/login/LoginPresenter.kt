package com.fourteenrows.p2pcs.activities.authentication.login

import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelValidator

class LoginPresenter(toView: ILoginView, private val database: ModelDatabase = ModelFirebase()) :
    ILoginPresenter {
    private var view = toView

    override fun checkLoginValues(email: String, pwd: String) {
        if (!ModelValidator.checkValueIsEmpty(email) || !ModelValidator.checkValueIsEmpty(pwd)) {
            view.makeAlertDialog(R.string.all_fields_required, R.string.error)
            return
        }
        if (!ModelValidator.checkValueIsEmail(email)) {
            view.makeAlertDialog(R.string.email_not_email, R.string.error)
            return
        }
        authenticateUser(email, pwd)
    }

    override fun checkResetValues(email: String) {
        if (!ModelValidator.checkValueIsEmpty(email)) {
            view.makeAlertDialog(R.string.field_required, R.string.error)
            return
        }

        if (!ModelValidator.checkValueIsEmail(email)) {
            view.makeAlertDialog(R.string.email_not_email, R.string.error)
            return
        }

        database.isEmailRegistered(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.size() == 0) {
                        view.makeAlertDialog(R.string.not_registered, R.string.error)
                    } else {
                        sendResetEmail(email)
                    }
                }
            }
    }


    override fun onSuccess() {
        view.updateToMainView()
    }

    override fun onFailure(message: Int, title: Int) {
        view.makeAlertDialog(message, title)
    }

    override fun authenticateUser(email: String, pwd: String) {
        database.authenticateUser(email, pwd)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (database.isEmailVerified()) {
                        onSuccess()
                    } else {
                        database.sendVerificationEmail()
                        database.signOut()
                        onFailure(R.string.not_verified, R.string.error)
                    }
                } else {
                    onFailure(R.string.wrong_credentials, R.string.error)
                }
            }
    }

    override fun sendResetEmail(email: String) {
        database.sendResetEmail(email)
        view.makeAlertDialog(R.string.reset_email_sent, R.string.success)
    }
}