package com.fourteenrows.p2pcs.activities.booster

import android.app.Activity
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.objects.boosters.ActiveBooster
import kotlinx.android.synthetic.main.activity_booster.*

class BoosterPresenter(toView: BoosterActivity, private val database: ModelDatabase = ModelFirebase()) :
    IBoosterPresenter, IBoosterListener {

    private var view = toView
    private val boosterList = ArrayList<ActiveBooster>()

    init {
        view.startProgressDialog()
        fetchBooster()
    }

    private fun deactivateBooster(bid: String) {
        database.deactivateBooster(bid)
            .addOnSuccessListener {
                fetchBooster()
            }
    }

    override fun endEffectBooster(bid: String) {
        database.getUserBoostersWithBID(bid)
            .addOnSuccessListener {
                val remained = it["quantity"] as Long
                if (remained == 0L) {
                    removeUserBooster(bid)
                } else {
                    deactivateBooster(bid)
                }
            }
    }

    private fun fetchBooster() {
        boosterList.clear()
        database.getUserBoosters()
            .addOnSuccessListener { documents ->
                documents.documents
                    .stream().parallel()
                    .forEach {
                        parseBooster(it.data!!, it.id)
                    }
                updateRecycler()
            }
    }

    @Synchronized
    private fun parseBooster(boosterMap: MutableMap<String, Any>, bid: String) {
        boosterList.add(
            ActiveBooster(
                boosterMap["name"] as String,
                boosterMap["category"] as Long,
                boosterMap["multiplicator"] as Long,
                boosterMap["active"] as Boolean,
                boosterMap["quantity"] as Long,
                boosterMap["description"] as String,
                bid
            )
        )
    }

    private fun removeUserBooster(bid: String) {
        database.removeUserBooster(bid)
            .addOnSuccessListener {
                fetchBooster()
            }
    }

    @Synchronized
    private fun updateRecycler() {
        if (boosterList.size > 0) {
            (view as Activity).message.visibility = android.view.View.GONE
            view.setRecyclerAdapter(BoosterRecyclerAdapter(boosterList, this))
        } else {
            (view as Activity).message.visibility = android.view.View.VISIBLE
        }
        view.stopProgressDialog()
    }


    override fun activateBooster(bid: String, quantity: Long) {
        database.activateBooster(bid, quantity)
            .addOnSuccessListener {
                fetchBooster()
            }
    }

    override fun boosterAlreadyActivated() {
        view.makeAlertDialog(R.string.booster_already_active, R.string.error)
    }

    override fun showDialog() {
        view.makeAlertDialog(R.string.booster_activated,R.string.evvivaBooster)
    }
}