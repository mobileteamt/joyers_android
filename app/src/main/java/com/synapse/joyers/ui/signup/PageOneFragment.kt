package com.synapse.joyers.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.synapse.joyers.R
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.request.UpdateUserRequest
import com.synapse.joyers.apiData.response.BaseResponse
import com.synapse.joyers.apiData.response.UploadResponse
import com.synapse.joyers.databinding.FragmentPageOneBinding
import com.synapse.joyers.localstore.PreferencesManager
import com.synapse.joyers.ui.auth.SplashVideoActivity
import com.synapse.joyers.utils.ImagePickerBottomSheet
import com.synapse.joyers.utils.showCCPDialog
import com.synapse.joyers.utils.showProgress
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class PageOneFragment : Fragment() {


    lateinit var binding: FragmentPageOneBinding
    private var isHeader: Boolean = false
    private var isImage: Boolean = false
    private var isName: Boolean = false
    private var isCountry: Boolean = false

    private val signupViewModel: SignupViewModel by inject()
    private val preferencesManager: PreferencesManager by inject()
    private var imagePath: String? = null
    private var headerPath: String? = null

    enum class SelectedImageType {
        PROFILE_PICTURE,
        BACKGROUND_PICTURE
    }

    private var selectedImageType: SelectedImageType? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeApiPostData()

        binding.profileImage.setOnClickListener {
            selectedImageType = SelectedImageType.PROFILE_PICTURE

            val bottomSheet = ImagePickerBottomSheet(
                activity = requireActivity() as IdentityActivity,
                allowMultipleSelection = false, // or false
                onImagesPicked = { uris ->
                    isImage = true
                    binding.linearProfileImage.visibility = View.GONE
                    binding.profileImage.setImageURI(uris[0])
                    validate()
                    lifecycleScope.launch {
                        val token = preferencesManager.getAccessToken()
                        if (token != null) {
                            signupViewModel.uploadImage(uris[0], token)
                        }
                    }

                },
                onCameraImagePicked = { uri ->
                    isImage = true
                    binding.linearProfileImage.visibility = View.GONE
                    binding.profileImage.setImageURI(uri)
                    validate()
                    lifecycleScope.launch {
                        val token = preferencesManager.getAccessToken()
                        if (token != null) {
                            signupViewModel.uploadImage(uri, token)

                        }
                    }

                }
            )
            bottomSheet.show(requireActivity().supportFragmentManager, "ImagePickerBottomSheet")
        }

        binding.headerImagePicker.setOnClickListener {
            selectedImageType = SelectedImageType.BACKGROUND_PICTURE

            val bottomSheet = ImagePickerBottomSheet(
                activity = requireActivity() as IdentityActivity,
                allowMultipleSelection = false, // or false
                onImagesPicked = { uris ->
                    isHeader = true
                    binding.headerImage.setImageURI(uris[0])
                    validate()
                    lifecycleScope.launch {
                        val token = preferencesManager.getAccessToken()
                        if (token != null) {
                            signupViewModel.uploadImage(uris[0], token)

                        }
                    }

                },
                onCameraImagePicked = { uri ->
                    isHeader = true
                    binding.headerImage.setImageURI(uri)
                    validate()
                    lifecycleScope.launch {
                        val token = preferencesManager.getAccessToken()
                        if (token != null) {
                            signupViewModel.uploadImage(uri, token)

                        }
                    }

                }
            )
            bottomSheet.show(requireActivity().supportFragmentManager, "ImagePickerBottomSheet")
        }

        binding.headerClose.setOnClickListener {
            isHeader = false
            validate()
            binding.headerImage.setImageDrawable(null)
        }

        binding.closeImage.setOnClickListener {
            isImage = false
            validate()
            binding.linearProfileImage.visibility = View.VISIBLE
            binding.profileImage.setImageDrawable(null)
        }

        binding.countryName.setOnClickListener {
            showCCPDialog(requireActivity()) { code, name ->
                isCountry = true
                binding.countryName.text = name
                binding.location.text = resources.getString(R.string.joyer_location_only)
                binding.countryName.setTypeface(null, android.graphics.Typeface.BOLD)
                validate()

            }
        }

        binding.username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val remaining = 45 - (s?.length ?: 0)
                binding.textLength.text = remaining.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    binding.username.setTypeface(null, android.graphics.Typeface.BOLD)
                    binding.name.text = resources.getString(R.string.name_only)
                    isName = true
                    validate()
                }else{
                    binding.username.setTypeface(null, android.graphics.Typeface.NORMAL)
                    binding.name.text = resources.getString(R.string.name)
                    isName = false
                    validate()
                }
            }
        })

        binding.skip.setOnClickListener {
            lifecycleScope.launch {
                val token = preferencesManager.getAccessToken()
                val userID = preferencesManager.getUserId()
                if (token != null && userID != null) {
                    signupViewModel.setPageNumber(token = token, userId = userID,
                        updateRequest = UpdateUserRequest(is_skipped = true)
                    )
                }
            }

        }

        binding.next.setOnClickListener {
            lifecycleScope.launch {
                val token = preferencesManager.getAccessToken()
                val userID = preferencesManager.getUserId()
                if (token != null && userID != null) {
                    signupViewModel.updateUser(
                        token, userID, UpdateUserRequest(
                            firstname = binding.username.text.toString(),
                            location = binding.countryName.text.toString(),
                            profilePictureId = imagePath,
                            backgroundPictureId = headerPath,
                             is_identity_verified = true
                        )
                    )

                }
            }
        }
    }

    fun validate() {
        if (isName && isHeader && isImage && isCountry) {
            binding.skip.visibility = View.GONE
            binding.next.visibility = View.VISIBLE
        } else {
            binding.skip.visibility = View.VISIBLE
            binding.next.visibility = View.GONE
        }

        if (isHeader) {
            binding.headerImagePicker.visibility = View.GONE
            binding.headerClose.visibility = View.VISIBLE
        } else {
            binding.headerImagePicker.visibility = View.VISIBLE
            binding.headerClose.visibility = View.GONE
        }

        if (isImage) {
            binding.closeImage.visibility = View.VISIBLE
        } else {
            binding.closeImage.visibility = View.GONE
        }
    }

    private fun observeApiPostData() {
        signupViewModel.imageUploadResponse.observe(requireActivity()) { response ->
            val apiResultHandler = ApiResultHandler<UploadResponse>(
                requireActivity(),
                onLoading = { showProgress(binding.progressBar.circularProgress, true) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    when (selectedImageType) {
                        SelectedImageType.PROFILE_PICTURE -> {
                            imagePath = it?.data?._id
                        }

                        SelectedImageType.BACKGROUND_PICTURE -> {
                            headerPath = it?.data?._id
                        }

                        else -> {
                            // Do nothing or show error
                        }

                    }
                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                }
            )
            apiResultHandler.handleApiResult(response)
        }

        signupViewModel.userInfoResponse.observe(requireActivity()) { response ->
            val apiResultHandler = ApiResultHandler<BaseResponse>(
                requireActivity(),
                onLoading = { showProgress(binding.progressBar.circularProgress, true) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    (requireActivity() as IdentityActivity).goToNextPage()
                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                }
            )
            apiResultHandler.handleApiResult(response)
        }

        signupViewModel.setPageResponse.observe(requireActivity()) { response ->
            val apiResultHandler = ApiResultHandler<BaseResponse>(
                requireActivity(),
                onLoading = { showProgress(binding.progressBar.circularProgress, true) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    val intent = Intent(requireActivity(), SplashVideoActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                }
            )
            apiResultHandler.handleApiResult(response)
        }
    }
}