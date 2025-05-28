package com.synapse.joyers.utils

import android.content.Context


object DimensionUtils {
    // design width in dp (from Figma or design spec)
    private const val DESIGN_SCREEN_WIDTH_DP = 414f

    /**
     * Convert px (pixels) to dp (density-independent pixels)
     */
    fun pxToDp(context: Context, px: Float): Float {
        val metrics = context.resources.displayMetrics
        return px / (metrics.density)
    }

    /**
     * Convert dp to px
     */
    fun dpToPx(context: Context, dp: Float): Float {
        val metrics = context.resources.displayMetrics
        return dp * metrics.density
    }

    /**
     * Convert px to sp (scale-independent pixels) for font size
     */
    fun pxToSp(context: Context, px: Float): Float {
        val metrics = context.resources.displayMetrics
        return px / metrics.scaledDensity
    }

    /**
     * Convert sp to px
     */
    fun spToPx(context: Context, sp: Float): Float {
        val metrics = context.resources.displayMetrics
        return sp * metrics.scaledDensity
    }

    /**
     * Scale font size (sp) proportionally based on device screen width vs design width
     */
    fun getScaledFontSize(context: Context, designFontSp: Float): Float {
        val metrics = context.resources.displayMetrics

        // Device screen width in dp
        val deviceWidthDp = metrics.widthPixels / metrics.density

        // Scale factor based on width
        val scale = deviceWidthDp / DESIGN_SCREEN_WIDTH_DP

        // Return scaled font size in sp
        return designFontSp * scale
    }
}
