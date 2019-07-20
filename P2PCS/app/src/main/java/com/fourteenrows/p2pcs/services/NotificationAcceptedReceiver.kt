package com.fourteenrows.p2pcs.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.objects.reservations.Reservation
import java.util.*


class NotificationAcceptedReceiver : BroadcastReceiver() {
    val database = ModelFirebase()

    override fun onReceive(context: Context?, intent: Intent?) {
        val cid = intent!!.getStringExtra("cid")
        removeNotification(context!!)

        database.accept(cid)
        ModelFirebase().insertReservation(
            Reservation(
                intent.getStringExtra("cid"),
                intent.getStringExtra("model"),
                intent.getStringExtra("uid"),
                Date(intent.getLongExtra("startDate", 0)),
                Date(intent.getLongExtra("endDate", 0)),
                false
            )
        )
    }


    fun removeNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(0)
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)
    }
}