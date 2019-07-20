package com.fourteenrows.p2pcs.activities.trip.add_trip

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.activities.trip.TripActivity
import com.fourteenrows.p2pcs.activities.trip.add_trip.location_trip.LocationTripActivity
import kotlinx.android.synthetic.main.activity_add_trip.*

class AddTripActivity : GeneralActivity(), IAddTripView {

    private lateinit var presenter: AddTripPresenter

    companion object {
        val TRIP_START = "start"
        val TRIP_END = "end"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)
        initializeDrawer()

        presenter = AddTripPresenter(this)

        var start: String? = null
        var end: String? = null

        if (intent.extras != null) {
            start = intent.getStringExtra(TRIP_START)
            end = intent.getStringExtra(TRIP_END)

            if (start != null) {
                tripStart.setText(start)
            }
            if (end != null) {
                tripEnd.setText(end)
            }
        }

        tripButton.setOnClickListener {
            val tripStart = tripStart.text.toString()
            val tripEnd = tripEnd.text.toString()
            presenter.checkTripValues(tripStart, tripEnd)
        }

        tripStart.setOnClickListener {
            val toIntent = Intent(this, LocationTripActivity::class.java)
            toIntent.putExtra("type", TRIP_START)
            val emails = intent.getStringArrayListExtra("emails") as ArrayList<String>?
            if (emails != null) {
                toIntent.putExtra("emails", emails)
            }
            toIntent.putExtra(TRIP_START, start)
            toIntent.putExtra(TRIP_END, end)
            startActivity(toIntent)
        }

        tripEnd.setOnClickListener {
            val toIntent = Intent(this, LocationTripActivity::class.java)
            toIntent.putExtra("type", TRIP_END)
            val emails = intent.getStringArrayListExtra("emails") as ArrayList<String>?
            if (emails != null) {
                toIntent.putExtra("emails", emails)
            }
            toIntent.putExtra(TRIP_START, start)
            toIntent.putExtra(TRIP_END, end)
            startActivity(toIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun makeDataDialog(message: String, title: Int) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setOnDismissListener {
                val intent = Intent(this, TripActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
