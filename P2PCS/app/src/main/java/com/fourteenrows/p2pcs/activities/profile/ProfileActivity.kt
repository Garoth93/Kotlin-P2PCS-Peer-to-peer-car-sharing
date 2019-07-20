package com.fourteenrows.p2pcs.activities.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.objects.points.LevelParser
import com.fourteenrows.p2pcs.objects.user.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : GeneralActivity(), IProfileView {
    private lateinit var presenter: IProfilePresenter
    private val IMAGE_GALLERY_REQUEST = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initializeDrawer()

        presenter = ProfilePresenter(this)

        btnImmatricolazione.setOnClickListener {
            makeEditTextDialog(
                R.string.name,
                R.string.name_edit,
                InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
                R.string.confirm,
                btnImmatricolazione.text.toString(),
                "name")
        }
        btnTarga.setOnClickListener {
            makeEditTextDialog(
                R.string.surname,
                R.string.surname_edit,
                InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
                R.string.confirm,
                btnTarga.text.toString(),
                "surname")
        }
        changePassword.setOnClickListener {
            presenter.sendReset()
        }

        progressDemo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            val pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val pictureDirectoryPath = pictureDirectory.path
            val data = Uri.parse(pictureDirectoryPath)

            intent.setDataAndType(data, "image/*")
            startActivityForResult(
                intent, IMAGE_GALLERY_REQUEST
            )
        }
        ImageHelper().setAvatar(profile_image, presenter.getUid())
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                val imageUri = data!!.data
                try {
                    val inputStream = contentResolver.openInputStream(imageUri!!)
                    val image = BitmapFactory.decodeStream(inputStream)
                    val drawable = BitmapDrawable(image)
                    profile_image.setImageBitmap(image)

                    FirebaseStorage.getInstance()
                        .getReference(ImageHelper().getNameOf(presenter.getUid()))
                        .putFile(imageUri)
                        .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                            val downloadUrl = taskSnapshot.uploadSessionUri
                            val asd = downloadUrl.toString()
                            Picasso.get().invalidate(ImageHelper().getNameOf(presenter.getUid()))
                            initializeDrawer()
                        })
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.fourteenrows.p2pcs.R.menu.menu_main, menu)
        return true
    }

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
            .setPositiveButton(positive) {_,_ ->
                val input = textField.text.toString()
                presenter.updateData(field, input)
            }
            .setNeutralButton(R.string.cancel) {_,_ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun replaceData(user: User) {
        btnImmatricolazione.text = user.name
        btnTarga.text = user.surname
        btnDisponibilita.text = user.mail
        val level = LevelParser().parse(user.exp)
        textViewlivello.text = level.toString()
        val percentage = LevelParser().getPercentage(user.exp)
        progressDemo.min = 0
        progressDemo.max = 100
        progressDemo.progress = percentage
    }

    override fun refresh() {
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}