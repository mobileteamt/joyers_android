package com.synapse.joyers.utils

import android.view.View

fun showProgress(view: View, isShown: Boolean) {
    view.visibility = if (isShown) View.VISIBLE else View.GONE
}