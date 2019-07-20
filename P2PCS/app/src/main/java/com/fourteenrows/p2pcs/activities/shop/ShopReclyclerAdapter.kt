package com.fourteenrows.p2pcs.activities.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.objects.items.Item
//import kotlinx.android.synthetic.main.recycler_quest_item.view.description
import kotlinx.android.synthetic.main.recycler_quest_item.view.name
import kotlinx.android.synthetic.main.recycler_shop_item.view.*

class ShopReclyclerAdapter(
    private val items: ArrayList<Item>,
    private val listener: IShopListener
) :
    RecyclerView.Adapter<ShopReclyclerAdapter.BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_shop_item, parent, false)
        return ItemViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        (holder as ItemViewHolder).bind(element)
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class ItemViewHolder(val view: View, val listener: IShopListener) :
        BaseViewHolder<Item>(view),
        View.OnClickListener {
        private val name: TextView = view.name
        private val description: TextView = view.description
        private val cost: TextView = view.cost
        private val iid: TextView = view.iid
        private val image = view.shopImage

        override fun bind(item: Item) {
            name.text = item.name
            description.text = item.description
            cost.text = item.cost.toString()
            iid.text = item.iid

            ImageHelper().setShopImage(image, item.iid)

            if (!item.purchasable) {
                TODO("Set sold")
            }

            view.shop.setOnClickListener {
                listener.purchase(iid.text.toString(), cost.text.toString().toLong(), Item::class)
            }
        }

        override fun onClick(view: View) {
        }
    }
}

