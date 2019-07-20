package com.fourteenrows.p2pcs.activities.info.flappy

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas

class PipeSprite(private val image: Bitmap, private val image2: Bitmap, var xX: Int, var yY: Int) {
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(
            image,
            xX.toFloat(),
            (-(GameView.gapHeight / 2) + yY).toFloat(),
            null
        )
        canvas.drawBitmap(
            image2,
            xX.toFloat(),
            (screenHeight / 2 + GameView.gapHeight / 2 + yY).toFloat(), null
        )
    }

    fun update() {
        xX -= GameView.velocity
    }
}
