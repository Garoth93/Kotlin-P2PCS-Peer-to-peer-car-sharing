package com.fourteenrows.p2pcs.activities.car.add_car.location_car

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.volley.VolleySingleton
import com.fourteenrows.p2pcs.objects.cars.CarLocation
import kotlinx.android.synthetic.main.activity_add_trip_location.*


class LocationCarPresenter(toView: LocationCarActivity, private val database: ModelDatabase = ModelFirebase()) :
    ILocationCarPresenter, ILocationCarListener {

    private var view = toView

    init {
        view.tripPoint.hint = view.resources.getString(R.string.car_position)
    }

    override fun getPlaces(text: String) {
        view.startProgressDialog()
        val point = view.tripPoint.text.toString()

        val location = "45.411154, 11.887554"
        val array = ArrayList<CarLocation>()
        val url =
            "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=$point&location=$location&radius=1000&language=it&key=AIzaSyCOC1mSTaQw8jLdmN8VD5nP_s2Bn2jqi6w"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {
                val status = it.getString("status")
                if (status == "OK") {
                    view.noLocation.visibility = android.view.View.GONE
                    view.recycleView.visibility = android.view.View.VISIBLE
                    val predictions = it.getJSONArray("predictions")
                    for (i in 0 until predictions.length()) {
                        val structure = predictions.getJSONObject(i).getJSONObject("structured_formatting")
                        val main = structure.getString("main_text")
                        val second = structure.getString("secondary_text")
                        val newLocation = CarLocation(main, second)
                        array.add(newLocation)
                    }
                    view.setRecyclerAdapter(LocationCarRecyclerAdapter(array, this))
                    view.stopProgressDialog()
                } else {
                    view.noLocation.visibility = android.view.View.VISIBLE
                    view.recycleView.visibility = android.view.View.GONE
                    view.stopProgressDialog()
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
}