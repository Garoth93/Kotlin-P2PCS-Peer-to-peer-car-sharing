package com.fourteenrows.p2pcs.model

import java.util.*
import java.util.stream.Collectors

class CouponHelper {
    private val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321poiuytrewqlkjhgfdsamnbvcxz"

    private fun generagePart(lnPart: Long) = Random()
        .ints(lnPart, 0, source.length)
        .mapToObj { i -> source[i].toString() }!!
        .collect(Collectors.joining());

    fun get(): String {
        var toReturn = ""
        for (i in 0..2) {
            toReturn += generagePart(4) + "-"
        }
        toReturn += generagePart(4)
        return toReturn
    }
}
