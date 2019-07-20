package com.fourteenrows.p2pcs.activities.trip

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.activities.trip.add_trip.AddTripActivity
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.RecyclerItemTouchHelper
import com.fourteenrows.p2pcs.objects.trips.Trip
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_trip.*

class TripPresenter(private val view: TripActivity, private val database: ModelDatabase = ModelFirebase()) :
    ITripPresenter, ITripListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private val trips = ArrayList<Trip>()

    init {
        view.startProgressDialog()
        fetchTrips()
    }

    private fun fetchTrips() {
        database.getTrips()
            .addOnSuccessListener {
                it.forEach { doc ->
                    addTrip(parseTrip(doc))
                }
                view.stopProgressDialog()
                if (trips.size == 0) {
                    (view as Activity).message.visibility = android.view.View.VISIBLE
                    (view as Activity).recycleView.visibility = android.view.View.GONE
                } else {
                    (view as Activity).message.visibility = android.view.View.GONE
                    (view as Activity).recycleView.visibility = android.view.View.VISIBLE
                    updateRecyclerView()
                }
            }
    }

    @Synchronized
    private fun addTrip(trip: Trip) {
        trips.add(trip)
    }

    private fun updateRecyclerView() {
        view.setRecyclerAdapter(TripRecyclerAdapter(trips, this))
    }

    override fun hideTrip(tid: String) {
        database.hideTrip(tid)
            .addOnSuccessListener {
                fetchTrips()
            }
    }

    private fun parseTrip(doc: QueryDocumentSnapshot): Trip =
        Trip(
            doc["start"] as String,
            doc["end"] as String,
            doc["participants"] as Long,
            doc["distance_text"] as String,
            doc["distance_value"] as Long,
            doc["duration_text"] as String,
            doc["duration_value"] as Long,
            doc["deleted"] as Boolean,
            doc["date"] as String,
            doc.id,
            doc["cid"] as String
        )

    override fun requestStartTrip() {
        database.getActivePrenotation()
            .addOnSuccessListener {
                if (it.size() == 0) {
                    view.showErrorNoReservationActive()
                } else {
                    val intent = Intent(view.baseContext, AddTripActivity::class.java)
                    view.setPrenotationToCache(
                        view,
                        it.documents[0]["carId"] as String,
                        it.documents[0]["model"] as String,
                        it.documents[0]["owner"] as String,
                        (it.documents[0]["start_slot"] as Timestamp).toDate()
                    )
                    view.gotoAdd(intent)
                }
            }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is TripRecyclerAdapter.TripViewHolder) {
            view.makeConfirmationDialog(viewHolder.tid.text.toString())
        }
    }
}