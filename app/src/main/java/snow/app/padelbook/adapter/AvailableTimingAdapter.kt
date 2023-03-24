package snow.app.padelbook.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_available_timing.view.*
import snow.app.padelbook.R
import snow.app.padelbook.listener.CourtTimeAdapterInterface
import snow.app.padelbook.network.responses.courtresponse.CategoryData
import snow.app.padelbook.network.responses.courtresponse.CourtData
import snow.app.padelbook.utils.SafeClickListener

class AvailableTimingAdapter(
    var mcontext : Context,var courtData : CourtData,
    val timingList : ArrayList<CategoryData>, val courtTimeAdapterInterface: CourtTimeAdapterInterface)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_available_timing, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var timingData = timingList[position]

        if(selectedPosition == position){
            ClickItemColor(
                holder.itemView.tvSixtySec,
                holder.itemView.tvSixty,
                holder.itemView.tvsixtyprice
            )
        }
        else{
            UnSelectedItemColor(
                holder.itemView.tvSixtySec,
                holder.itemView.tvSixty,
                holder.itemView.tvsixtyprice
            )
        }

        if(timingData.time != null){
            holder.itemView.tvSixty.setText(timingData.time+"'")
        }
        if(timingData.price != null){
            holder.itemView.tvsixtyprice.setText(timingData.price+".00 €")
        }

        holder.itemView.tvSixtySec.setSafeOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            courtTimeAdapterInterface.onTimeClick(mcontext.getString(R.string.court_available),timingData,position,courtData)
        }

    }
    private fun ClickItemColor(tvSixtySec: LinearLayout, tvSixty: TextView, tvsixtyprice: TextView) {
        tvSixtySec.background.setColorFilter(
            ContextCompat.getColor(mcontext,R.color.theme_blue),
            PorterDuff.Mode.SRC_ATOP);
        tvSixty.setTextColor(ContextCompat.getColor(mcontext,R.color.white))
        tvsixtyprice.setTextColor(ContextCompat.getColor(mcontext,R.color.white))
    }

    private fun UnSelectedItemColor(tvSixtySec: LinearLayout, tvSixty: TextView, tvsixtyprice: TextView) {
        tvSixtySec.background.setColorFilter(
            ContextCompat.getColor(mcontext,R.color.white),
            PorterDuff.Mode.SRC_ATOP);
        tvSixty.setTextColor(ContextCompat.getColor(mcontext,R.color.theme_blue))
        tvsixtyprice.setTextColor(ContextCompat.getColor(mcontext,R.color.theme_blue))
    }
    override fun getItemCount() = timingList.size


    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }
}