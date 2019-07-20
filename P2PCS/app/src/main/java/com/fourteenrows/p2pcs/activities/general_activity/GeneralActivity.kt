package com.fourteenrows.p2pcs.activities.general_activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.leaderboard.LeaderboardActivity
import com.fourteenrows.p2pcs.activities.reservation.ReservationActivity
import com.fourteenrows.p2pcs.model.drawer.DrawerSingleton
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.dialog_show_reward.view.*
import java.util.*

abstract class GeneralActivity : AppCompatActivity(), IGeneralView {
    private lateinit var progressDialog: ProgressDialog
    lateinit var toolBar: Toolbar

    override fun initializeDrawer() {
        toolBar = findViewById(R.id.appBar)

        setSupportActionBar(toolBar)
        toolBar.setOnMenuItemClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
            true
        }
        DrawerSingleton.getDrawer(this)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun makeAlertDialog(message: Int, title: Int, toLogin: Boolean) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ ->
                if (toLogin) {
                    val intent = Intent(this, ReservationActivity::class.java)
                    startActivity(intent)
                }
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun startProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setTitle(R.string.wait)
        progressDialog.setMessage(resources.getString(R.string.loading))
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    override fun stopProgressDialog() {
        progressDialog.dismiss()
    }

    override fun updateToolbar(): Task<DocumentSnapshot> {
        return DrawerSingleton.updateUser(this)
    }

    override fun updateValues() {
        DrawerSingleton.loadUserStates(this)
    }


    fun setPrenotationToCache(activity: Activity, carId: String, model: String, owner: String, startSlot: Date) {
        val cache: SharedPreferences = activity.getSharedPreferences("userData", 0)
        val editor: SharedPreferences.Editor = cache.edit()
        editor.putString("carId", carId)
        editor.putString("model", model)
        editor.putString("owner", owner)
        editor.putString("start_slot", startSlot.time.toString())
        editor.apply()
    }

    override fun makeRewardDialog(
        gaiaCoins: Long,
        exp: Long,
        weekPoints: Long,
        itemName: String,
        imageUrl: String,
        instance: GeneralActivityPresenter
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_show_reward, null)
        dialogView.gaiaCoins.text = resources.getString(R.string.coins, gaiaCoins.toString())
        dialogView.weekPoints.text = resources.getString(R.string.weekly, weekPoints.toString())
        dialogView.itemName.text = itemName
        //Picasso.get().load(imageUrl).into(dialogView.imageView)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(R.string.success)
            .setMessage(R.string.quest_done)
            .setPositiveButton(R.string.ok) { _, _ -> }

        val dialog: AlertDialog = builder.create()

        dialogView.changeReward.setOnClickListener {
            dialog.dismiss()
            instance.changeReward()
        }
        dialog.show()
    }
}