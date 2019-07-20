package com.fourteenrows.p2pcs.objects.items

open class Item(
    override val name: String,
    override val description: String,
    override val cost: Long,
    open var purchasable: Boolean,
    open val iid: String
) : ItemBase(name, description, cost)