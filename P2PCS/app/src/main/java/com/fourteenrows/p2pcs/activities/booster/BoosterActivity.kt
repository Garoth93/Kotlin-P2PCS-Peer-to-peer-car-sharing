package com.fourteenrows.p2pcs.activities.booster

import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import kotlinx.android.synthetic.main.activity_booster.*

class BoosterActivity : GeneralActivity(), IBoosterView {

    private lateinit var presenter: BoosterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booster)
        initializeDrawer()

        presenter = BoosterPresenter(this)

        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun setRecyclerAdapter(boosterRecyclerAdapter: BoosterRecyclerAdapter) {
        recycleView.adapter = boosterRecyclerAdapter
    }
}