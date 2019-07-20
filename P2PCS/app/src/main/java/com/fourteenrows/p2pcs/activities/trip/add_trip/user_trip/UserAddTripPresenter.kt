package com.fourteenrows.p2pcs.activities.trip.add_trip.user_trip

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.fourteenrows.p2pcs.activities.trip.add_trip.AddTripActivity
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.objects.trips.Participant

class UserAddTripPresenter(toView: UserAddTripActivity, private val database: ModelDatabase = ModelFirebase()) :
    IUserAddTripPresenter, IUserAddTripListener {

    private var view = toView
    private val participants = ArrayList<Participant>()

    init {

    }

    override fun fetchUsers(email: String) {
        database.fetchUsers(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result != null) {
                        val array = ArrayList<String>()
                        it.result!!.forEach { user ->
                            if (user.get("mail").toString().contains(email)) {
                                array.add(user.get("mail").toString())
                            }
                        }
                        onSuccess(array)
                    }
                } else {
                    onFailure()
                }
            }
    }

    override fun onSuccess(array: ArrayList<String>) {
        view.setRecyclerAdapter(
            UserAddTripRecycleAdapter(
                array,
                this
            )
        )
    }

    override fun onFailure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Synchronized
    private fun addToList(participant: Participant) {
        participants.add(participant)
    }

    override fun addParticipant(userEmail: String) {
        val extras = view.intent.extras
        lateinit var emails: ArrayList<String>
        emails = if (extras != null && extras.containsKey("emails")) {
            view.intent.getStringArrayListExtra("emails") as ArrayList<String>
        } else {
            arrayListOf()
        }
        emails.add(userEmail)
        val intent = Intent(view, AddTripActivity::class.java)
        val start = (view as Activity).intent.getStringExtra("start")
        val end = (view as Activity).intent.getStringExtra("end")
        intent.putExtra("start", start)
        intent.putExtra("end", end)
        intent.putExtra("emails", emails)
        startActivity(view, intent, null)
    }

}