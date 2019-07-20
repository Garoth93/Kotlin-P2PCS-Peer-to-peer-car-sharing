package com.fourteenrows.p2pcs.activities.car.add_car.location_car

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.car.CarActivity
import com.fourteenrows.p2pcs.activities.car.TempVehicle2
import com.fourteenrows.p2pcs.activities.car.add_car.AddCarActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.objects.cars.CarLocation
import com.fourteenrows.p2pcs.objects.cars.TempVehicle
import kotlinx.android.synthetic.main.recycler_trip_location.view.*

class LocationCarRecyclerAdapter(
    private val items: ArrayList<CarLocation>,
    private var listener: ILocationCarListener
) :
    RecyclerView.Adapter<LocationCarRecyclerAdapter.BaseViewHolder<*>>() {

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
        abstract fun bind(item: CarLocation)
    }

    class LocationTripHolderClass(val view: View, val listener: ILocationCarListener) :
        BaseViewHolder<String>(view),
        View.OnClickListener {
        private val main = view.tripMain
        private val second = view.tripSecondary

        override fun bind(item: CarLocation) {
            main.text = item.main
            second.text = item.secondary
            view.setOnClickListener {
                val context = (view.context) as Activity
                val auto = context.intent.getSerializableExtra("auto") as TempVehicle?
                var intent: Intent?
                if (auto != null) {
                    intent = Intent(context, AddCarActivity::class.java)
                    auto.location = "${main.text}, ${second.text}"
                    intent.putExtra("auto", auto)
                } else {
                    val auto2 = context.intent.getSerializableExtra("autoEdit") as TempVehicle2
                    intent = Intent(context, CarActivity::class.java)
                    auto2.location = "${main.text}, ${second.text}"
                    intent.putExtra("autoEdit", auto2)

                    ModelFirebase().updateCarField(auto2.cid, "location", "${main.text}, ${second.text}")
                }
                startActivity(context, intent, null)
            }
        }

        override fun bind(item: String) {}

        override fun onClick(v: View) {}

    }

}