package com.fourteenrows.p2pcs.objects.items

import java.io.Serializable

open class ItemBase(
    open val name: String,
    open val description: String,
    open val cost: Long
) : Serializable