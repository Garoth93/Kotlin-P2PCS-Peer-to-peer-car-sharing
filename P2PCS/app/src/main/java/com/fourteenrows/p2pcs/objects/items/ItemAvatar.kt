package com.fourteenrows.p2pcs.objects.items

class ItemAvatar(
    override val name: String,
    override val description: String,
    override val cost: Long,
    override var purchasable: Boolean,
    override val iid: String,
    val category: Long
) : Item(name, description, cost, purchasable, iid)