package com.fourteenrows.p2pcs.services

import android.app.IntentService
import android.content.Context
import android.content.Intent

class ReservationService private constructor() : IntentService("ReservationIntentService") {
    override fun onHandleIntent(intent: Intent?) {}

    fun updateOwnCarListeners(cids: ArrayList<String>) {
        companionUpdateOwnCarListeners(cids)
    }

    fun updateRequestObserver() {
        companionUpdateRequestObserver()
    }

    companion object {
        private var requestOnOwnCarObserver: RequestOnOwnCarListener? = null
        private var requestObserver: RequesConfermationtListener? = null
        private var instance: ReservationService? = null
        private var context: Context? = null

        fun getInstance(context: Context): ReservationService {
            this.context = context
            if (instance == null) {
                instance = ReservationService()
                requestOnOwnCarObserver = RequestOnOwnCarListener(context)
                requestObserver = RequesConfermationtListener(context)
            }
            return instance!!
        }

        fun getInstance() = instance

        fun companionUpdateOwnCarListeners(cids: ArrayList<String>) {
            requestOnOwnCarObserver!!.initialize(cids)
        }

        fun companionUpdateRequestObserver() {
            requestObserver!!.initalize()
        }
    }

}
