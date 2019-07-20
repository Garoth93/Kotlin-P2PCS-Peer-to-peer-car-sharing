package com.fourteenrows.p2pcs.activities.reservation.add_reservation.add_reservation_vehicle

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import kotlinx.android.synthetic.main.activity_add_reservation_vehicle.*

class AddReservationVehicleActivity : GeneralActivity(), IAddReservationVehicleView {

    private lateinit var presenter: AddReservationVehiclePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reservation_vehicle)
        initializeDrawer()

        presenter = AddReservationVehiclePresenter(this)

        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun setRecyclerAdapter(addReservationVehicleRecyclerAdapter: AddReservationVehicleRecyclerAdapter) {
        recycleView.adapter = addReservationVehicleRecyclerAdapter
    }

    override fun refresh() {
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}