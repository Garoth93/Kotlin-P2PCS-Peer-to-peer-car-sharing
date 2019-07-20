package com.fourteenrows.p2pcs.model.utility

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.activities.reservation.ReservationRecyclerAdapter
import com.fourteenrows.p2pcs.activities.trip.TripRecyclerAdapter

class RecyclerItemTouchHelper(dragDirs: Int, swipeDirs: Int, private val listener: RecyclerItemTouchHelperListener) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    private fun checkIsCastable(viewHolder: RecyclerView.ViewHolder): Boolean {
        if ((viewHolder is ReservationRecyclerAdapter.ActiveReservationViewHolder
                    && viewHolder.pending.visibility == View.GONE) ||
            viewHolder is ReservationRecyclerAdapter.PastReservationViewHolder ||
            viewHolder is TripRecyclerAdapter.TripViewHolder
        ) {
            return true
        }
        return false
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null && checkIsCastable(viewHolder)) {
            val foregroundView = when (viewHolder) {
                is TripRecyclerAdapter.TripViewHolder -> viewHolder.viewForeground
                is ReservationRecyclerAdapter.ActiveReservationViewHolder -> viewHolder.viewForeground
                else -> (viewHolder as ReservationRecyclerAdapter.PastReservationViewHolder).viewForeground
            }
            getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (checkIsCastable(viewHolder)) {
            val foregroundView = when (viewHolder) {
                is TripRecyclerAdapter.TripViewHolder -> viewHolder.viewForeground
                is ReservationRecyclerAdapter.ActiveReservationViewHolder -> viewHolder.viewForeground
                else -> (viewHolder as ReservationRecyclerAdapter.PastReservationViewHolder).viewForeground
            }
            getDefaultUIUtil().onDrawOver(
                c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive
            )
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if (checkIsCastable(viewHolder)) {
            val foregroundView = when (viewHolder) {
                is TripRecyclerAdapter.TripViewHolder -> viewHolder.viewForeground
                is ReservationRecyclerAdapter.ActiveReservationViewHolder -> viewHolder.viewForeground
                else -> (viewHolder as ReservationRecyclerAdapter.PastReservationViewHolder).viewForeground
            }
            getDefaultUIUtil().clearView(foregroundView)
        }
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (checkIsCastable(viewHolder)) {
            val foregroundView = when (viewHolder) {
                is TripRecyclerAdapter.TripViewHolder -> viewHolder.viewForeground
                is ReservationRecyclerAdapter.ActiveReservationViewHolder -> viewHolder.viewForeground
                else -> (viewHolder as ReservationRecyclerAdapter.PastReservationViewHolder).viewForeground
            }
            getDefaultUIUtil().onDraw(
                c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive
            )
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}