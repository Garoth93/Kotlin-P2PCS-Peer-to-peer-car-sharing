package com.fourteenrows.p2pcs.activities.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.objects.reservations.*
import kotlinx.android.synthetic.main.recycler_reservation_active_reservation_item.view.*
import kotlinx.android.synthetic.main.recycler_reservation_active_reservation_item.view.endDate
import kotlinx.android.synthetic.main.recycler_reservation_active_reservation_item.view.hours
import kotlinx.android.synthetic.main.recycler_reservation_active_reservation_item.view.vehicleModel
import kotlinx.android.synthetic.main.recycler_reservation_past_reservation_item.view.*
import kotlinx.android.synthetic.main.recycler_reservation_title_item.view.*

class ReservationRecyclerAdapter(
    private val items: ArrayList<ReservationCardObject>,
    private val listener: IReservationListener
) :
    RecyclerView.Adapter<ReservationRecyclerAdapter.BaseViewHolder<*>>() {
    companion object {
        private const val TYPE_TITLE = 0
        private const val TYPE_ACTIVE_RESERVATION = 1
        private const val TYPE_PAST_RESERVATION = 2
        private const val TYPE_MESSAGE_ERROR = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_MESSAGE_ERROR -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_reservation_title_item, parent, false)
                MessageErrorViewHolder(view)
            }
            TYPE_TITLE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_reservation_title_item, parent, false)
                TitleViewHolder(view)
            }
            TYPE_ACTIVE_RESERVATION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_reservation_active_reservation_item, parent, false)
                ActiveReservationViewHolder(view, listener)
            }
            TYPE_PAST_RESERVATION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_reservation_past_reservation_item, parent, false)
                PastReservationViewHolder(view, listener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        when (holder) {
            is PastReservationViewHolder -> holder.bind(element as PastReservationReservationCardObject)
            is ActiveReservationViewHolder -> holder.bind(element as ActiveReservationReservationCardObject)
            is MessageErrorViewHolder -> holder.bind(element as MessageErrorObjectReservation)
            is TitleViewHolder -> holder.bind(element)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PastReservationReservationCardObject -> TYPE_PAST_RESERVATION
            is ActiveReservationReservationCardObject -> TYPE_ACTIVE_RESERVATION
            is MessageErrorObjectReservation -> TYPE_MESSAGE_ERROR
            else -> TYPE_TITLE
        }
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class TitleViewHolder(val view: View) : BaseViewHolder<ReservationCardObject>(view) {
        private val titleTextView = view.title

        override fun bind(item: ReservationCardObject) {
            if (item.cardType == ReservationCardType.TITLE_ACTIVE_PRENOTATION) {
                titleTextView.setText(R.string.future_reservations)
            } else {
                titleTextView.setText(R.string.past_reservations)
            }
        }
    }

    class MessageErrorViewHolder(val view: View) : BaseViewHolder<ReservationCardObject>(view) {
        private val titleTextView = view.title

        override fun bind(item: ReservationCardObject) {
            titleTextView.setText(R.string.missing_reservations)
        }
    }

    class ActiveReservationViewHolder(val view: View, val listener: IReservationListener) :
        BaseViewHolder<ActiveReservationReservationCardObject>(view),
        View.OnClickListener {
        val viewForeground: RelativeLayout = view.findViewById(R.id.foreground)

        private val vehicleModel: TextView = view.vehicleModel
        private val endDate: TextView = view.endDate
        private val hours: TextView = view.hours
        val rid = view.rida!!
        private val image = view.imageCarActive
        val pending = view.pending_reservation

        override fun bind(item: ActiveReservationReservationCardObject) {
            vehicleModel.text = view.resources.getString(R.string.vehicle, item.vehicleModel)
            endDate.text = view.resources.getString(R.string.date, item.endDate)
            hours.text = view.resources.getString(R.string.time_zone, item.hours)
            rid.text = item.rid
            ImageHelper().setCarImage(image, item.carId)

            if (item.rid == "") {
                pending.visibility = View.VISIBLE
            } else {
                pending.visibility = View.GONE
            }
        }

        override fun onClick(view: View) {}
    }

    class PastReservationViewHolder(val view: View, val listener: IReservationListener) :
        BaseViewHolder<PastReservationReservationCardObject>(view),
        View.OnClickListener {
        val viewForeground: RelativeLayout = view.findViewById(R.id.foreground)

        private val vehicleModel: TextView = view.vehicleModel
        private val endDate: TextView = view.endDate
        private val hours: TextView = view.hours
        private val totalCost = view.totalCost
        val rid = view.ridp!!
        private val image = view.imageCarPast

        override fun bind(item: PastReservationReservationCardObject) {
            vehicleModel.text = view.resources.getString(R.string.vehicle, item.vehicleModel)
            endDate.text = view.resources.getString(R.string.date, item.endDate)
            hours.text = view.resources.getString(R.string.time_zone, item.hours)
            totalCost.text = view.resources.getString(R.string.total_cost, item.totalCost.toString())
            rid.text = item.rid

            ImageHelper().setCarImage(image, item.carId)
        }

        override fun onClick(view: View) {
        }
    }
}