package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_contacts.view.*
import snow.app.padelbook.R

class PlayersAdapter(var mcontext: Context,val requestList: ArrayList<String>)  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_contacts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = requestList[position]
         holder.itemView.etFirst.setText(data)
        holder.itemView.etFirst.setBackgroundResource(R.drawable.blue_rect)
        holder.itemView.etFirst.setTextColor(ContextCompat.getColor(mcontext,R.color.white))

    }

    override fun getItemCount() = requestList.size
}