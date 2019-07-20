package com.fourteenrows.p2pcs.objects.items

import com.fourteenrows.p2pcs.objects.boosters.BoosterToDatabase

class ItemBooster(
    override val name: String,
    override var description: String,
    override val cost: Long,
    override var purchasable: Boolean,
    override val iid: String,
    val category: Long,
    val multiplicator: Long
) : Item(name, description, cost, purchasable, iid) {

    init {
        this.description = BoosterToDatabase(name, category, multiplicator, description, 0).description
    }

}