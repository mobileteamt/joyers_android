package com.synapse.joyers.componants

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import kotlin.random.Random

class SegmentedCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var segments: Int = 1
        set(value) {
            field = value.coerceAtLeast(1) // ensure minimum of 1
            invalidate()
        }

    var segmentColor: Int = Color.GRAY
        set(value) {
            field = value
            invalidate()
        }

    var gapAngle: Float = 6f // gap between segments in degrees
        set(value) {
            field = value
            invalidate()
        }

    var randomizeColor: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    var bottomIcon: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }

    var bottomIconSize: Int = 60 // px
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = min(width, height).toFloat()
        val padding = paint.strokeWidth / 2
        val radius = (size / 2) - padding
        val centerX = width / 2f
        val centerY = height / 2f
        val rect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        val fullCircle = 360f

        if (segments == 1) {
            paint.color = if (randomizeColor) {
                Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            } else {
                segmentColor
            }
            canvas.drawArc(rect, 0f, fullCircle, false, paint)

            // Draw icon for 1 segment
            drawBottomIcon(canvas, centerX, centerY, radius)
            return
        }

        val sweepAngle = (fullCircle - (gapAngle * segments)) / segments

        for (i in 0 until segments) {
            paint.color = if (randomizeColor) {
                Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            } else {
                segmentColor
            }

            val startAngle = i * (sweepAngle + gapAngle)
            canvas.drawArc(rect, startAngle, sweepAngle, false, paint)
        }

        // Draw icon after segments
        drawBottomIcon(canvas, centerX, centerY, radius)
    }

    private fun drawBottomIcon(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        bottomIcon?.let { icon ->
            val iconHalfSize = bottomIconSize / 2
            val iconCenterX = centerX
            val iconCenterY = centerY + radius
            val left = (iconCenterX - iconHalfSize).toInt()
            val top = (iconCenterY - iconHalfSize).toInt()
            val right = (iconCenterX + iconHalfSize).toInt()
            val bottom = (iconCenterY + iconHalfSize).toInt()

            icon.setBounds(left, top, right, bottom)
            icon.draw(canvas)
        }
    }
}
