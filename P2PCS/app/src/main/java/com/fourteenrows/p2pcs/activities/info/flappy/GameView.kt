package com.fourteenrows.p2pcs.activities.info.flappy

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.fourteenrows.p2pcs.R
import java.util.*

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val thread: MainThread
    private var characterSprite: CharacterSprite? = null
    private lateinit var pipe1: PipeSprite
    private lateinit var pipe2: PipeSprite
    private lateinit var pipe3: PipeSprite
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels

    companion object {
        var gapHeight = 500
        var velocity = 10
    }

    init {
        holder.addCallback(this)
        thread = MainThread(holder, this)
        isFocusable = true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        val matrix = Matrix()

        matrix.postScale(scaleWidth, scaleHeight)

        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        characterSprite!!.y = characterSprite!!.y - characterSprite!!.yVelocity * 10
        return super.onTouchEvent(event)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        makeLevel()
        thread.setRunning(true)
        thread.start()
    }

    private fun makeLevel() {
        characterSprite =
            CharacterSprite(
                getResizedBitmap(
                    BitmapFactory.decodeResource(resources, R.drawable.bird),
                    300,
                    240
                )
            )
        val bmp = getResizedBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.pipe_down),
            500,
            Resources.getSystem().displayMetrics.heightPixels / 2
        )
        val bmp2 = getResizedBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.pipe_up),
            500,
            Resources.getSystem().displayMetrics.heightPixels / 2
        )

        pipe1 = PipeSprite(bmp, bmp2, 2000, 100)
        pipe2 = PipeSprite(bmp, bmp2, 4500, 100)
        pipe3 = PipeSprite(bmp, bmp2, 3200, 100)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    fun update() {
        logic()
        characterSprite!!.update()
        pipe1.update()
        pipe2.update()
        pipe3.update()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null) {
            canvas.drawRGB(0, 100, 205)
            characterSprite!!.draw(canvas)
            pipe1.draw(canvas)
            pipe2.draw(canvas)
            pipe3.draw(canvas)
        }
    }

    private fun logic() {
        val pipes = ArrayList<PipeSprite>()
        pipes.add(pipe1)
        pipes.add(pipe2)
        pipes.add(pipe3)

        for (i in pipes.indices) {
            if (characterSprite!!.y < pipes[i].yY + screenHeight / 2 - gapHeight / 2
                && characterSprite!!.x + 300 > pipes[i].xX
                && characterSprite!!.x < pipes[i].xX + 500
            ) {
                resetLevel()
            } else if (characterSprite!!.y + 240 > screenHeight / 2 + gapHeight / 2 + pipes[i].yY
                && characterSprite!!.x + 300 > pipes[i].xX
                && characterSprite!!.x < pipes[i].xX + 500
            ) {
                resetLevel()
            }

            if (pipes[i].xX + 500 < 0) {
                val r = Random()
                val value1 = r.nextInt(500)
                val value2 = r.nextInt(500)
                pipes[i].xX = screenWidth + value1 + 1000
                pipes[i].yY = value2 - 250
            }
        }

        if (characterSprite!!.y + 240 < 0) {
            resetLevel()
        }
        if (characterSprite!!.y > screenHeight) {
            resetLevel()
        }
    }


    private fun resetLevel() {
        characterSprite!!.y = 100
        pipe1.xX = 2000
        pipe1.yY = 0
        pipe2.xX = 4500
        pipe2.yY = 200
        pipe3.xX = 3200
        pipe3.yY = 250
    }
}