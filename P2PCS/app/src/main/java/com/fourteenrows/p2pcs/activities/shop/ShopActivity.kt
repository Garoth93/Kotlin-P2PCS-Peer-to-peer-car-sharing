package com.fourteenrows.p2pcs.activities.shop

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.objects.items.Item
import kotlinx.android.synthetic.main.activity_shop.*
import kotlin.reflect.KClass

class ShopActivity : GeneralActivity(), IShopView {

    private lateinit var presenter: IShopPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        initializeDrawer()

        presenter = ShopPresenter(this)
        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun makeConfirmationDialog(
        cost: Long,
        userGC: Long,
        iid: String,
        javaClass: KClass<Item>
    ) {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.confirm_purchase)
            .setMessage(resources.getString(R.string.purchase, cost, userGC))
            .setPositiveButton(R.string.confirm) { _, _ ->
                presenter.performPurchase(cost, userGC, iid, javaClass)
            }
            .setNeutralButton(R.string.cancel) { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun makeCouponDialog(message: Int, coupon: String, title: Int) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(resources.getString(message, coupon))
            .setPositiveButton(R.string.ok) { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    fun notEnoughCoins() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.error)
            .setMessage(R.string.not_enough_coins)
            .setPositiveButton(R.string.confirm) { _, _ ->
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}