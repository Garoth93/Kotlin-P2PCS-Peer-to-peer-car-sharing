package com.fourteenrows.p2pcs.activities.trip.add_trip.location_trip

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.trip.add_trip.AddTripActivity
import com.fourteenrows.p2pcs.objects.trips.TripLocation
import kotlinx.android.synthetic.main.recycler_trip_location.view.*

class LocationTripRecyclerAdapter(
    private val items: ArrayList<TripLocation>,
    private var listener: ILocationTripListener
) :
    RecyclerView.Adapter<LocationTripRecyclerAdapter.BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_trip_location, parent, false)
        return LocationTripHolderClass(
            view,
            listener
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        (holder as LocationTripHolderClass).bind(element)
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
        abstract fun bind(item: TripLocation)
    }

    class LocationTripHolderClass(val view: View, val listener: ILocationTripListener) :
        BaseViewHolder<String>(view),
        View.OnClickListener {
        private val main = view.tripMain
        private val second = view.tripSecondary

        override fun bind(item: TripLocation) {

            main.text = item.main
            second.text = item.secondary
            view.setOnClickListener {
                val context = (view.context) as Activity
                val intent = Intent(context, AddTripActivity::class.java)
                if (item.type == "start") {
                    intent.putExtra("start", "${item.main}, ${item.secondary}")
                    val end = context.intent.getStringExtra("end")
                    if (end != null) {
                        intent.putExtra("end", end)
                    }
                } else {
                    intent.putExtra("end", "${item.main}, ${item.secondary}")
                    val start = context.intent.getStringExtra("start")
                    if (start != null) {
                        intent.putExtra("start", start)
                    }
                }
                val emails = context.intent.getStringArrayListExtra("emails") as ArrayList<String>?
                intent.putExtra("emails", emails)
                startActivity(context, intent, null)
            }
        }

        override fun bind(item: String) {}

        override fun onClick(v: View) {}

    }

}