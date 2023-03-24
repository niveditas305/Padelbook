package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_time_slot_bottomsheet.view.*
import snow.app.padelbook.R


class TimeSlotBottomSheetAdapter(
    var context: Context,
    val timeList: ArrayList<String>,
    var time: String, val listener : TimeSlotInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectedPosition = -1
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    interface TimeSlotInterface{
        fun timeSLotClick(time: String, position: Int, itemView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_time_slot_bottomsheet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(selectedPosition == position){
            holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.theme_blue))
        }
        else{
            holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.text_hint_color))
        }
        holder.itemView.timeSlot.setText(timeList[position])

        if(position == 0){
            holder.itemView.sep.visibility = View.GONE
        }
        else{
            holder.itemView.sep.visibility = View.VISIBLE
        }

        holder.itemView.timeSlot.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            listener.timeSLotClick(holder.itemView.timeSlot.text.toString(),position,holder.itemView)
        }

//       if(position == 0){
//           holder.itemView.sep.visibility = View.GONE
//           holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.text_hint_color))
//       }
//        else if(position == 1){
//           holder.itemView.sep.visibility = View.VISIBLE
//           holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.theme_blue))
//        }
//        else{
//           holder.itemView.sep.visibility = View.VISIBLE
//           holder.itemView.timeSlot.setTextColor(ContextCompat.getColor(context,R.color.text_hint_color))
//       }

    }

    override fun getItemCount() = timeList.size
}
