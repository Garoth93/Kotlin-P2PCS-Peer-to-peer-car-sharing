package com.fourteenrows.p2pcs.activities.authentication.registration

import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.fourteenrows.p2pcs.objects.boosters.ActiveBoosterToDB
import com.fourteenrows.p2pcs.objects.boosters.BoosterTypes
import com.fourteenrows.p2pcs.objects.quests.QuestAuthorizing

class RegistrationPresenter(toView: RegistrationActivity, private val database: ModelDatabase = ModelFirebase()) :
    IRegistrationPresenter {

    private var view = toView

    override fun checkLoginValues(email: String, name: String, surname: String, pwd: String, pwd2: String) {
        arrayOf(email, name, surname, pwd, pwd2)
            .forEach {
                if (it.isEmpty()) {
                    view.makeAlertDialog(R.string.all_fields_required, R.string.error)
                    return
                }
            }

        if (!ModelValidator.checkValueIsEmail(email)) {
            view.makeAlertDialog(R.string.email_not_email, R.string.error)
            return
        }

        if (!ModelValidator.checkStringLength(pwd, 6)) {
            view.makeAlertDialog(R.string.password_length, R.string.error)
            return
        }

        if (!ModelValidator.checkStringsEqual(pwd, pwd2)) {
            view.makeAlertDialog(R.string.passwords_not_matching, R.string.error)
            return
        }

        insertUser(email, name, surname, pwd)
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
        view.stopProgressDialog()
        view.changeView(R.string.registration_successful, R.string.success)
    }

    override fun onFailure() {
        view.stopProgressDialog()
        view.makeAlertDialog(R.string.email_already_in, R.string.error)
    }

    override fun insertUser(email: String, name: String, surname: String, pwd: String) {
        view.startProgressDialog()
        database.isEmailRegistered(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.size() == 0) {
                        database.insertUser(email, pwd)
                            .addOnSuccessListener {
                                addUserData(database.getUid()!!, name, surname, email)
                            }
                    } else {
                        onFailure()
                    }
                }
            }
    }

    private fun addEmailData(email: String) {
        database.addEmailData(email)
            .addOnSuccessListener {
                onSuccess()
            }
    }

    private fun addUserData(uid: String, name: String, surname: String, email: String) {
        database.addUserData(uid, name, surname, email)
            .addOnSuccessListener {
                database.sendVerificationEmail()
                val chosen = arrayListOf<String>()
                val questAuthorizing = QuestAuthorizing(1L)
                for (i in 0..3) {
                    val quest = questAuthorizing.getQuestForWithStrings(chosen)
                    chosen.add(quest)
                    database.insertQuest(quest, uid)
                }
                database.addOwnershipBooster(
                    uid, ActiveBoosterToDB(
                        "Beginner's Luck",
                        BoosterTypes.mAll.ordinal.toLong(), 3, "Booster di benvenuto", true, 1
                    )
                )
                addEmailData(email)
            }
    }

    override fun sendResetEmail(email: String) {
        database.sendResetEmail(email)
    }
}