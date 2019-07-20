package com.fourteenrows.p2pcs.activities.car.add_car

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.car.CarActivity
import com.fourteenrows.p2pcs.activities.car.add_car.location_car.LocationCarActivity
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.objects.cars.TempVehicle
import kotlinx.android.synthetic.main.activity_add_car.*
import java.util.*

class AddCarActivity : GeneralActivity(), IAddCarView {

    private lateinit var presenter: IAddCarPresenter
    private val IMAGE_GALLERY_REQUEST = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)
        initializeDrawer()

        presenter = AddCarPresenter(this)

        val auto = intent.getSerializableExtra("auto") as TempVehicle?
        if (auto != null) {
            vehiclePlate.setText(auto.plate)
            vehicleModel.setText(auto.model)
            vehicleSeats.setText(auto.seats)
            vehicleMillis.text = auto.endDate
            immatricolazione.setText(auto.immatricolazione)
            vehicleLocation.setText(auto.location)
        }

        clearEndDate.setOnClickListener {
            vehicleDate.setText("")
        }

        vehicleConfirm.setOnClickListener {
            val plate = vehiclePlate.text.toString().toUpperCase()
            val model = vehicleModel.text.toString()
            val seats = vehicleSeats.text.toString()
            val location = vehicleLocation.text.toString()
            val endDate = vehicleMillis.text.toString()
            val immatricolazione = immatricolazione.text.toString()

            presenter.checkVehicleValues(plate, model, seats, location, endDate, immatricolazione)
        }

        vehicleDate.setOnClickListener {
            makeCalendarDialog()
        }

        vehicleLocation.setOnClickListener {
            val plate = vehiclePlate.text.toString().toUpperCase()
            val model = vehicleModel.text.toString()
            val seats = vehicleSeats.text.toString()
            val location = vehicleLocation.text.toString()
            val endDate = vehicleMillis.text.toString()
            val immatricolazione = immatricolazione.text.toString()

            val temp = TempVehicle(plate, model, seats, location, endDate, immatricolazione, "")
            val intent = Intent(this, LocationCarActivity::class.java)
            intent.putExtra("auto", temp)
            startActivity(intent)
        }

        imageCar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            val pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val pictureDirectoryPath = pictureDirectory.path
            val data = Uri.parse(pictureDirectoryPath)

            intent.setDataAndType(data, "image/*")
            startActivityForResult(
                intent, IMAGE_GALLERY_REQUEST
            )
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                val imageUri = data!!.data
                try {
                    val inputStream = contentResolver.openInputStream(imageUri!!)
                    val image = BitmapFactory.decodeStream(inputStream)
                    val drawable = BitmapDrawable(image)
                    imageCar.setImageBitmap(image)

                    presenter.setImageUri(imageUri)
                } catch (e: Exception) {
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun makeCalendarDialog() {
        val calendar = Calendar.getInstance()

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            vehicleDate.showSoftInputOnFocus = false
            val date = presenter.formatDate(calendar.time)
            vehicleDate.setText(date)
            vehicleMillis.text = calendar.timeInMillis.toString()
        }

        val datePicker = DatePickerDialog(
            this, dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.datePicker.minDate = presenter.nextNDays(1)
        datePicker.datePicker.maxDate = presenter.nextNDays(31)

        datePicker.show()
    }

    override fun changeView(message: Int, title: Int) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setOnDismissListener {
                val intent = Intent(this, CarActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}