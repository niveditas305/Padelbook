package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_locations.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.searchResponse.searchNearBy.recentHistory.RecentData

class LocationAdapter(var context: Context,val recentList: ArrayList<RecentData>
                      ,val listener : onLocationClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface onLocationClick{
        fun onClick(recentData: RecentData)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_locations, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var recentData = recentList[position]

        if(recentData.address != null){
            holder.itemView.tvTitle.setText(recentData.address)
        }

        holder.itemView.tvTitle.setOnClickListener {
            listener.onClick(recentData)
        }

    }

    override fun getItemCount() = recentList.size

}
