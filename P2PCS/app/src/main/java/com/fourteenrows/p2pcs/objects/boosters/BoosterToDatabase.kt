package com.fourteenrows.p2pcs.objects.boosters

open class BoosterToDatabase(
    override val name: String,
    override val category: Long,
    override val multiplicator: Long,
    override val description: String,
    open val cost: Long
) : Booster(name, category, multiplicator, description)