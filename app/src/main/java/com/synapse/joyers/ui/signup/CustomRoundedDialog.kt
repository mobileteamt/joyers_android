package com.synapse.joyers.ui.signup

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.synapse.joyers.R

class CustomRoundedDialog : DialogFragment() {

    private lateinit var expandableContent: LinearLayout
    private lateinit var toggleButton: Button
    private var isExpanded = false
    private var initialHeight = 0 // To store the initial height of the expandable content

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Remove default dialog title bar
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_custom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expandableContent = view.findViewById(R.id.expandable_content)
        toggleButton = view.findViewById(R.id.btn_toggle_content)
        val closeButton = view.findViewById<ImageButton>(R.id.btn_close_dialog)

        // Measure the height of the expandable content after layout
        expandableContent.post {
            initialHeight = expandableContent.height
            expandableContent.visibility = View.GONE // Initially hidden
        }

        toggleButton.setOnClickListener {
            toggleContentVisibility()
        }

        closeButton.setOnClickListener {
            dismiss() // Simply dismiss the dialog
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            // Set dialog width to match parent
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            // Set background to transparent to show rounded corners
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun toggleContentVisibility() {
        if (isExpanded) {
            collapseContent()
        } else {
            expandContent()
        }
    }

    private fun expandContent() {
        expandableContent.visibility = View.VISIBLE
        val animator = ValueAnimator.ofInt(0, initialHeight)
        animator.addUpdateListener { animation ->
            expandableContent.layoutParams.height = animation.animatedValue as Int
            expandableContent.requestLayout()
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isExpanded = true
                toggleButton.text = "Hide"
            }
        })
        animator.duration = 300 // Adjust duration as needed
        animator.start()
    }

    private fun collapseContent() {
        val animator = ValueAnimator.ofInt(expandableContent.height, 0)
        animator.addUpdateListener { animation ->
            expandableContent.layoutParams.height = animation.animatedValue as Int
            expandableContent.requestLayout()
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                expandableContent.visibility = View.GONE
                isExpanded = false
                toggleButton.text = "Show More"
            }
        })
        animator.duration = 300 // Adjust duration as needed
        animator.start()
    }
}