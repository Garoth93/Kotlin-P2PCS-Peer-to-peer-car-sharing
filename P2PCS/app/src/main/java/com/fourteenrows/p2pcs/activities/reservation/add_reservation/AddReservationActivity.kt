package com.fourteenrows.p2pcs.activities.reservation.add_reservation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.activities.reservation.ReservationActivity
import com.fourteenrows.p2pcs.activities.reservation.add_reservation.add_reservation_vehicle.AddReservationVehicleActivity
import com.fourteenrows.p2pcs.model.utility.ModelDates
import com.fourteenrows.p2pcs.model.utility.ModelUtils
import com.fourteenrows.p2pcs.model.utility.ModelValidator
import com.fourteenrows.p2pcs.objects.reservations.ReservationVehicle
import kotlinx.android.synthetic.main.activity_add_reservation.*
import java.text.SimpleDateFormat
import java.util.*

class AddReservationActivity : GeneralActivity(), IAddReservationView {
    private lateinit var presenter: IAddReservationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reservation)
        initializeDrawer()

        presenter = AddReservationPresenter(this)

        reservationDate.setOnClickListener {
            makeCalendarDialog()
        }

        reservationCar.setOnClickListener {
            val date = reservationMillis.text.toString()
            val zone = "${startTime.text} - ${endTime.text}"
            presenter.checkValues(date, zone)
        }

        reservationConfirm.setOnClickListener {
            presenter.insertReservation()
        }

        startTime.setOnClickListener {
            makeTimeDialog("start")
        }

        endTime.setOnClickListener {
            if (ModelValidator.checkValueIsEmpty(startTime.text.toString())) {
                makeTimeDialog("end")
            } else {
                makeAlertDialog(R.string.start_time_first, R.string.error)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun changeView(message: Int, title: Int) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setOnDismissListener {
                val intent = Intent(this, ReservationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun sendMail(email: String) {
        val array = arrayOf(email)
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, array)
            putExtra(Intent.EXTRA_SUBJECT, "Prenotazione veicolo")
            putExtra(
                Intent.EXTRA_TEXT,
                "Salve, vorrei prendere in prestito ${reservationCar.text} il ${reservationDate.text} dalle ${startTime.text} alle ${endTime.text}. Mi faccia sapere se è d'accordo per accordarci. Cordiali saluti"
            )
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        //.setType("text/plain")
        //.setData(Uri.parse(presenter.getOwner(presenter.getUid()!!)))
        //.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail")
        if (intent.resolveActivity(packageManager) != null) {
            val intent2 = Intent(this, ReservationActivity::class.java)
            intent2.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent2)
            startActivity(intent)
        }
    }

    override fun chooseVehicle(date: String, zone: String) {
        val intent = Intent(this, AddReservationVehicleActivity::class.java)
        intent.putExtra(
            "vehicle",
            ReservationVehicle("", date.toLong(), "", zone, "")
        )
        startActivity(intent)
    }

    override fun makeCalendarDialog() {
        val calendar = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val vehicle = intent.getSerializableExtra("vehicle") as ReservationVehicle?
            if (vehicle != null && calendar.timeInMillis != vehicle.date) {
                reservationCar.setText("")
                vehicle.carId = ""
                vehicle.model = ""
            }
            val dateFormat = "dd MMMM yyyy"
            val toLocale = SimpleDateFormat(dateFormat, Locale.ITALIAN)
            reservationDate.setText(toLocale.format(calendar.time))
            reservationDate.showSoftInputOnFocus = false
            reservationMillis.text = calendar.timeInMillis.toString()
        }

        val datePicker = DatePickerDialog(
            this, dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = ModelDates.nextNDays(1)
        datePicker.datePicker.maxDate = ModelDates.nextNDays(31)
        datePicker.show()
    }

    override fun makeRadioDialog() {
        val listItems = ModelUtils.getTimeSlots()
        val builder = AlertDialog.Builder(this)
            .setTitle("Scegli fascia")
            .setItems(listItems) { _, item ->
                val vehicle = intent.getSerializableExtra("vehicle") as ReservationVehicle?

                if (vehicle != null && listItems[item] != vehicle.zone) {
                    reservationCar.setText("")
                    vehicle.carId = ""
                    vehicle.model = ""
                }
                //reservationZone.setText(listItems[item])
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun makeTimeDialog(target: String) {
        val calendar = Calendar.getInstance()
        val timeListener = TimePickerDialog.OnTimeSetListener { picker, hour, minute ->
            val toHour = hour.toString().padStart(2, '0')
            val toMinute = minute.toString().padStart(2, '0')
            val toText = "$toHour:$toMinute"
            if (target == "start") {
                startTime.setText(toText)
            } else {
                if (toText < startTime.text.toString()) {
                    endTime.setText("")
                    makeAlertDialog(R.string.end_after_time, R.string.error)
                } else {
                    val dateFormat = SimpleDateFormat("kk:mm", Locale.ITALIAN)
                    val date = dateFormat.parse(startTime.text.toString())
                    date.minutes += 15
                    val toDate = dateFormat.format(date)
                    if (toDate.toString() > toText) {
                        makeAlertDialog(R.string.fifteen, R.string.error)
                    } else {
                        endTime.setText(toText)
                    }
                }
            }
        }
        val timePicker = TimePickerDialog(
            this,
            timeListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    override fun refillContent(vehicle: ReservationVehicle) {
        reservationCar.setText(vehicle.model)
        reservationMillis.text = vehicle.date.toString()

        val dateFormat = "dd MMMM yyyy"
        val toLocale = SimpleDateFormat(dateFormat, Locale.ITALIAN)
        reservationDate.setText(toLocale.format(vehicle.date))
        val zone = vehicle.zone.split(" - ")
        startTime.setText(zone[0])
        endTime.setText(zone[1])
    }

    override fun refresh() {
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}