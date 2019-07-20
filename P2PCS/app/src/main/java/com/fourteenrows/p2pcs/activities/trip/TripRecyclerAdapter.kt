package com.fourteenrows.p2pcs.activities.trip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.objects.trips.Trip
import kotlinx.android.synthetic.main.recycler_trip_item.view.*

class TripRecyclerAdapter(
    private val items: ArrayList<Trip>,
    private val listener: ITripListener
) :
    RecyclerView.Adapter<TripRecyclerAdapter.BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_trip_item, parent, false)
        return TripViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        (holder as TripViewHolder).bind(element)
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class TripViewHolder(val view: View, val listener: ITripListener) :
        BaseViewHolder<Trip>(view),
        View.OnClickListener {
        val viewForeground: RelativeLayout = view.findViewById(R.id.foreground)

        private val start: TextView = view.start
        private val addressStart: TextView = view.addressStart
        private val end: TextView = view.end
        private val addressEnd: TextView = view.addressEnd
        private val info: TextView = view.info
        private val date: TextView = view.date
        val tid = view.tid!!
        private val carImage = view.imageCarTrip

        override fun bind(item: Trip) {
            val startIndex = item.start.indexOf(",")
            val startMain = item.start.substring(0, startIndex)
            val startSecondary = item.start.substring(startIndex + 1, item.start.length)
            val endIndex = item.end.indexOf(",")
            val endMain = item.end.substring(0, endIndex)
            val endSecondary = item.end.substring(endIndex + 1, item.end.length)

            tid.text = item.tid
            start.text = startMain
            addressStart.text = view.resources.getString(R.string.from, startSecondary)
            end.text = endMain
            addressEnd.text = view.resources.getString(R.string.to, endSecondary)
            date.text = item.date
            info.text = view.resources.getString(R.string.trip_info, item.distance_string, item.duration_text)
            ImageHelper().setCarImage(carImage, item.cid)
        }

        override fun onClick(view: View) {}
    }
}

