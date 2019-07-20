package com.fourteenrows.p2pcs.activities.trip.add_trip.user_trip

import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import kotlinx.android.synthetic.main.activity_user_trip.*

class UserAddTripActivity : GeneralActivity(), IUserAddTripView {

    private lateinit var presenter: IUserAddTripPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_trip)
        initializeDrawer()

        presenter = UserAddTripPresenter(this)

        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        searchUser.setOnClickListener {
            val email = userTripPeople.text.toString()

            if (email.isNotEmpty()) {
                presenter.fetchUsers(email)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun setRecyclerAdapter(adapter: UserAddTripRecycleAdapter) {
        recycleView.adapter = adapter
    }
}
