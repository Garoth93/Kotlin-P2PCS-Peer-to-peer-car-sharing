package com.fourteenrows.p2pcs.activities.quest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.objects.quests.ActiveQuest
import kotlinx.android.synthetic.main.recycler_quest_item.view.*

class QuestRecyclerAdapter(
    private val items: ArrayList<ActiveQuest>,
    private val listener: IQuestListener
) :
    RecyclerView.Adapter<QuestRecyclerAdapter.BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_quest_item, parent, false)
        return QuestViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = items[position]
        (holder as QuestViewHolder).bind(element)
    }

    override fun getItemCount(): Int = items.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class QuestViewHolder(val view: View, val listener: IQuestListener) :
        BaseViewHolder<ActiveQuest>(view),
        View.OnClickListener {
        private val name = view.name
        private val qid = view.qid
        private val exp = view.exp as AppCompatTextView
        private val gaiaCoin = view.gaiacoin as AppCompatTextView
        private val imgPremio = view.imgPremio
        private val objective = view.fine as AppCompatTextView
        private val progressBar = view.progressbar

        override fun bind(item: ActiveQuest) {
            name.text = view.resources.getString(R.string.quest_objective, item.name)
            qid.text = item.qid
            exp.text = item.exp.toString()
            gaiaCoin.text = item.gaia_coins.toString()
            objective.text =
                view.resources.getString(R.string.quest_status, item.progress.toString(), item.target.toString())
            progressBar.min = 0
            progressBar.max = item.target.toInt()
            progressBar.progress = item.progress.toInt()

            if (item.box.isNotEmpty()) {
                imgPremio.visibility = View.VISIBLE
            }
            view.changeQuest.setOnClickListener {
                listener.changeQuestRequest(qid.text.toString())
            }
        }

        override fun onClick(view: View) {}
    }
}
