package com.fourteenrows.p2pcs.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.reservation.ReservationActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RequesConfermationtListener(private val context: Context) {
    private val appContext = context.applicationContext

    fun initalize() {
        FirebaseFirestore.getInstance()
            .collection("Requests")
            .addSnapshotListener { querySnapshot, _ ->

                if (!querySnapshot!!.metadata.isFromCache) {
                    querySnapshot.documents.forEach { req ->
                        if (req != null && req["applicant"] as String == ModelFirebase().getUid()!!) {
                            FirebaseFirestore.getInstance()
                                .collection("Requests")
                                .document(req.id)
                                .addSnapshotListener { documentSnapshot, _ ->

                                    if (documentSnapshot != null && documentSnapshot["accepted"] != null) {
                                        notifyRequestPermission(
                                            documentSnapshot["accepted"] as Boolean,
                                            documentSnapshot["model"] as String,
                                            ModelDates.toLocaleTimeFormat((documentSnapshot["startDate"] as Timestamp).toDate()),
                                            ModelDates.toTinyTimeSpanFormat(
                                                Date(
                                                    (documentSnapshot["startDate"] as Timestamp).toDate().time -
                                                            (documentSnapshot["endDate"] as Timestamp).toDate().time
                                                )
                                            )
                                        )
                                        ModelFirebase().removeRequest(req.id)
                                    }
                                }
                        }
                    }
                }
            }
    }

    private fun notifyRequestPermission(result: Boolean, model: String, date: String, timeSlot: String) {
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bigText = NotificationCompat.BigTextStyle()
        if (result) {
            bigText.setSummaryText("Prenotazione confermata")
            bigText.setBigContentTitle("Richiesta di prenotazione accettata")
        } else {
            bigText.setSummaryText("Prenotazione rigettata")
            bigText.setBigContentTitle("Richiesta di prenotazione rifiutata")
        }

        val intent = Intent(appContext, ReservationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)


        val mBuilder = NotificationCompat.Builder(appContext, "notify_001")
            .setStyle(bigText)
            .setContentText("Modello: $model \nIl $date\nSlot: $timeSlot")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(Notification.PRIORITY_HIGH)
            .setVibrate(longArrayOf(500, 0, 500, 500))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= 26) {
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }

        mNotificationManager.notify(0, mBuilder.build())
    }
}