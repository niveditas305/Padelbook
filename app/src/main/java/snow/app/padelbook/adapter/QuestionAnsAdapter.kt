package snow.app.padelbook.adapter

import android.content.Context
import android.transition.Fade
import android.transition.Transition
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_qustans.view.*
import snow.app.padelbook.R
import snow.app.padelbook.model.OptionsModel
import androidx.core.view.isVisible
import androidx.transition.Slide
import androidx.transition.TransitionManager
import android.animation.Animator

import android.animation.AnimatorListenerAdapter

import android.animation.ObjectAnimator
import snow.app.padelbook.utils.SafeClickListener


class QuestionAnsAdapter(var context: Context, val list: ArrayList<OptionsModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectedRadio = true

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_qustans, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.rbText.setText(list[position].text)

        if (list[position].isSelected) {
            holder.itemView.rbText.isChecked = true
            holder.itemView.rbText.setTextColor(ContextCompat.getColor(context, R.color.theme_blue))
         //   holder.itemView.tvDes.visibility = View.VISIBLE

            holder.itemView.llParent.slideUp(500)
        } else {
            holder.itemView.rbText.isChecked = false
            holder.itemView.rbText.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.text_hint_color
                )
            )
          // holder.itemView.tvDes.visibility = View.GONE

           }

        holder.itemView.rbText.setSafeOnClickListener {
            setSelected(position)


        }
    }

    fun View.slideUp(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    fun View.slideDown(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    private fun setSelected(position: Int) {
        for (i in 0 until list.size) {
            if (i == position) {
                list[i].isSelected = true
            } else {
                list[i].isSelected = false
            }
            notifyDataSetChanged()
        }


    }
      fun View.fadeIn(durationMillis: Long = 250) {
        this.startAnimation(AlphaAnimation(0F, 1F).apply {
            duration = durationMillis
            fillAfter = true
        })
    }
    fun fadeOutAnimation(viewToFadeOut: View) {
        val fadeOut = ObjectAnimator.ofFloat(viewToFadeOut, "alpha", 1f, 0f)
        fadeOut.duration = 200
        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // We wanna set the view to GONE, after it's fade out. so it actually disappear from the layout & don't take up space.
                viewToFadeOut.visibility = View.GONE
            }
        })
        fadeOut.start()
    }


    fun View.fadeOut(durationMillis: Long = 250) {
        this.startAnimation(AlphaAnimation(1F, 0F).apply {
            duration = durationMillis
            fillAfter = true
        })
    }
    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }
    override fun getItemCount() = list.size
}