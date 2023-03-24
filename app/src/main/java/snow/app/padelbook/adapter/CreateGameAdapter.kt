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
import snow.app.padelbook.common.Utils
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.startaMatch.Data
import snow.app.padelbook.ui.CategoryDetailActivity
import snow.app.padelbook.utils.SafeClickListener
import kotlin.math.roundToInt

class CreateGameAdapter(
    var context: Context, val arrayList: ArrayList<Data>,
    val listener: FavouriteListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ClubTimeSlotSearchAdapter.SelectedTimeSlot {
    private var adapter: ClubTimeSlotSearchAdapter? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val drawable = (holder.itemView.clParent).background as GradientDrawable
        drawable.setStroke(1, ContextCompat.getColor(context, R.color.btn_grey_clr))

        var data = arrayList[position]

        if (data.club_name != null) {
            holder.itemView.title.setText(data.club_name)
        }
        holder.itemView.subHeading.setText(data.city/*+" - "+data.distance.replace(",","").toDouble().roundToInt()+" "+context.getString(R.string.km)*/)
        holder.itemView.tvPricePerPerson.setText("From " + data.price_per_person + "€ per person")
        if (data.club_image != null) {
            Utils.loadImage(data.club_image, context, holder.itemView.imageData)
        }
        if (data.rating != null) {
            holder.itemView.tvRating.setText(data.rating.toFloat().toString())
            holder.itemView.ratingBar.setRating(data.rating.toFloat())
        }
        if (data.price_per_person != null) {
            holder.itemView.tvPricePerPerson.setText(
                context.getString(R.string.from) + " " + data.price_per_person + context.getString(
                    R.string.perperson
                )
            )
        }
        if (data.favourite_status) {
            holder.itemView.imageLike.setImageResource(R.drawable.ic_icon_awesome_heart_1)
        } else {
            holder.itemView.imageLike.setImageResource(R.drawable.ic_heart_img)
        }

        holder.itemView.imageLike.setOnClickListener {
            listener.onIconClick(position, data.club_id)
        }

        holder.itemView.viewData.setOnClickListener {
            context.startActivity(
                Intent(context, CategoryDetailActivity::class.java)
                    .putExtra("club_id", data.club_id)
                    .putExtra("callFrom", "StartAMatch")
            )
        }

        if (data.court_time != null) {
            if (data.court_time.size > 0) {
                adapter = ClubTimeSlotSearchAdapter(context, data.court_time, this, data.club_id)
                holder.itemView.recycleTime.adapter = adapter
                holder.itemView.tvNoAvailability.visibility = View.GONE
                holder.itemView.textSection.visibility = View.VISIBLE
            } else {
                holder.itemView.recycleTime.visibility = View.GONE
                holder.itemView.recycleTime.visibility = View.GONE
                holder.itemView.textSection.visibility = View.GONE
                holder.itemView.tvNoAvailability.visibility = View.VISIBLE
            }
        }

    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun getItemCount() = arrayList.size
    override fun selectedTime(scheduleId: String, clubID: String) {
        context.startActivity(
            Intent(context, CategoryDetailActivity::class.java)
                .putExtra("club_id", clubID)
                .putExtra("scheduleID", scheduleId)
                .putExtra("callFrom", "StartAMatch")
        )
    }
}