package com.fourteenrows.p2pcs.activities.shop

import com.fourteenrows.p2pcs.objects.items.Item
import kotlin.reflect.KClass

interface IShopPresenter {
    fun performPurchase(cost: Long, userGC: Long, iid: String, javaClass: KClass<Item>)
}