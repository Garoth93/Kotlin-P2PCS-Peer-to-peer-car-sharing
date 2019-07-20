package com.fourteenrows.p2pcs.activities.info

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.activities.info.flappy.MainActivity
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : GeneralActivity(), IInfoView {

    private lateinit var presenter: IInfoPresenter
    private var clicks = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        initializeDrawer()

        presenter = InfoPresenter(this)

        appversion.setOnClickListener {
            clicks += 1
            if (clicks >= 5) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}