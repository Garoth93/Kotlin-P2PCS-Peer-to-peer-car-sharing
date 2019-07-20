package com.fourteenrows.p2pcs.activities.trip.add_trip.user_trip

interface IUserAddTripPresenter {
    fun fetchUsers(email: String)
    fun onSuccess(array: ArrayList<String>)
    fun onFailure()
}