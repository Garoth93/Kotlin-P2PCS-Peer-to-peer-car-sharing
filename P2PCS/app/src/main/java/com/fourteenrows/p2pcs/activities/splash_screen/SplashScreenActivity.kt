package com.fourteenrows.p2pcs.activities.splash_screen

import android.os.Bundle
import android.os.Handler
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity : GeneralActivity(), ISplashScreenView {

    private lateinit var presenter: ISplashScreenPresenter
    private val SPLASH_DISPLAY_LENGTH = 2000L
    private val phrases = arrayListOf(
        "Refactorando il codice...",
        "Cambiando gli XML...",
        "Rimuovendo funzionalità...",
        "Fallendo i test...",
        "sudo mv /* /dev/null",
        "Convertendo in Java...",
        "Rimuovendo System32...",
        "sudo rm -Rf /",
        "Orca l'oca",
        "Cambiando capitolato..."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        splashText.text = phrases.shuffled().take(1)[0]
        Handler().postDelayed({
            val presenter = SplashScreenPresenter(this)
        }, SPLASH_DISPLAY_LENGTH)
    }
}