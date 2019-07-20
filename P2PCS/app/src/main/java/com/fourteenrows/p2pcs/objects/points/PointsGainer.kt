package com.fourteenrows.p2pcs.objects.points

import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.objects.boosters.ActiveBooster
import com.fourteenrows.p2pcs.objects.boosters.ActiveBoosterContainer
import com.fourteenrows.p2pcs.objects.boosters.BoosterTypes

class PointsGainer(
    private val distance: Long,
    private val participants: Int,
    private val boosters: ActiveBoosterContainer
) {
    var exp: Long = 0
    var gaiaCoins: Long = 0
    private val database = ModelFirebase()

    init {
        exp = ((distance * 0.1) * (1 + (participants + 1) / 10)).toLong()
        gaiaCoins = exp / 10 * 2

        applyBoosters()
    }

    private fun applyBoosters() {
        boosters.forEach { ab ->
            if (ab.active) {
                when (ab.category.toInt()) {
                    BoosterTypes.mExp.ordinal -> exp = exp * ab.multiplicator / 10
                    BoosterTypes.bPassenger.ordinal -> {
                        exp = ((distance * 0.1) * (1 + (participants + 1) / 10) * 1.25).toLong()
                        gaiaCoins = exp / 10 * 2
                    }
                    BoosterTypes.mGaiacoins.ordinal -> gaiaCoins += gaiaCoins * ab.multiplicator / 10
                    BoosterTypes.mAll.ordinal -> {
                        exp = ((distance * 0.1) * (1 + (participants + 1) / 10) * 1.25).toLong()
                        gaiaCoins = exp / 10 * 2
                        gaiaCoins += gaiaCoins * ab.multiplicator / 10
                    }
                }
                deactive(ab)
            }
        }
    }

    private fun deactive(ab: ActiveBooster) {
        if (ab.quantity == 0L) {
            database.removeUserBooster(ab.bid)
        } else {
            database.deactivateBooster(ab.bid)
        }
    }

}