package com.fourteenrows.p2pcs.activities.quest

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import kotlinx.android.synthetic.main.activity_quest.*

class QuestActivity : GeneralActivity(), IQuestView {
    private lateinit var presenter: IQuestPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)
        initializeDrawer()

        presenter = QuestPresenter(this)
        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun setRecyclerAdapter(adapter: QuestRecyclerAdapter) {
        recycleView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun makeConfirmationDialog(cost: Long, userGC: Long, qid: String, level: Long) {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.quest_change)
            .setMessage(resources.getString(R.string.quest_cost_change, cost, userGC))
            .setPositiveButton(R.string.confirm) { _, _ ->
                presenter.performChangeQuest(cost, userGC, qid, level)
            }
            .setNeutralButton(R.string.cancel) { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun makeConfirmationDialogFreeChange(qid: String, level: Long) {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.quest_change)
            .setMessage(R.string.quest_free_change)
            .setPositiveButton(R.string.confirm) { _, _ ->
                presenter.performFreeChangeQuest(qid, level)
            }
            .setNeutralButton(R.string.cancel) { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}