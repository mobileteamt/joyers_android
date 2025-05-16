package com.synapse.joyers.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.synapse.joyers.R

class ImagePickerBottomSheet(
    private val activity: AppCompatActivity,
    private val allowMultipleSelection: Boolean = false, // customize here
    private val onImagesPicked: (List<Uri>) -> Unit,    // returns 1+ uris
    private val onCameraImagePicked: (Uri) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var singlePicker: ActivityResultLauncher<String>
    private lateinit var multiplePicker: ActivityResultLauncher<Array<String>>
    private lateinit var cameraPicker: ActivityResultLauncher<Uri>
    private var cameraImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        singlePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onImagesPicked(listOf(it)) }
            dismiss()
        }

        multiplePicker = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            if (uris.isNotEmpty()) onImagesPicked(uris)
            dismiss()
        }

        cameraPicker = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) cameraImageUri?.let { onCameraImagePicked(it) }
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_image_picker, container, false)

        val optionSingle = view.findViewById<TextView>(R.id.option_single)
        val optionMultiple = view.findViewById<TextView>(R.id.option_multiple)
        val optionCamera = view.findViewById<TextView>(R.id.option_camera)

        optionCamera.setOnClickListener {
            cameraImageUri = createImageUri(activity)
            cameraImageUri?.let { uri -> cameraPicker.launch(uri) }
        }

        if (allowMultipleSelection) {
            optionMultiple.visibility = View.VISIBLE
            optionSingle.visibility = View.GONE
            optionMultiple.setOnClickListener {
                multiplePicker.launch(arrayOf("image/*"))
            }
        } else {
            optionSingle.visibility = View.VISIBLE
            optionMultiple.visibility = View.GONE
            optionSingle.setOnClickListener {
                singlePicker.launch("image/*")
            }
        }

        return view
    }

    private fun createImageUri(context: Context): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "camera_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    }
}
