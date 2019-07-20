package com.fourteenrows.p2pcs.activities.profile

import android.app.Activity
import android.content.SharedPreferences
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.fourteenrows.p2pcs.objects.user.User
import java.util.*

class ProfilePresenter(toView: IProfileView, private val database: ModelDatabase = ModelFirebase()) :
    IProfilePresenter {

    private val view = toView

    init {
        loadUserData()
    }

    //TODO("MOVE CACHE AWAY")

    override fun loadUserData() {
        val activity = view as Activity
        val cache: SharedPreferences = activity.getSharedPreferences("userData", 0)
        if (!cache.contains("mail")) {
            loadUser()
        } else {
            val user = User(
                cache.getString("name", "")!!,
                cache.getString("surname", "")!!,
                cache.getString("mail", "")!!,
                cache.getLong("exp", 0),
                cache.getLong("gaia_coins", 0),
                cache.getLong("week_points", 0),
                Date(cache.getLong("last_free_change_quest", 0)),
                Date(cache.getLong("last_daily_new_quest", 0))
            )
            getSuccessful(user)
        }
    }

    override fun getUid() = database.getUid()!!

    override fun getSuccessful(user: User) {
        view.replaceData(user)
    }

    override fun sendReset() {
        sendResetEmailKnown()
    }

    override fun updateData(field: String, input: String) {
        if (!ModelValidator.checkValueIsEmpty(input)) {
            view.makeAlertDialog(R.string.field_required, R.string.error)
            return
        }

        val activity = view as Activity
        val cache: SharedPreferences = activity.getSharedPreferences("userData", 0)
        if (cache.contains("mail")) {
            val editor: SharedPreferences.Editor = cache.edit()
            editor.putString(field, input)
            editor.apply()
        }
        view.refresh()
    }

    override fun onSuccess(message: Int, title: Int) {
        view.makeAlertDialog(message, title)
    }

    override fun loadUser() {
        database.getUserDocument()
            .addOnSuccessListener {
                val user = database.buildUser(it)
                getSuccessful(user)
            }
    }

    override fun sendResetEmailKnown() {
        database.sendResetEmailKnown()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(R.string.password_change, R.string.success)
                }
            }
    }

}