package com.fourteenrows.p2pcs.activities.info.flappy

import android.graphics.Bitmap
import android.graphics.Canvas

class CharacterSprite(private val image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    var yVelocity = 10

    init {
        x = 100
        y = 50
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun update() {
        y += yVelocity
    }
}


