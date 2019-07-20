package com.fourteenrows.p2pcs.activities.shop

import com.fourteenrows.p2pcs.objects.items.Item
import kotlin.reflect.KClass

interface IShopListener {
    fun purchase(iid: String, cost: Long, javaClass: KClass<Item>)
}