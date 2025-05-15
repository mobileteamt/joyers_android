package com.synapse.joyers.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.synapse.joyers.R
import com.synapse.joyers.databinding.FragmentPageThreeBinding
import com.synapse.joyers.databinding.FragmentPageTwoBinding
import com.synapse.joyers.ui.auth.JoyersAuthActivity
import com.synapse.joyers.ui.auth.SplashVideoActivity


class PageThreeFragment : Fragment() {


    private lateinit var binding: FragmentPageThreeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.setOnClickListener {
            val dialog = CustomRoundedDialog()
            dialog.show(requireActivity().supportFragmentManager, "CustomRoundedDialogTag")
        }

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), SplashVideoActivity::class.java)
            startActivity(intent)
        }
    }


}