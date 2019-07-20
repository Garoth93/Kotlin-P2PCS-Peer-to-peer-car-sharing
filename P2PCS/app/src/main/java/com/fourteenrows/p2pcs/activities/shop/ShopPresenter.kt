package com.fourteenrows.p2pcs.activities.shop

import android.content.ClipboardManager
import android.content.Context
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.CouponHelper
import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.objects.boosters.ActiveBoosterToDB
import com.fourteenrows.p2pcs.objects.items.Item
import com.fourteenrows.p2pcs.objects.items.ItemAvatar
import com.fourteenrows.p2pcs.objects.items.ItemBooster
import kotlinx.android.synthetic.main.activity_shop.*
import kotlin.reflect.KClass


class ShopPresenter(toView: ShopActivity, private val database: ModelDatabase = ModelFirebase()) :
    IShopPresenter, IShopListener {
    private var view = toView
    private val items = ArrayList<Item>()

    init {
        view.updateToolbar()
        view.startProgressDialog()
        fetchItems()
    }

    private fun fetchItems() {
        items.clear()
        addCoupon()
        database.fetchAvatarPieces()
            .addOnSuccessListener {
                it.forEach { item ->
                    items.add(
                        ItemAvatar(
                            item["name"] as String,
                            item["description"] as String,
                            item["cost"] as Long,
                            true,
                            item.id,
                            item["category"] as Long
                        )
                    )
                }
                getOwnedAvatarParts()
            }
    }

    private fun addCoupon() {
        items.add(Item("Buono amazon", "Buono amazon da 10.00€", 1, true, "c001"))
        items.add(Item("Buono amazon", "Buono amazon da 20.00€", 1, true, "c002"))
    }

    private fun getOwnedAvatarParts() {
        database.getOwnedAvatarParts()
            .addOnSuccessListener { avatarPieces ->
                avatarPieces.forEach { piece ->
                    items.forEach { item ->
                        if (item.iid == piece.id)
                            item.purchasable = false
                    }
                }
                getItemBooster()
            }
    }

    private fun getItemBooster() {
        database.getBoosters()
            .addOnSuccessListener { boosters ->
                boosters.forEach { booster ->
                    items.add(
                        ItemBooster(
                            booster["name"] as String,
                            booster["description"] as String,
                            booster["cost"] as Long,
                            true,
                            booster.id,
                            booster["category"] as Long,
                            booster["multiplicator"] as Long
                        )
                    )
                }
                updateRecycler()
            }
    }

    @Synchronized
    private fun updateRecycler() {
        view.recycleView.adapter = ShopReclyclerAdapter(items, this)
        view.stopProgressDialog()
    }

    override fun purchase(
        iid: String,
        cost: Long,
        javaClass: KClass<Item>
    ) {
        database.getUserDocument()
            .addOnSuccessListener {
                val gaiaCoins = it["gaia_coins"] as Long
                if (gaiaCoins < cost) {
                    view.notEnoughCoins()
                } else {
                    view.makeConfirmationDialog(cost, gaiaCoins, iid, javaClass)
                }
            }

    }

    override fun performPurchase(
        cost: Long,
        userGC: Long,
        iid: String,
        javaClass: KClass<Item>
    ) {
        val item = getItem(iid)
        database.updateField("gaia_coins", userGC - cost)
            .addOnSuccessListener {
                view.updateToolbar()
                    .addOnSuccessListener {
                        view.updateValues()
                    }
                if (iid.length == 4 && iid[0] == 'c') {
                    val coupon: String = CouponHelper().get()
                    view.makeCouponDialog(R.string.amazon_coupon, coupon, R.string.evvivaBooster)
                    (view.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).text = coupon
                } else {
                    view.makeAlertDialog(R.string.success_change, R.string.success)
                }
            }
        if (javaClass == ItemAvatar::class) {
            database.addOwnershipAvatar(iid, item as ItemAvatar)
        } else {
            if (item is ItemBooster)
                addBooster(iid, item)
        }
    }

    private fun addBooster(iid: String, item: Item) {
        database.getUserBoostersWithBID(iid)
            .addOnSuccessListener {
                val itemBooster = item as ItemBooster
                if (it.data == null) {//Add or increment owned itemes
                    database.addOwnershipBooster(
                        iid,
                        ActiveBoosterToDB(
                            itemBooster.name,
                            itemBooster.category,
                            itemBooster.multiplicator,
                            itemBooster.description,
                            false,
                            1L
                        )
                    )
                } else {
                    increaseQuantity(iid)
                }
            }
    }

    private fun increaseQuantity(bid: String) {
        database.getUserBoostersWithBID(bid)
            .addOnSuccessListener {
                val map = it.data
                val quantity = map!!["quantity"] as Long + 1
                database.updateBoosterQuantity(bid, quantity)
            }
    }

    private fun getItem(iid: String): Item {
        for (i in 0 until items.size) {
            if (items[i].iid == iid) {
                return items[i]
            }
        }
        throw Exception("Errore")
    }

}