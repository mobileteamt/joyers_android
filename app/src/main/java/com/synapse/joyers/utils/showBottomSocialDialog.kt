package com.synapse.joyers.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import com.synapse.joyers.R
import androidx.core.graphics.drawable.toDrawable

fun showBottomSocialDialog(
    context: Context,
    onFacebookClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
) {
    val dialog = Dialog(context)
    dialog.setContentView(R.layout.bottom_popup)
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    dialog.window?.setGravity(Gravity.BOTTOM)
    dialog.window?.attributes?.windowAnimations = R.style.DialogSlideAnimation

    val fbBtn = dialog.findViewById<LinearLayout>(R.id.btn_facebook)
   val googleBtn = dialog.findViewById<LinearLayout>(R.id.btn_google)

    fbBtn?.setOnClickListener {
        onFacebookClick()
        dialog.dismiss()
    }

    googleBtn?.setOnClickListener {
        onGoogleClick()
        dialog.dismiss()
    }


    dialog.show()
}
