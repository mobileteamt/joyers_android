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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R
import com.synapse.joyers.ui.signup.adapter.TitleNameAdapter

class CustomRoundedDialog : DialogFragment() {

    private lateinit var expandableContent: LinearLayout
    private lateinit var toggleContent: TextView
    private var isExpanded = false

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
        toggleContent = view.findViewById(R.id.tv_toggle_content)
        val closeButton = view.findViewById<ImageButton>(R.id.btn_close_dialog)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_titles)
        val rvSubTitles = view.findViewById<RecyclerView>(R.id.rv_sub_titles)
        val titles = listOf("Baby Joyer", "Couple", "Family", "Friends","Student", "Ghost", "Nick Name", "Pet")
        val subTitles = listOf("Doctoral Student", "Master’s Student", "Bachelor’s Student")

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TitleNameAdapter(titles)

        rvSubTitles.layoutManager = LinearLayoutManager(context)
        rvSubTitles.adapter = TitleNameAdapter(subTitles)

        // Measure the height of the expandable content after layout
        expandableContent.post {
            expandableContent.visibility = View.GONE // Initially hidden
        }

        toggleContent.setOnClickListener {
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
        val targetHeight = getMeasuredHeight(expandableContent)
        val animator = ValueAnimator.ofInt(0, targetHeight)
        animator.addUpdateListener { animation ->
            expandableContent.layoutParams.height = animation.animatedValue as Int
            expandableContent.requestLayout()
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isExpanded = true
                toggleContent.text = context!!.getString(R.string.hide)
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
                toggleContent.text = context!!.getString(R.string.show)
            }
        })
        animator.duration = 300 // Adjust duration as needed
        animator.start()
    }

    private fun getMeasuredHeight(view: View): Int {
        view.measure(
            View.MeasureSpec.makeMeasureSpec((view.parent as View).width, View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        return view.measuredHeight
    }

}