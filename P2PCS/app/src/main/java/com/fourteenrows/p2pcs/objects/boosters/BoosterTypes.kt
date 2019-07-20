package com.fourteenrows.p2pcs.objects.boosters

enum class BoosterTypes {
    mExp,
    mGaiacoins,
    mAll,
    bPassenger,
    none;

    companion object {
        fun of(x: Int): BoosterTypes {
            return when (x) {
                0 -> mExp
                1 -> mGaiacoins
                2 -> mAll
                3 -> bPassenger
                else -> throw Exception("Can't parse passed number")
            }
        }
    }

}