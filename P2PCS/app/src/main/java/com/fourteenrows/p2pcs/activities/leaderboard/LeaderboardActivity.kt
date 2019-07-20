package com.fourteenrows.p2pcs.activities.leaderboard

import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import kotlinx.android.synthetic.main.activity_leaderboard.*

class LeaderboardActivity : GeneralActivity(), ILeaderboardView {

    private lateinit var presenter: LeaderboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        initializeDrawer()
        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        presenter = LeaderboardPresenter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}