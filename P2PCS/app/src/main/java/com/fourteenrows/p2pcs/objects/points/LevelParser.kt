package com.fourteenrows.p2pcs.objects.points

class LevelParser() {
    private val stairs = arrayOf(0, 100, 400, 1000, 2000, 3400, 6000, 10000)

    fun parse(exp: Long): Int {
        for (i in 0 until stairs.size - 1)
            if (stairs[i + 1] > exp)
                return i + 1
        return stairs.size
    }

    fun getPercentage(exp: Long): Int {
        val level = parse(exp)
        if (level == stairs.size) //Max level
            return 0
        val ub = stairs[level]
        val asd = (exp - stairs[level - 1]) * 100 / (ub - stairs[level - 1])
        return asd.toInt()
    }
}