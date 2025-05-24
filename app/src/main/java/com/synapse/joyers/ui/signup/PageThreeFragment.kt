package com.synapse.joyers.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.synapse.joyers.apiData.ApiResultHandler
import com.synapse.joyers.apiData.response.Title
import com.synapse.joyers.apiData.response.TitlesApiResponse
import com.synapse.joyers.databinding.FragmentPageThreeBinding
import com.synapse.joyers.localstore.PreferencesManager
import com.synapse.joyers.ui.auth.SplashVideoActivity
import com.synapse.joyers.utils.showProgress
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class PageThreeFragment : Fragment() {

    private lateinit var binding: FragmentPageThreeBinding
    private val signupViewModel: SignupViewModel by inject()
    private lateinit var titles: List<Title>
    private val preferencesManager: PreferencesManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val token = preferencesManager.getAccessToken()
            if (token != null) {
                signupViewModel.getTitles(token)
            }
        }


        observeApiPostData()

        binding.title.setOnClickListener {
            val dialog = CustomRoundedDialog(requireActivity(),titles)
            dialog.show(requireActivity().supportFragmentManager, "CustomRoundedDialogTag")
        }

        binding.next.setOnClickListener {
            val intent = Intent(requireContext(), SplashVideoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeApiPostData() {
        signupViewModel.titlesApiResponse.observe(requireActivity()) { response ->
            val apiResultHandler = ApiResultHandler<TitlesApiResponse>(
                requireActivity(),
                onLoading = { showProgress(binding.progressBar.circularProgress, true) },
                onSuccess = {
                    showProgress(binding.progressBar.circularProgress, false)
                    titles = response.data?.data?.data!!

                },
                onFailure = {
                    showProgress(binding.progressBar.circularProgress, false)
                }
            )
            apiResultHandler.handleApiResult(response)
        }
    }


}