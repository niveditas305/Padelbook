package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_time_slot.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.clubList.CourtTime

class ClubTimeSlotSearchAdapter(var context: Context, val courtTime: List<CourtTime>,
                                val listener : SelectedTimeSlot,var clubID : String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectedPosition : Int = -1

    interface SelectedTimeSlot{
        fun selectedTime(scheduleId: String,clubID:String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_time_slot, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = courtTime[position]

        if(selectedPosition == position){
            holder.itemView.timeSlot.setBackgroundResource(R.drawable.blue_rect)
            holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.white))
        }
        else{
            holder.itemView.timeSlot.setBackgroundResource(R.drawable.rectangular_box)
            holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.text_hint_color))
        }

        holder.itemView.timeSlot.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            listener.selectedTime(data.start_time,clubID)
        }

        if(data.start_time != null){
            holder.itemView.timeSlot.setText(data.start_time)
        }

    }

    override fun getItemCount() = courtTime.size
}