package com.fourteenrows.p2pcs.model.utility

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.fourteenrows.p2pcs.R
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class ImageHelper {
    fun getLinkOf(id: String): String {
        return "https://firebasestorage.googleapis.com/v0/b/p2pcs-acc00.appspot.com/o/$id.jpg?alt=media"
    }

    fun getNameOf(uid: String) = "$uid.jpg"

    fun setAvatar(profileImage: ImageView, uid: String, drawable: Drawable? = null) {
        val callback = object : Callback {
            override fun onSuccess() {
            }

            override fun onError(e: java.lang.Exception?) {
                Picasso.get()
                    .load(R.drawable.avatar)
                    .into(profileImage)
            }
        }

        val picasso = Picasso.get()
            .load(getLinkOf(uid))
            .fit()
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
        if (drawable != null) {
            picasso.placeholder(drawable)
        }
        picasso.into(profileImage, callback)
    }

    fun setCarImage(imageCar: ImageView, cid: String) {
        val callback = object : Callback {
            override fun onSuccess() {}

            override fun onError(e: java.lang.Exception?) {
                Picasso.get()
                    .load(R.drawable.icon_auto)
                    .into(imageCar)
            }
        }

        val s = getLinkOf(cid)
        Picasso.get()
            .load(s)
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .into(imageCar, callback)
    }

    fun setShopImage(imageView: ImageView, id: String) {
        if (id.startsWith("c00")) {
            Picasso.get()
                .load(R.drawable.amazon)
                .into(imageView)
        } else {
            val callback = object : Callback {
                override fun onSuccess() {}

                override fun onError(e: java.lang.Exception?) {
                    Picasso.get()
                        .load(R.drawable.icon_auto)
                        .into(imageView)
                }
            }
            val s = getLinkOf(id)
            Picasso.get()
                .load(s)
                .into(imageView, callback)
        }
    }

}
