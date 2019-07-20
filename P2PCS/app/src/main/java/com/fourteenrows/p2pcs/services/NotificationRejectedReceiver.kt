package com.fourteenrows.p2pcs.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fourteenrows.p2pcs.model.database.ModelFirebase

class NotificationRejectedReceiver : BroadcastReceiver() {
    val database = ModelFirebase()

    override fun onReceive(context: Context?, intent: Intent?) {
        val cid = intent!!.getStringExtra("cid")
        removeNotification(context!!)

        database.reject(cid)
    }

    fun removeNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(0)
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)
    }
}