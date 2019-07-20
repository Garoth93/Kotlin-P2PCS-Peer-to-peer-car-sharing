package com.fourteenrows.p2pcs.objects.boosters

class ActiveBooster(
    override val name: String,
    override val category: Long,
    override val multiplicator: Long,
    override val active: Boolean,
    override val quantity: Long,
    override val description: String,
    val bid: String
) : ActiveBoosterToDB(name, category, multiplicator, description, active, quantity)