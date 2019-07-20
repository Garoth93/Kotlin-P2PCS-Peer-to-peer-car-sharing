package com.fourteenrows.p2pcs.activities.leaderboard

import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.objects.user.User
import kotlinx.android.synthetic.main.activity_leaderboard.*

class LeaderboardPresenter(
    private val view: LeaderboardActivity,
    private val database: ModelDatabase = ModelFirebase()
) :
    ILeaderboardPresenter, ILeaderboardListener {
    val leaderboard = ArrayList<User>()

    init {
        view.startProgressDialog()
        fetchLeaderboard()
    }

    private fun fetchLeaderboard() {
        leaderboard.clear()
        database.fetchLeaderboard()
            .addOnSuccessListener {
                it.documents.forEach { dsu ->
                    leaderboard.add(database.buildUser(dsu))
                }
                updateRecycler()
            }
    }

    @Synchronized
    private fun updateRecycler() {
        view.recycleView.adapter = LeaderboardRecyclerAdapter(leaderboard, this, database.getUid()!!)
        view.stopProgressDialog()
    }

}