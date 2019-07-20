package com.fourteenrows.p2pcs.activities.general_activity

import com.fourteenrows.p2pcs.objects.items.Item

interface IItemListener {
    fun showItem(item: Item)
}