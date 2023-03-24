package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_search_court_type.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.searchResponse.searchCourt.CourtNameData

class CourtTypeBottomSheetAdapter(var context: Context,val courtNameList : ArrayList<CourtNameData>
,val listener : getCourtNameListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface getCourtNameListener{
        fun getName(data: CourtNameData)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_search_court_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = courtNameList[position]

        if(data.name != null) {
            holder.itemView.tvMatches.setText(data.name)
        }
        holder.itemView.tvMatches.setOnClickListener {
            listener.getName(data)
        }

    }

    override fun getItemCount() = courtNameList.size
}
