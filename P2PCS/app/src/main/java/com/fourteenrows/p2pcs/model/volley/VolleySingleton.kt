package com.fourteenrows.p2pcs.model.volley

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.android.volley.RequestQueue as RequestQueue1

class VolleySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue1 by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}