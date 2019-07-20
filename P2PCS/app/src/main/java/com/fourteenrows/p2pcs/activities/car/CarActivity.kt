package com.fourteenrows.p2pcs.activities.car

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.car.add_car.AddCarActivity
import com.fourteenrows.p2pcs.activities.car.add_car.location_car.LocationCarActivity
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.model.utility.ModelUtils
import com.fourteenrows.p2pcs.objects.cars.FetchedVehicle
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_car.*
import java.io.Serializable
import java.util.*


class CarActivity : GeneralActivity(), ICarView {
    private lateinit var presenter: ICarPresenter

    val IMAGE_GALLERY_REQUEST = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)
        initializeDrawer()
        mainConstraint.visibility = View.GONE
        mainCard.visibility = View.GONE
        messageNoCar.visibility = View.VISIBLE

        val auto = this.intent.getSerializableExtra("autoEdit") as TempVehicle2?
        if (auto == null) {
            presenter = CarPresenter(this)
        } else {
            presenter = CarPresenter(this, ModelFirebase(), auto.index)
        }

        addVehicle.setOnClickListener {
            val intent = Intent(this, AddCarActivity::class.java)
            startActivity(intent)
        }
        deleteVehicle.setOnClickListener {
            presenter.deleteCar()
        }
        vehiclePrev.setOnClickListener {
            presenter.previousVehicle()
        }
        vehicleNext.setOnClickListener {
            presenter.nextVehicle()
        }
        btnImmatricolazione.setOnClickListener {
            makeEditTextDialog(
                R.string.immatricolazione,
                R.string.moficaLanno,
                InputType.TYPE_CLASS_NUMBER,
                R.string.confirm,
                btnImmatricolazione.text.toString(),
                "immatricolazione"
            )
        }
        model.setOnClickListener {
            makeEditTextDialog(
                R.string.vehicle_model,
                R.string.vehicle_model_edit,
                InputType.TYPE_CLASS_TEXT,
                R.string.confirm,
                model.text.toString(),
                "model"
            )
        }
        btnTarga.setOnClickListener {
            makeEditTextDialog(
                R.string.targa,
                R.string.modificaTarga,
                InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
                R.string.confirm,
                btnTarga.text.toString(),
                "plate"
            )
        }
        btnLocation.setOnClickListener {
            val cid = cid.text.toString()
            val model = model.text.toString()
            val immatricolazione = btnImmatricolazione.text.toString()
            val plate = btnTarga.text.toString()
            val seats = btnNPosti.text.toString()
            val disponibilita = btnDisponibilita.text.toString()
            val avaiable = editVehicleInstant.isChecked

            val temp =
                TempVehicle2(cid, model, immatricolazione, plate, seats, disponibilita, avaiable, presenter.getIndex())
            val intent = Intent(this, LocationCarActivity::class.java)
            intent.putExtra("autoEdit", temp)
            startActivity(intent)
        }
        btnNPosti.setOnClickListener {
            makeEditTextDialog(
                R.string.numeroDiPosti,
                R.string.modificaNumeroDiPosti,
                InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
                R.string.confirm,
                btnNPosti.text.toString(),
                "seats"
            )
        }
        btnDisponibilita.setOnClickListener {
            makeCalendarDialog()
        }
        editVehicleInstant.setOnCheckedChangeListener { _, isChecked ->
            presenter.checkInstantAvailability(isChecked)
        }
        clearEndDate.setOnClickListener {
            presenter.checkFinalAvailability(Date(0))
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

                    FirebaseStorage.getInstance()
                        .getReference(ImageHelper().getNameOf(presenter.getCid()))
                        .putFile(imageUri)
                        .addOnSuccessListener { taskSnapshot ->
                            ImageHelper().setCarImage(imageCar, presenter.getCid())
                        }
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun carPresents() {
        mainConstraint.visibility = View.VISIBLE
        mainCard.visibility = View.VISIBLE
        messageNoCar.visibility = View.GONE
    }

    private fun makeCalendarDialog() {
        val calendar = Calendar.getInstance()

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            btnDisponibilita.showSoftInputOnFocus = false
            val date = presenter.formatDate(calendar.time)
            btnDisponibilita.text = date
            editVehicleMillis.text = calendar.timeInMillis.toString()
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
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            presenter.checkFinalAvailability(GregorianCalendar(year, month, dayOfMonth).time!!)
        }
    }

    @SuppressLint("InflateParams")
    override fun makeEditTextDialog(
        hint: Int,
        title: Int,
        type: Int,
        positive: Int,
        placeholder: String,
        field: String
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_text, null)
        val textField: EditText = dialogView.findViewById(R.id.alertTextField) as EditText
        textField.hint = getString(hint)
        textField.inputType = type
        textField.setText(placeholder)
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton(positive) { _, _ ->
                val input = textField.text.toString()
                when (field) {
                    "immatricolazione" -> presenter.checkImmatricolazione(input)
                    "model" -> presenter.checkModel(input)
                    "plate" -> presenter.checkPlate(input)
                    "location" -> presenter.checkLocation(input)
                    "seats" -> presenter.checkSeats(input)
                }
            }
            .setNeutralButton(R.string.cancel) { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun showErrorCanNotEdit() {
        makeAlertDialog(R.string.vehicle_not_editable, R.string.error)
    }

    override fun showErrorCanNotDelete() {
        makeAlertDialog(R.string.errorNonPuoiCancellare, R.string.error)
    }

    override fun loadVehicle(vehicle: FetchedVehicle) {
        cid.text = vehicle.cid
        model.text = vehicle.model
        btnTarga.text = vehicle.plate
        btnImmatricolazione.text = vehicle.immatricolazione
        btnLocation.text = vehicle.location
        btnNPosti.text = vehicle.seats.toString()
        editVehicleInstant.isChecked = vehicle.instant_availability

        ImageHelper().setCarImage(imageCar, presenter.getCid())

        var img: Drawable
        if (vehicle.editable) {
            img = resources.getDrawable(R.drawable.icon_edit)
            clearEndDate.setImageDrawable(resources.getDrawable(R.drawable.icon_clear))
        } else {
            img = resources.getDrawable(R.color.transparent)
            clearEndDate.setImageDrawable(resources.getDrawable(R.color.transparent))
        }
        img.setBounds(0, 0, 60, 60)
        val btns: Array<AppCompatButton> = arrayOf(btnTarga, btnImmatricolazione, btnNPosti, btnDisponibilita)
        btns.forEach { btn ->
            btn.setCompoundDrawables(null, null, img, null)
            btn.isClickable = vehicle.editable
        }
        clearEndDate.isClickable = vehicle.editable
        editVehicleInstant.isClickable = vehicle.editable
        deleteVehicle.isClickable = vehicle.editable

        if (vehicle.instant_availability) {
            val NULL_DATE = Date(32500915200000)
            if (vehicle.final_availability!! == NULL_DATE) {
                btnDisponibilita.text = getString(R.string.prenotabile)
            } else {
                val date = ModelUtils.formatDate(Date(vehicle.final_availability!!.time))
                btnDisponibilita.text = resources.getString(R.string.not_reservable, date)
            }
        } else {
            btnDisponibilita.text = getString(R.string.nonPrenotabile)
        }
    }
}

class TempVehicle2(
    val cid: String,
    val model: String,
    val immatricolazione: String,
    val plate: String,
    val seats: String,
    val disponibilita: String,
    val avaiable: Boolean,
    val index: Int,
    var location: String = ""
) : Serializable