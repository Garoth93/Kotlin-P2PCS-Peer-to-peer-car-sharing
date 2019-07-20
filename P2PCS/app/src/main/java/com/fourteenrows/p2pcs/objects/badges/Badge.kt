package com.fourteenrows.p2pcs.objects.badges

import java.io.Serializable

data class Badge(
    val name: String,
    val description: String,
    val value: Long,
    val category: Int
) : Serializable