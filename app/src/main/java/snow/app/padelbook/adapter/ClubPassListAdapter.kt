package snow.app.padelbook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_club_pass.view.*
import kotlinx.android.synthetic.main.items_search.view.*
import kotlinx.android.synthetic.main.toolbar_icon.view.*
import snow.app.padelbook.R
import snow.app.padelbook.common.Utils
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.cardlistnew.CardsListResponseNew
import snow.app.padelbook.network.responses.clubpassesistresponse.Data

import snow.app.padelbook.ui.CategoryDetailActivity
import snow.app.padelbook.utils.SafeClickListener
import kotlin.math.roundToInt

class ClubPassListAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listItems = ArrayList<Data>()
    var onItemClick: ((Data) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_club_pass, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item: Data = listItems.get(position)
        holder.itemView.tvBookings.setText(item.bookingsWeekly +" "+ context.resources.getString(R.string.bookings_per_week))
        holder.itemView.tvClubName.setText(item.clubName )
        holder.itemView.tvExpiryDate.setText(context.getString(R.string.exp)+ " "+item.expiryDate )
        Glide.with(context)
            .load(item.imageFile)             // image url
            .placeholder(R.drawable.ic_user) // any placeholder to load at start
            .error(R.drawable.ic_user)  // any image in case of error
            .into(holder.itemView.circularImg)

        holder.itemView.rvParent.setOnClickListener {
            if (item.isActive == 1) {
                onItemClick?.invoke(item)
            }
        } 
        holder.itemView.mainViewClick.setOnClickListener {
            onItemClick?.invoke(item)
        }
        holder.itemView.tvClubName.setOnClickListener {
            onItemClick?.invoke(item)
        }
        holder.itemView.llName.setOnClickListener {
            onItemClick?.invoke(item)
        }
        holder.itemView.circularImg.setOnClickListener {
            onItemClick?.invoke(item)
        }
        if (item.isActive == 0){
            holder.itemView.tvExprired.visibility = View.VISIBLE
            holder.itemView.tvExpiryDate.visibility = View.GONE
            holder.itemView.rvParent.setBackgroundColor(ContextCompat.getColor(context,R.color.ed_border_clr))
        }else {
            holder.itemView.tvExprired.visibility = View.GONE
            holder.itemView.tvExpiryDate.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: ArrayList<Data>) {
        listItems = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = listItems.size
}