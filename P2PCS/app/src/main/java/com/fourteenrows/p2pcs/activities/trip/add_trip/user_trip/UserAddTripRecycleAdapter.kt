package com.fourteenrows.p2pcs.activities.trip.add_trip.user_trip

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.trip.add_trip.AddTripActivity
import kotlinx.android.synthetic.main.recycler_user_add_trip_item.view.*

class UserAddTripRecycleAdapter(
    private val items: ArrayList<String>,
    private var listenerAdd: IUserAddTripListener
) :
    RecyclerView.Adapter<UserAddTripRecycleAdapter.BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_user_add_trip_item, parent, false)
        return UserTripViewHolderclass(
            view,
            listenerAdd
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        (holder as UserTripViewHolderclass).bind(element)
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class UserTripViewHolderclass(val view: View, val listenerAdd: IUserAddTripListener) :
        BaseViewHolder<String>(view),
        View.OnClickListener {
        private val email = view.userEmail
        private val uid = view.uid
        override fun bind(item: String) {
            email.text = item

            view.setOnClickListener {
                val context = (view.context as Activity)
                val emails = context.intent.getStringArrayListExtra("emails") as ArrayList<String>? ?: arrayListOf()
                emails.add(item)
                val intent = Intent(context, AddTripActivity::class.java)
                val start = context.intent.getStringExtra("start")
                val end = context.intent.getStringExtra("end")
                intent.putExtra("start", start)
                intent.putExtra("end", end)
                intent.putExtra("emails", emails)
                ContextCompat.startActivity(context, intent, null)
            }
        }

        override fun onClick(v: View) {
        }

    }

}