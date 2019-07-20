package com.fourteenrows.p2pcs.activities.trip.add_trip

import com.fourteenrows.p2pcs.objects.points.PointsGainer
import com.fourteenrows.p2pcs.objects.trips.Participant
import org.json.JSONObject

interface IAddTripPresenter {
    fun checkTripValues(start: String, end: String)
    fun getDistance()
    fun updateUserPoints(pointsGainer: PointsGainer)
    fun getReward(distance: Float, participants: ArrayList<Participant>)
    fun performTrip(distance: JSONObject, participants: ArrayList<String>, duration: JSONObject)
    fun addTripData(
        start: String,
        end: String,
        distance: JSONObject,
        duration: JSONObject,
        participants: Long,
        cid: String
    )
}
