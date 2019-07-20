package com.fourteenrows.p2pcs.activities.leaderboard

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.objects.user.User
import kotlinx.android.synthetic.main.recycler_leaderboard_item.view.*

class LeaderboardRecyclerAdapter(
    private val items: ArrayList<User>,
    private val listener: ILeaderboardListener,
    private val uid: String
) :
    RecyclerView.Adapter<LeaderboardRecyclerAdapter.BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_leaderboard_item, parent, false)
        return ItemViewHolder(view, listener, uid)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        (holder as ItemViewHolder).bind(element)
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class ItemViewHolder(val view: View, val listener: ILeaderboardListener, val uid: String) :
        BaseViewHolder<User>(view),
        View.OnClickListener {
        private val name: TextView = view.name
        private val weekPoints: TextView = view.weekPoints
        private val position: TextView = view.position
        private val uidt: TextView = view.uid
        private val profileImage = view.profile_image

        override fun bind(item: User) {
            name.text = item.name
            weekPoints.text = item.week_points.toString()
            position.text = (this.adapterPosition + 1).toString()
            ModelFirebase().getUidFromEmail(item.mail)
                .addOnSuccessListener {
                    ImageHelper().setAvatar(profileImage, it.documents[0]!!.id)
                    uidt.text = it.documents[0]!!.id
                    if (uidt.text == uid) {
                        view.card.setCardBackgroundColor(Color.parseColor("#A536ee86"))
                    }
                }
        }

        override fun onClick(view: View) {
        }
    }
}

