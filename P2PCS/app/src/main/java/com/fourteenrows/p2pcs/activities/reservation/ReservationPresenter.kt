package com.fourteenrows.p2pcs.activities.reservation

import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.model.utility.RecyclerItemTouchHelper
import com.fourteenrows.p2pcs.objects.reservations.*
import com.fourteenrows.p2pcs.services.ReservationService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class ReservationPresenter(toView: ReservationActivity, private val database: ModelDatabase = ModelFirebase()) :
    IReservationPresenter, IReservationListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private var view = toView

    init {
        view.startProgressDialog()
        fetchReservations()
        initializeReservationService()
    }

    private fun initializeReservationService() {
        database.getUserVehicles()
            .addOnSuccessListener { cars ->
                val cids = cars.documents
                    .map {
                        it.id
                    }.stream()
                    .collect(Collectors.toList())
                ReservationService.getInstance(view.applicationContext)
                    .updateOwnCarListeners(ArrayList(cids))
            }
        ReservationService.getInstance(view.applicationContext)
            .updateRequestObserver()
    }

    override fun deleteReservation(rid: String) {
        database.delete("Dates", rid)
            .addOnSuccessListener {
                fetchReservations()
            }
    }

    override fun confirmDeletion(rid: String) {
        val SIX_HOURS = 1000L * 60L * 60L * 6L
        val TWO_HOURS = 1000L * 60L * 60L * 2L
        getReservation(rid)
            .addOnSuccessListener {
                val map = it.data
                val asd = map?.get("start_slot") as Timestamp
                val a = asd.toDate().time + SIX_HOURS //+ TWO_HOURS
                if (a - Date().time > SIX_HOURS) {
                    view.makeConfirmationDialog(rid)
                } else {
                    view.makeAlertDialog(R.string.ongoing_reservation, R.string.error)
                }
            }
    }

    override fun onSuccess(reservation: ArrayList<ReservationCardObject>) {
        view.setRecyclerAdapter(ReservationRecyclerAdapter(reservation, this))
        view.stopProgressDialog()
    }

    override fun onFailure() {}

    //map: mappa ordinata di prenotazioni dalle più alle meno recenti
    private fun getReservations(map: ArrayList<Map<String, Any>>): ArrayList<ReservationCardObject> {
        val array = ArrayList<ReservationCardObject>()
        if (map.size == 0 || ModelDates.isInThePast(map[0]["end_slot"] as Timestamp)) {
            array.add(
                MessageErrorObjectReservation(
                    ReservationCardType.TITLE_MESSAGE_ERROR
                )
            )
        } else array.add(ReservationCardObject(ReservationCardType.TITLE_ACTIVE_PRENOTATION))

        var newIntoPast = false
        map.forEach {
            val startSlot = it["start_slot"] as Timestamp
            val endSlot = it["end_slot"] as Timestamp
            if (!ModelDates.isInThePast(endSlot)) {
                array.add(
                    ActiveReservationReservationCardObject(
                        ReservationCardType.TITLE_ACTIVE_PRENOTATION,
                        it["model"] as String,
                        ModelDates.toLocaleTimeFormat(startSlot.toDate()),
                        ModelDates.getSlotString(startSlot, endSlot),
                        it["rid"] as String,
                        it["carId"] as String,
                        it["rid"] as String != ""
                    )
                )
            } else {
                if (!newIntoPast) {
                    newIntoPast = true
                    array.add(ReservationCardObject(ReservationCardType.TITLE_PAST_PRENOTATION))
                }
                array.add(
                    //TODO(Set endDate, get total cost")
                    PastReservationReservationCardObject(
                        ReservationCardType.TITLE_PAST_PRENOTATION,
                        it["model"] as String, ModelDates.toLocaleTimeFormat(startSlot.toDate()),
                        ModelDates.getSlotString(startSlot, endSlot),
                        it["rid"] as String,
                        it["carId"] as String,
                        (0L..100L).random().toDouble()
                    )
                )
            }
        }
        return array
    }

    private fun clearRequest() {
        database.getUserRequests()
            .addOnSuccessListener { reqs ->
                reqs.forEach { req ->
                    if (req["accepted"] != null) {
                        database.removeRequest(req.id)
                    }
                }
            }
    }

    private fun clearRequestAndFetch() {
        val uid = database.getUid()
        database.getUserRequests()
            .addOnSuccessListener { reqs ->
                reqs.forEach { req ->
                    if (req["accepted"] != null) {
                        database.removeRequest(req.id)
                    }
                }
                FirebaseFirestore.getInstance().collection("Dates")
                    .whereEqualTo("owner", uid)
                    .orderBy("start_slot", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener {
                        val array = ArrayList<Map<String, Any>>()
                        it.forEach { x ->
                            val map = x.data.toMap() as MutableMap
                            if (!(map["deleted"] as Boolean)) {
                                map["rid"] = x.id
                                array.add(map)
                            }
                        }
                        database.getUserRequests()
                            .addOnSuccessListener { reqs ->
                                reqs.forEach { req ->
                                    array.add(
                                        hashMapOf(
                                            Pair("carId", req!!.id as Any),
                                            Pair("deleted", false as Any),
                                            Pair("end_slot", req["endDate"] as Any),
                                            Pair("start_slot", req["startDate"] as Any),
                                            Pair("model", req["model"] as Any),
                                            Pair("owner", database.getUid() as Any),
                                            Pair("rid", "")
                                        )
                                    )
                                }
                                onSuccess(getReservations(array))
                            }
                    }
            }
    }

    override fun fetchReservations() {
        clearRequestAndFetch()
    }

    override fun hideReservation(rid: String) {
        database.deleteReservation(rid)
            .addOnSuccessListener {
                fetchReservations()
            }
    }

    override fun getReservation(rid: String) = database.getReservation(rid)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is ReservationRecyclerAdapter.ActiveReservationViewHolder
            && viewHolder.pending.visibility == android.view.View.GONE
        ) {
            confirmDeletion(viewHolder.rid.text.toString())
        } else if (viewHolder is ReservationRecyclerAdapter.PastReservationViewHolder) {
            view.makeConfirmationArchivedDialog(viewHolder.rid.text.toString())
        }
    }
}