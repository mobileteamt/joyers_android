package com.synapse.joyers.ui.ui.home.adapters

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synapse.joyers.R
import com.synapse.joyers.componants.SegmentedCircleView
import com.synapse.joyers.models.FeedItem
import de.hdodenhof.circleimageview.CircleImageView

class FeedAdapter(private val items: List<FeedItem>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {



    private val borderColors = listOf(
        R.color.border1,
        R.color.border2,
        R.color.border3,
        R.color.border4
    )


    class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfile: CircleImageView = view.findViewById(R.id.imgProfile)
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtCompany: TextView = view.findViewById(R.id.txtCompany)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtLocation: TextView = view.findViewById(R.id.txtLocation)
        val txtContent: TextView = view.findViewById(R.id.txtContent)
        val ratingOne: ImageView = view.findViewById(R.id.imgRating1)
        val ratingTwo: ImageView = view.findViewById(R.id.imgRating2)
        val ratingThree: ImageView = view.findViewById(R.id.imgRating3)
        val ratingFour: ImageView = view.findViewById(R.id.imgRating4)
        val ratingFive: ImageView = view.findViewById(R.id.imgRating5)
        val segmentedCircle: SegmentedCircleView = view.findViewById(R.id.segmentedCircle)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = items[position]
        holder.imgProfile.setImageResource(item.imageResId)
        holder.txtName.text = item.title
        holder.txtCompany.text = item.type
        holder.txtTime.text = item.timestamp
        holder.txtLocation.text = item.location

        val context = holder.itemView.context
        val colorResId = borderColors.random()
        val actualColor = context.getColor(colorResId)

        holder.segmentedCircle.segmentColor = actualColor
        holder.segmentedCircle.segments = item.rating
        holder.segmentedCircle.gapAngle = 8f
        holder.segmentedCircle.randomizeColor = false
        holder.segmentedCircle.gapAngle = 10f

        val textView = holder.txtContent
        val fullText = item.description
        val readMore = " Read All"
        val ellipsis = "..."

        // Show rating from 1 to 5
        val ratingViews = listOf(
            holder.ratingOne,
            holder.ratingTwo,
            holder.ratingThree,
            holder.ratingFour,
            holder.ratingFive
        )

        val rating = item.rating.coerceIn(0, 5)

        for ((index, view) in ratingViews.withIndex()) {
            view.visibility = if (index < rating) View.VISIBLE else View.GONE
        }


        textView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                textView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val width = textView.width
                val paint = textView.paint

                var endIndex = fullText.length
                var finalText = ""
                var layout: android.text.StaticLayout

                do {
                    endIndex -= 1
                    val subText = fullText.substring(0, endIndex).trimEnd()
                    finalText = "$subText$ellipsis$readMore"

                    layout = android.text.StaticLayout.Builder
                        .obtain(finalText, 0, finalText.length, paint, width)
                        .build()
                } while (layout.lineCount > 5 && endIndex > 0)

                if (layout.lineCount <= 5) {
                    val spannable = SpannableString(finalText)

                    val clickableSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            textView.text = fullText
                            textView.movementMethod = null
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.color = Color.parseColor("#F2A100")
                            ds.isUnderlineText = false
                            ds.isFakeBoldText = false
                        }
                    }

                    spannable.setSpan(
                        clickableSpan,
                        finalText.length - readMore.length,
                        finalText.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    textView.text = spannable
                    textView.movementMethod = LinkMovementMethod.getInstance()
                } else {
                    textView.text = fullText
                }
            }
        })
    }


}
