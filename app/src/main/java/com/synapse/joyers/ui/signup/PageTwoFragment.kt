package com.synapse.joyers.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.synapse.joyers.R
import com.synapse.joyers.databinding.FragmentPageTwoBinding


class PageTwoFragment : Fragment() {


    private lateinit var binding: FragmentPageTwoBinding
    private var selectedTextView: AppCompatTextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextViewClickListener(binding.classic)
        setTextViewClickListener(binding.leader)
        setTextViewClickListener(binding.provider)
        setTextViewClickListener(binding.proficient)
        setTextViewClickListener(binding.celebrity)

        binding.classicText.text =
            HtmlCompat.fromHtml(getString(R.string.classic_text), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.providerText.text =
            HtmlCompat.fromHtml(getString(R.string.provider_text), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.proficientText.text = HtmlCompat.fromHtml(
            getString(R.string.proficient_text),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.leaderText.text =
            HtmlCompat.fromHtml(getString(R.string.leader_text), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.celebrityText.text = HtmlCompat.fromHtml(
            getString(R.string.celebrity_text),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

    }

    // Method to set click listener and handle selection/deselection
    private fun setTextViewClickListener(textView: AppCompatTextView) {
        textView.setOnClickListener {
            // If it's already selected, do nothing
            if (selectedTextView == textView) return@setOnClickListener

            // Reset previously selected TextView if exists
            selectedTextView?.let { deselectTextView(it) }

            // Select the clicked TextView
            selectTextView(textView)
        }
    }

    // Method to set the selected state for a TextView
    private fun selectTextView(textView: AppCompatTextView) {
        textView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        textView.background = ContextCompat.getDrawable(
            requireActivity(),
            R.drawable.golden_background
        )
        // Mark it as selected
        selectedTextView = textView
    }

    // Method to reset the state of a TextView
    private fun deselectTextView(textView: AppCompatTextView) {
        textView.background = ContextCompat.getDrawable(
            requireActivity(),
            R.drawable.round_light_gray
        )

        textView.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.black
            )
        )
    }


}