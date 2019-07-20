package com.fourteenrows.p2pcs.activities.shop

import com.fourteenrows.p2pcs.activities.general_activity.IGeneralView

interface IShopView : IGeneralView {
    fun makeCouponDialog(message: Int, coupon: String, title: Int)
}