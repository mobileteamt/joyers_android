package com.synapse.joyers.ui.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.CoilAdapter
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.synapse.joyers.databinding.FragmentPageOneBinding
import com.synapse.joyers.utils.Constants.Companion.PICKER_IMAGE_CODE
import com.synapse.joyers.utils.ImagePickerBottomSheet


class PageOneFragment : Fragment() {


    lateinit var binding: FragmentPageOneBinding

    private lateinit var pickSingleImageLauncher: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.profileImage.setOnClickListener {
            val bottomSheet = ImagePickerBottomSheet(
                activity = requireActivity() as IdentityActivity,
                allowMultipleSelection = false, // or false
                onImagesPicked = { uris ->
                    binding.linearProfileImage.visibility = View.GONE
                    binding.profileImage.setImageURI(uris[0])
                },
                onCameraImagePicked = { uri ->
                    binding.linearProfileImage.visibility = View.GONE
                    binding.profileImage.setImageURI(uri)
                }
            )
            bottomSheet.show(requireActivity().supportFragmentManager, "ImagePickerBottomSheet")
        }
    }


}