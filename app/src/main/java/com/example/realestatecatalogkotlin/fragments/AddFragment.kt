package com.example.realestatecatalogkotlin.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.navigation.Navigation
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.realestatecatalogkotlin.Helperss.Helpers
import com.example.realestatecatalogkotlin.R
import com.example.realestatecatalogkotlin.presenters.AddPresenter
import com.example.realestatecatalogkotlin.views.AddViewFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.add_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AddFragment : BaseFragment(), AddViewFragment {
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    private val GALLERY = 1
    private val CAMERA = 2
    private var imageBitmap = ""

    @InjectPresenter
    lateinit var addPresenter: AddPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        Helpers.stopTimer()
        Helpers.startTimer()
        image_add_property.setOnClickListener {

            if (checkAndRequestPermissions()) {
                Toast.makeText(
                    activity?.applicationContext,
                    "Права Предоставлены",
                    Toast.LENGTH_LONG
                )
                    .show()
                showPictureDialog()
            }
        }
        btn_add_property.setOnClickListener {
            addPresenter.saveEstatePresenter(
                imageBitmap,
                text_add_address_property.text.toString(),
                text_add_area_property.text.toString(),
                text_add_price_property.text.toString(),
                text_add_quantity_room_property.text.toString(),
                text_add_floor_property.text.toString()
            )
        }
    }

    override fun saveEstate(massage: Int, bundle: Bundle) {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_addFragment_to_viewFragment, bundle)
        }
    }

    override fun showError(
        errorPhoto: Boolean,
        errorAddress: Boolean,
        errorArea: Boolean,
        errorPrice: Boolean,
        errorQuantityRoom: Boolean,
        errorFloor: Boolean
    ) {
        if (errorPhoto) {
            txt_add_property.visibility = View.VISIBLE
            txt_add_property.setText(R.string.error_select_image)
        } else {
            txt_add_property.visibility = View.GONE
        }
        if (errorAddress) {
            text_add_address_property_layout.isErrorEnabled = true
            text_add_address_property_layout.error = "Не заполнен адрес"
        } else {
            text_add_address_property_layout.isErrorEnabled = false
        }
        if (errorArea) {
            text_add_area_property_layout.isErrorEnabled = true
            text_add_area_property_layout.error = "Не указана площадь"
        } else {
            text_add_area_property_layout.isErrorEnabled = false
        }
        if (errorPrice) {
            text_add_price_property_layout.isErrorEnabled = true
            text_add_price_property_layout.error = "Не указана цена"
        } else {
            text_add_price_property_layout.isErrorEnabled = false
        }
        if (errorQuantityRoom) {
            text_add_quantity_room_property_layout.isErrorEnabled = true
            text_add_quantity_room_property_layout.error = "Не указано количество комнат"
        } else {
            text_add_quantity_room_property_layout.isErrorEnabled = false
        }
        if (errorFloor) {
            text_add_floor_property_layout.isErrorEnabled = true
            text_add_floor_property_layout.error = "Не указан этаж"
        } else {
            text_add_floor_property_layout.isErrorEnabled = false
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_edit_property).isVisible = false
        menu.findItem(R.id.action_add_property).isVisible = false
        menu.findItem(R.id.action_exit).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(requireActivity())
        pictureDialog.setTitle(requireActivity().getString(R.string.selectPicture))
        val pictureDialogItem =
            arrayOf(
                requireActivity().getString(R.string.gallery),
                requireActivity().getString(R.string.camera)
            )
        pictureDialog.setItems(pictureDialogItem)
        { _, which ->
            when (which) {
                0 -> selectImageFromGalery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun selectImageFromGalery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        "com.example.realestatecatalogkotlin.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY && data != null) {
            val contentURI = data.data
            try {
                imageBitmap = getImagePath(contentURI!!)
                Picasso.get().load(File(imageBitmap)).into(image_add_property)
                txt_add_property.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == CAMERA) {
            Picasso.get().load(File(imageBitmap)).into(image_add_property)
            txt_add_property.visibility = View.GONE
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = File("/storage/emulated/0/RealEstateCatalog/Pictures/")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            imageBitmap = absolutePath
        }
    }

    private fun getImagePath(uri: Uri): String {
        var path: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    private fun checkAndRequestPermissions(): Boolean {
        val cameraPermission = ActivityCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.CAMERA
        )
        val writePermission = ActivityCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val listPermissionsNeeded = ArrayList<String>()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            requestPermissions(
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    ) {
                        showPictureDialog()
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                requireActivity(),
                                Manifest.permission.CAMERA
                            )
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                requireActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        ) {
                            showDialogOK()
                        } else {
                            openSettings()
                        }
                    }
                }
            }
        }
    }

    private fun showDialogOK() {
        AlertDialog.Builder(requireActivity())
            .setMessage(requireActivity().getString(R.string.PermissionsRequired))
            .setPositiveButton("OK") { _, _ -> checkAndRequestPermissions() }
            .setNegativeButton("Cancel") { _, _ ->
            }.create().show()
    }

    private fun openSettings() {
        val pictureDialog = AlertDialog.Builder(requireActivity())
        pictureDialog.setTitle(requireActivity().getString(R.string.openSettings))
        val pictureDialogItems = arrayOf("Открыть настройки", "Отмена")
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> startActivity(
                    Intent(
                        ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:com.example.realestatecatalogkotlin")
                    )
                )
                1 -> {
                }
            }
        }
        pictureDialog.show()
    }
}