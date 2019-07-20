package com.fourteenrows.p2pcs.activities.trip.add_trip.location_trip

import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import kotlinx.android.synthetic.main.activity_add_trip_location.*

class LocationTripActivity : GeneralActivity(), ILocationTripView {

    private lateinit var presenter: ILocationTripPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip_location)
        initializeDrawer()

        presenter = LocationTripPresenter(this)

        searchUser.setOnClickListener {
            val text = tripPoint.text.toString()
            if (text.isNotEmpty()) {
                presenter.getPlaces(text)
            }
        }

        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun setRecyclerAdapter(adapter: LocationTripRecyclerAdapter) {
        recycleView.adapter = adapter
    }
}