package snow.app.padelbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_available.view.*
import snow.app.padelbook.R
import snow.app.padelbook.listener.CourtTimeAdapterInterface
import snow.app.padelbook.network.responses.courtresponse.CategoryData
import snow.app.padelbook.network.responses.courtresponse.CourtData
import snow.app.padelbook.utils.SafeClickListener

class CourtAdapter(
    var context: Context,
    val courtDataList: ArrayList<CourtData>,
    val courtTimeAdapterInterface: CourtTimeAdapterInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    CourtTimeAdapterInterface {

    private var tempPos: Int = -1
    var isSelectedArrow = true

    var adapter: AvailableTimingAdapter? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_available, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = courtDataList[position]
        if (tempPos == position) {
            Log.e("yess", "yess")
            holder.itemView.availableSec.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_blue
                )
            )
            holder.itemView.imgArrow.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
            holder.itemView.timerSec.visibility = View.VISIBLE

        } else {
            holder.itemView.availableSec.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.itemView.imgArrow.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
            holder.itemView.timerSec.visibility = View.GONE
        }
        if (data.available_status == "Available") {
            holder.itemView.notAvailableSec.visibility = View.GONE
            holder.itemView.availableSec.visibility = View.VISIBLE


            if (data.available_status != null) {
                holder.itemView.tvAvailableText.setText(context.getString(R.string.available))
            }


        } else {

            if (data.available_status != null) {
                holder.itemView.tvAvail.setText(context.getString(R.string.not_available))
            }
            holder.itemView.notAvailableSec.visibility = View.VISIBLE
            holder.itemView.availableSec.visibility = View.GONE
        }
      //  if (data.club_status != null || data.name != null) {

            if(data.court_type.equals("Open"))
            {
                holder.itemView.tvTitle.setText(data.name + " | " +context.getString(R.string.open))
                holder.itemView.tvTitleTwo.setText(data.name + " | " +context.getString(R.string.open))
            }else
            if(data.court_type.equals("Roofed"))
            {
                holder.itemView.tvTitle.setText(data.name + " | " + context.getString(R.string.roofed))
                holder.itemView.tvTitleTwo.setText(data.name + " | " + context.getString(R.string.roofed))
            }else
            if(data.court_type.equals("Indoors"))
            {
                holder.itemView.tvTitle.setText(data.name + " | " +context.getString(R.string.indoors))
                holder.itemView.tvTitleTwo.setText(data.name + " | " +context.getString(R.string.indoors))
            }
            else
            {
                holder.itemView.tvTitle.setText(data.name + " | " + data.court_type)
                holder.itemView.tvTitleTwo.setText(data.name + " | " + data.court_type)
            }

    //    }

        holder.itemView.tvAvailableText.setSafeOnClickListener {
            Log.e("click", "click")
            tempPos = position
            notifyDataSetChanged()

        }
        holder.itemView.availableSec.setSafeOnClickListener {
            Log.e("click", "click")
            holder.itemView.tvAvailableText.performClick()

        }


        adapter = AvailableTimingAdapter(
            context,
            courtDataList[position],
            data.category_type as ArrayList<CategoryData>,
            this
        )
        holder.itemView.availableSection.adapter = adapter

    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun getItemCount() = courtDataList.size

    override fun onTimeClick(
        btnText: String,
        data: CategoryData,
        position: Int,
        courtData: CourtData
    ) {
        courtTimeAdapterInterface.onTimeClick(btnText, data, position, courtData)
    }


}
