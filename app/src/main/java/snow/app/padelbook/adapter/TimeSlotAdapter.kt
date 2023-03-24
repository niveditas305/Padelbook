package snow.app.padelbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_time_slot.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.courtresponse.ClubTime

class TimeSlotAdapter(
    var context: Context,
    val listener: onClickListener,
    val clubTimeList : ArrayList<ClubTime>,var scheduleID : String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectedPosition : Int = -1

    interface onClickListener{
        fun clickEvent(position: Int, time: String, clubTime: ClubTime)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_time_slot, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(scheduleID.equals(""))
        {
            if(selectedPosition == position) {
                holder.itemView.timeSlot.setBackgroundResource(R.drawable.blue_rect)
                holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.white))
            }
            else {
                holder.itemView.timeSlot.setBackgroundResource(R.drawable.rectangular_box)
                holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.text_hint_color))
            }
        }
        else {
            Log.e("ssss1","-->"+ scheduleID)
            Log.e("ssss2","-->"+ clubTimeList[position].schedule_id)

            if(scheduleID.equals(clubTimeList[position].start_time)) {
                holder.itemView.timeSlot.setText(clubTimeList[position].start_time)
                holder.itemView.timeSlot.setBackgroundResource(R.drawable.blue_rect)
                holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context, R.color.white))
                listener.clickEvent(position,holder.itemView.timeSlot.text.toString(),clubTimeList[position])
            }
        }

//        for(i in 0 until itemCount){
//            if(scheduleID.equals(clubTimeList[i].schedule_id)){
//                holder.itemView.timeSlot.setText(clubTimeList[i].start_time)
//                holder.itemView.timeSlot.setBackgroundResource(R.drawable.blue_rect)
//                holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.white))
//            }
//            break
//            else{
//                holder.itemView.timeSlot.setText(clubTimeList[i].start_time)
//                holder.itemView.timeSlot.setBackgroundResource(R.drawable.rectangular_box)
//                holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.text_hint_color))
//            }
//       }


        if(clubTimeList[position].start_time != null) {
            holder.itemView.timeSlot.setText(clubTimeList[position].start_time)
        }

        holder.itemView.timeSlot.setOnClickListener {
           // scheduleID = clubTimeList[position].schedule_id
            selectedPosition = position
            scheduleID=""
            listener.clickEvent(position,holder.itemView.timeSlot.text.toString(),clubTimeList[position])
            notifyDataSetChanged()
        }

    }

    override fun getItemCount() = clubTimeList.size
}