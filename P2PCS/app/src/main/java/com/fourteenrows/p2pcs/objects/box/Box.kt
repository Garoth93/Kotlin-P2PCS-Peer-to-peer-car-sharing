package com.fourteenrows.p2pcs.objects.box

import java.io.Serializable

data class Box(
    val avatar: ArrayList<String>,
    val booster: ArrayList<String>
) : Serializable