package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_facility.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.profileresponse.Aminity

class ClubFacilityAdapter(var context: Context,val aminity: List<Aminity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_facility, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = aminity[position]

        if(data.title != null){
            holder.itemView.tvFacility.setText(data.title)
        }
    }

    override fun getItemCount() = aminity.size
}
