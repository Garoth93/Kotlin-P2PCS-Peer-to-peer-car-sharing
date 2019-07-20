package com.fourteenrows.p2pcs.activities.booster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.objects.boosters.ActiveBooster
import kotlinx.android.synthetic.main.recycler_booster_item.view.*

//import kotlinx.android.synthetic.main.recycler_quest_item.description

class BoosterRecyclerAdapter(
    private val items: ArrayList<ActiveBooster>,
    private val listener: IBoosterListener
) :
    RecyclerView.Adapter<BoosterRecyclerAdapter.BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_booster_item, parent, false)
        return BoosterViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        (holder as BoosterViewHolder).bind(element)
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class BoosterViewHolder(val view: View, val listener: IBoosterListener) :
        BaseViewHolder<ActiveBooster>(view), View.OnClickListener {
        private val name: TextView = view.boosterName
        private val description = view.description
        private val bid: TextView = view.bid
        private val quantity: TextView = view.booster_amount
        private val boosterImage = view.boosterImage
        private val active = view.active_or_not

        override fun bind(item: ActiveBooster) {
            name.text = item.name
            bid.text = item.bid
            description.text = item.description
            quantity.text = view.resources.getString(R.string.booster_amount, item.quantity.toString())

            ImageHelper().setCarImage(boosterImage, item.bid)

            if (item.active) {
                active.visibility = View.VISIBLE
            } else {
                active.visibility = View.GONE
            }


            //Picasso.get().load(ImageHelper().getLinkOf(item.bid)).into(boosterImage)
            //val uri = Uri.parse(ImageHelper().getLinkOf(item.bid))
            //boosterImage.setImageURI(uri)

            view.activeBooster.setOnClickListener {
                if (!item.active) {
                    listener.activateBooster(bid.text.toString(), item.quantity - 1)
                    listener.showDialog()
                } else {
                    listener.boosterAlreadyActivated()
                }
            }
        }

        override fun onClick(view: View) {
        }
    }

}