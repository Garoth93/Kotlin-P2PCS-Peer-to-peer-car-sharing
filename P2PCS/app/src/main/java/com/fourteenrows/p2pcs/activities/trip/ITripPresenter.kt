package com.fourteenrows.p2pcs.activities.trip

interface ITripPresenter {
    fun requestStartTrip()

    fun hideTrip(tid: String)
}