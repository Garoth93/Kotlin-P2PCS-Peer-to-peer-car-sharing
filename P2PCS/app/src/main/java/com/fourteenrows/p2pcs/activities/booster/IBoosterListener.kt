package com.fourteenrows.p2pcs.activities.booster

interface IBoosterListener {
    fun activateBooster(bid: String, quantity: Long)
    fun boosterAlreadyActivated()
    fun showDialog()
}