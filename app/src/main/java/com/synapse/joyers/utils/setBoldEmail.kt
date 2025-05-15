package com.synapse.joyers.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.appcompat.widget.AppCompatTextView

fun setNormalBold(boldText: String, normalText: String): SpannableString {
    val fullText = "$normalText $boldText"
    val spannable = SpannableString(fullText)

    val start = fullText.indexOf(boldText)
    val end = start + boldText.length

    spannable.setSpan(
        StyleSpan(Typeface.BOLD),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannable
}