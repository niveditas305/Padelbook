package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_days.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.profileresponse.Availability

class DaysTimeScheduleAdapter(var context: Context, val availability : List<Availability>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_days, parent, false)
        return ClubFacilityAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = availability[position]

        if(data.club_time != null){
            holder.itemView.tvTime.setText(data.club_time)
        }

        if(data.day_name != null){
            holder.itemView.tvDay.setText(data.day_name)

        }

    }

    override fun getItemCount() = availability.size
}