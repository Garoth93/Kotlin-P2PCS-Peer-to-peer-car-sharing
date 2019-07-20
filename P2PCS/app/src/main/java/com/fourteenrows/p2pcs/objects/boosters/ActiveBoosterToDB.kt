package com.fourteenrows.p2pcs.objects.boosters

open class ActiveBoosterToDB(
    override val name: String,
    override val category: Long,
    override val multiplicator: Long,
    override val description: String,
    open val active: Boolean,
    open val quantity: Long
) : Booster(name, category, multiplicator, description)