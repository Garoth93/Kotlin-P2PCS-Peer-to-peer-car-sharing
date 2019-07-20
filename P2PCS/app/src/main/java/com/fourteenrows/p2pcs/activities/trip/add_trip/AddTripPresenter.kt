package com.fourteenrows.p2pcs.activities.trip.add_trip

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.trip.add_trip.user_trip.UserAddTripActivity
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.database.car.Car
import com.fourteenrows.p2pcs.model.drawer.DrawerSingleton
import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.fourteenrows.p2pcs.model.volley.VolleySingleton
import com.fourteenrows.p2pcs.objects.boosters.ActiveBooster
import com.fourteenrows.p2pcs.objects.boosters.ActiveBoosterContainer
import com.fourteenrows.p2pcs.objects.points.PointsGainer
import com.fourteenrows.p2pcs.objects.quests.QuestDirector
import com.fourteenrows.p2pcs.objects.trips.Participant
import com.fourteenrows.p2pcs.objects.trips.ToDatabaseTrip
import com.fourteenrows.p2pcs.objects.trips.Trip
import kotlinx.android.synthetic.main.activity_add_trip.*
import kotlinx.android.synthetic.main.trip_participant_textedit.view.*
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class AddTripPresenter(private val view: AddTripActivity, private val database: ModelDatabase = ModelFirebase()) :
    IAddTripPresenter {
    private var trip = Trip()
    private var car: Car? = null

    init {
        val cache: SharedPreferences = view.getSharedPreferences("userData", 0)
        fetchCar(cache.getString("carId", "Ms2Df2fwYv1GWRKQqUaE"))
    }

    private fun fetchCar(cid: String) {
        val emails = view.intent.getStringArrayListExtra("emails") as ArrayList<String>?
        val partContainer = view.participantsContainer as LinearLayout
        database.getCar(cid)
            .addOnSuccessListener {
                val seats = it.data!!["seats"] as Long
                for (i in 0 until seats) {
                    val child = LayoutInflater.from(partContainer.context)
                        .inflate(R.layout.trip_participant_textedit, partContainer, false)
                    if (emails != null && emails.size > i) {
                        child.tripPeople.setText(view.resources.getString(R.string.blank_string, emails[i.toInt()]))
                    }
                    child.tripPeople.setOnClickListener {
                        val intent = Intent((view as Activity), UserAddTripActivity::class.java)
                        val start = view.intent.getStringExtra("start")
                        val end = view.intent.getStringExtra("end")
                        intent.putExtra("emails", emails)
                        intent.putExtra("start", start)
                        intent.putExtra("end", end)
                        startActivity(view, intent, null)
                    }
                    partContainer.addView(child)
                }
                trip.cid = cid
                if (seats != 0L) {
                    if (emails != null) {
                        trip.participants = emails.size.toLong()
                        view.postiRimasti.visibility = View.VISIBLE
                        view.postiRimasti.text = "Rimasti ${seats - emails.size} posti su $seats"
                    } else {
                        view.postiRimasti.text = "Rimasti $seats posti su $seats"
                    }
                } else {
                    view.postiRimasti.visibility = View.GONE
                }
            }
    }

    override fun addTripData(
        start: String,
        end: String,
        distance: JSONObject,
        duration: JSONObject,
        participants: Long,
        cid: String
    ) {
        val trip = ToDatabaseTrip(
            distance.getString("text"),
            distance.getLong("value"),
            duration.getString("text"),
            duration.getLong("value"),
            end,
            participants,
            start,
            false,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toString(),
            cid
        )
        database.addTripData(trip)
            .addOnSuccessListener {
                view.stopProgressDialog()
                view.updateToolbar()
                    .addOnSuccessListener {
                        view.initializeDrawer()
                    }
                view.makeDataDialog(
                    view.resources.getString(
                        R.string.trip_data,
                        duration.getString("text"),
                        distance.getString("text")
                    ), R.string.success
                )
            }
    }

    override fun checkTripValues(start: String, end: String) {
        if (!ModelValidator.checkValueIsEmpty(start) || !ModelValidator.checkValueIsEmpty(end)) {
            view.makeAlertDialog(R.string.all_fields_required, R.string.error)
            return
        }
        getDistance()
    }

    override fun getDistance() {
        view.startProgressDialog()
        trip.start = view.tripStart.text.toString()
        trip.end = view.tripEnd.text.toString()

        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=${trip.start}&destination=${trip.end}&mode=driving&sensor=false&language=it&key=AIzaSyCOC1mSTaQw8jLdmN8VD5nP_s2Bn2jqi6w"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val status = it.getString("status")
                if (status == "OK") {
                    val routes = it.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0)
                    trip.start = routes.getString("start_address")
                    trip.end = routes.getString("end_address")
                    val distance = routes.getJSONObject("distance")
                    val duration = routes.getJSONObject("duration")
                    var emails = view.intent.getStringArrayListExtra("emails")
                    if (emails == null) {
                        emails = ArrayList<String>()
                    }
                    performTrip(distance, emails, duration)

                } else {
                    view.stopProgressDialog()
                    view.makeAlertDialog(R.string.no_trip, R.string.error)
                }
            },
            Response.ErrorListener {
                view.stopProgressDialog()
                view.makeAlertDialog(R.string.location_error, R.string.error)
            })
        val queue = VolleySingleton.getInstance(view).requestQueue
        VolleySingleton.getInstance(view).addToRequestQueue(jsonObjectRequest)
        queue.stop()
        queue.start()
    }

    override fun getReward(distance: Float, participants: ArrayList<Participant>) {
        val gcCoefficent = 2.5f
        val expCoefficent = 3f
        val gaia_coins = ceil((distance * gcCoefficent).toDouble())
        val exp = ceil((distance * expCoefficent).toDouble())

        //TODO("Check if boosters are actives")
    }

    override fun performTrip(distance: JSONObject, participants: ArrayList<String>, duration: JSONObject) {
        val distanceValue = distance.getLong("value")
        val durationValue = duration.getLong("value")

        database.getActiveBoosters()
            .addOnSuccessListener {
                val boosters = ActiveBoosterContainer()
                if (it.documents.size > 0) {
                    it.documents.forEach { doc ->
                        val data = doc.data!!
                        boosters.add(
                            ActiveBooster(
                                data["name"] as String,
                                data["category"] as Long,
                                data["multiplicator"] as Long,
                                true,
                                data["quantity"] as Long,
                                data["description"] as String,
                                doc.id
                            )
                        )
                    }
                    boosters.keepOnlyBoosterTripOnly()
                }
                updateUserPoints(
                    PointsGainer(
                        distanceValue,
                        participants.size,
                        boosters
                    )
                )
                val start = view.tripStart.text.toString()
                val end = view.tripEnd.text.toString()
                val questDirector = QuestDirector(view)
                questDirector.addTripProgress(distanceValue, participants, durationValue)
                addTripData(start, end, distance, duration, participants.size.toLong(), trip.cid)
                DrawerSingleton.getDrawer(view)
            }
    }

    override fun updateUserPoints(pointsGainer: PointsGainer) {
        database.getUserDocument()
            .addOnSuccessListener {
                val gaiaCoins = it.data!!["gaia_coins"] as Long + pointsGainer.gaiaCoins
                val exp = it.data!!["exp"] as Long + pointsGainer.exp
                val weekPoints = it.data!!["week_points"] as Long + pointsGainer.exp
                database.addUserPoints(exp, gaiaCoins, weekPoints)
            }
    }

}