package com.fourteenrows.p2pcs.activities.trip

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.activities.trip.add_trip.AddTripActivity
import com.fourteenrows.p2pcs.model.utility.RecyclerItemTouchHelper
import kotlinx.android.synthetic.main.activity_trip.*

class TripActivity : GeneralActivity(), ITripView {

    private lateinit var presenter: ITripPresenter
    private val DEBUG = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)
        initializeDrawer()

        presenter = TripPresenter(this)
        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        addTrip.setOnClickListener {
            if (!DEBUG) {
                presenter.requestStartTrip()
            } else {
                val intent = Intent(this, AddTripActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun gotoAdd(intent: Intent) {
        startActivity(intent)
    }

    override fun showErrorNoReservationActive() {
        makeAlertDialog(R.string.no_active_reservation, R.string.error)
    }

    override fun makeConfirmationDialog(tid: String) {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.trip_archive)
            .setMessage(R.string.confirmation_archiviation_trip)
            .setPositiveButton(R.string.confirm) { _, _ ->
                presenter.hideTrip(tid)
            }
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setOnDismissListener {
                (recycleView.adapter as TripRecyclerAdapter).notifyDataSetChanged()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    override fun setRecyclerAdapter(adapter: TripRecyclerAdapter) {
        recycleView.adapter = adapter
        val itemTouchHelperCallback = RecyclerItemTouchHelper(
            0,
            ItemTouchHelper.LEFT,
            presenter as RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
        )
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycleView)
    }

}