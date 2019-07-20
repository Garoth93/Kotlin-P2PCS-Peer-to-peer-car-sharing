package com.fourteenrows.p2pcs.activities.reservation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.authentication.login.LoginActivity
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.activities.reservation.add_reservation.AddReservationActivity
import com.fourteenrows.p2pcs.model.utility.RecyclerItemTouchHelper
import kotlinx.android.synthetic.main.activity_reservation.*

class ReservationActivity : GeneralActivity(), IReservationView {
    private lateinit var presenter: IReservationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)
        initializeDrawer()

        presenter = ReservationPresenter(this)

        recycleView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        addReservation.setOnClickListener {
            val intent = Intent(this, AddReservationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun makeAlertDialogDismiss(message: Int, title: Int) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setOnDismissListener {
                (recycleView.adapter as ReservationRecyclerAdapter).notifyDataSetChanged()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun makeConfirmationDialog(rid: String) {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.reservation_delete)
            .setMessage(R.string.confirmation_reservation)
            .setPositiveButton(R.string.confirm) {_,_ ->
                presenter.deleteReservation(rid)
            }
            .setNeutralButton(R.string.cancel) {_,_ -> }
            .setOnDismissListener {
                (recycleView.adapter as ReservationRecyclerAdapter).notifyDataSetChanged()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun makeConfirmationArchivedDialog(rid: String) {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.reservation_archive)
            .setMessage(R.string.confirmation_archiviation_reservation)
            .setPositiveButton(R.string.confirm) { _, _ ->
                presenter.hideReservation(rid)
            }
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setOnDismissListener {
                (recycleView.adapter as ReservationRecyclerAdapter).notifyDataSetChanged()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    override fun resetView() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun setRecyclerAdapter(adapter: ReservationRecyclerAdapter) {
        recycleView.adapter = adapter
        val itemTouchHelperCallback = RecyclerItemTouchHelper(
            0,
            ItemTouchHelper.LEFT,
            presenter as RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
        )
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycleView)
    }
}