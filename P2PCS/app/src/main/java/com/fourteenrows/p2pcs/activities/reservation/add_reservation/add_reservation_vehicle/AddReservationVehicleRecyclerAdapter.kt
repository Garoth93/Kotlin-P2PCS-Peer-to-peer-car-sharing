package com.fourteenrows.p2pcs.activities.reservation.add_reservation.add_reservation_vehicle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.objects.cars.VehicleObject
import kotlinx.android.synthetic.main.recycler_add_reservation_vehicle_item.view.*
import kotlinx.android.synthetic.main.recycler_reservation_past_reservation_item.view.vehicleModel

class AddReservationVehicleRecyclerAdapter(
    private val items: ArrayList<VehicleObject>,
    private val listener: IAddReservationVehicleListener
) :
    RecyclerView.Adapter<AddReservationVehicleRecyclerAdapter.BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_add_reservation_vehicle_item, parent, false)
        return VehicleObjectViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        (holder as VehicleObjectViewHolder).bind(element)
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class VehicleObjectViewHolder(
        val view: View,
        private val listener: IAddReservationVehicleListener
    ) :
        BaseViewHolder<VehicleObject>(view),
        View.OnClickListener {

        private val image = view.imageCarActive

        override fun bind(item: VehicleObject) {
            view.vehicleModel.text = item.model
            view.vehicleSeats.text = view.resources.getString(R.string.number_seasts, item.seats.toString())
            view.immatricolazione.text = view.resources.getString(R.string.immatricolata, item.immatricolazione)

            if (item.distance > 1) {
                view.vehicleDistance.text = view.resources.getString(R.string.such_distance, item.distance.toString())
            } else {
                view.vehicleDistance.text = view.resources.getString(R.string.much_distance)
            }
            view.carId.text = item.cid
            view.owner.text = item.owner


            ImageHelper().setCarImage(image, item.cid)

            view.setOnClickListener {
                listener.selectVehicle(item.cid, item.model, item.owner)
            }
        }

        override fun onClick(view: View) {}
    }
}