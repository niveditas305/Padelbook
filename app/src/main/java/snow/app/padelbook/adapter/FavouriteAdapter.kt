package snow.app.padelbook.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_search.view.*
import snow.app.padelbook.R
import snow.app.padelbook.common.Utils.loadImage
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.clubList.ClubData
import snow.app.padelbook.ui.CategoryDetailActivity
import kotlin.math.roundToInt

class FavouriteAdapter(var context: Context,val arrayList : ArrayList<ClubData>,val listener : FavouriteListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ClubTimeSlotSearchAdapter.SelectedTimeSlot {

    private var adapter : ClubTimeSlotSearchAdapter ?= null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = arrayList[position]
        val drawable = (holder.itemView.clParent).background as GradientDrawable
        drawable.setStroke(1, ContextCompat.getColor(context, R.color.btn_grey_clr))

        holder.itemView.subHeading.setText(data.city)
        holder.itemView.tvPricePerPerson.setText("From "+data.price_per_person+"€ per person")


        if(data.total_rating != null){
            holder.itemView.tvRating.setText(data.total_rating.toFloat().toString())
            holder.itemView.ratingBar.setRating(data.total_rating.toFloat())
        }
        holder.itemView.viewData.setOnClickListener {
            context.startActivity(Intent(context, CategoryDetailActivity::class.java)
                .putExtra("club_id",data.club_id)
                .putExtra("callFrom",""))
        }
        holder.itemView.imageLike.setImageResource(R.drawable.ic_icon_awesome_heart_1)

        holder.itemView.imageLike.setOnClickListener {
            listener.onFavClick(position,data)
        }

        if(data.club_name != null){
            holder.itemView.title.setText(data.club_name)
        }
        if(data.court_time != null){
            adapter = ClubTimeSlotSearchAdapter(context, data.court_time,this,data.club_id)
            holder.itemView.recycleTime.adapter = adapter
        }
        else{
            holder.itemView.recycleTime.visibility = View.GONE
        }
        if(data.club_image != null){
            loadImage(data.club_image,context,holder.itemView.imageData)
        }

    }

    override fun getItemCount() = arrayList.size
    override fun selectedTime(scheduleId: String, clubID: String) {
        context.startActivity(Intent(context, CategoryDetailActivity::class.java)
            .putExtra("club_id",clubID)
            .putExtra("scheduleID",scheduleId)
            .putExtra("callFrom",""))

    }
}