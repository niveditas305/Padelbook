package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import snow.app.padelbook.R
import snow.app.padelbook.model.IntroSlide

class IntroSliderAdapter(private val introSlides: List<IntroSlide>,val context: Context) :
    RecyclerView.Adapter<IntroSliderAdapter.IntroSlideViewHolder>() {

    inner class IntroSlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textTitle = view.findViewById<TextView>(R.id.titleTextView)
        private val textDescription = view.findViewById<TextView>(R.id.subTitleTextView)
        private val imageIcon = view.findViewById<ImageView>(R.id.topImg)
        private val rightImg = view.findViewById<ImageView>(R.id.rightImg)
        private val parentLayout = view.findViewById<ConstraintLayout>(R.id.parentLayout)

        fun bind(introSlide: IntroSlide,position: Int) {
            if (position == 3){
                imageIcon.visibility = View.GONE
                rightImg.visibility = View.GONE
            }else {
                imageIcon.visibility = View.VISIBLE
                rightImg.visibility = View.VISIBLE
            }
            textTitle.text = introSlide.title
            textDescription.text = introSlide.description
            imageIcon.setImageResource(introSlide.mainImage)
            rightImg.setImageResource(introSlide.sideIcon)
           parentLayout.setBackgroundColor(ContextCompat.getColor(context,introSlide.backgroundClr))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
        return IntroSlideViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_one, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {

        holder.bind(introSlides[position],position)
    }
}