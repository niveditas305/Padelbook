package snow.app.padelbook.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_home_two.view.*
import kotlinx.android.synthetic.main.items_search.view.*
import kotlinx.android.synthetic.main.items_search.view.subHeading
import kotlinx.android.synthetic.main.items_search.view.title
import snow.app.padelbook.R
import snow.app.padelbook.common.Utils.loadImage
import snow.app.padelbook.fragments.SearchFragment
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.clubList.ClubData
import snow.app.padelbook.ui.CategoryDetailActivity
import kotlin.math.roundToInt

class SearchAdapter(
    var context: Context, val arrayClub: ArrayList<ClubData>,
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
        var club_data = arrayClub[position]

        val drawable = (holder.itemView.clParent).background as GradientDrawable
        drawable.setStroke(1, ContextCompat.getColor(context, R.color.btn_grey_clr))

        if (club_data.club_name != null) {
            holder.itemView.title.setText(club_data.club_name)
        }


        holder.itemView.subHeading.setText(club_data.city/*+" - "+club_data.distance.replace(",","").toDouble().roundToInt()+" "+context.getString(R.string.km)?*/)
        holder.itemView.tvPricePerPerson.setText("From " + club_data.price_per_person + "€ per person")
        if (club_data.club_image != null) {
            loadImage(club_data.club_image, context, holder.itemView.imageData)
        }
        if (club_data.rating != null) {
            holder.itemView.tvRating.setText(club_data.rating.toFloat().toString())
            holder.itemView.ratingBar.setRating(club_data.rating.toFloat())
        }
        if (club_data.price_per_person != null) {
            holder.itemView.tvPricePerPerson.setText(
                context.getString(R.string.from) + " " + club_data.price_per_person + context.getString(
                    R.string.perperson
                )
            )
        }
        if (club_data.favourite_status) {
            holder.itemView.imageLike.setImageResource(R.drawable.ic_icon_awesome_heart_1)
        } else {
            holder.itemView.imageLike.setImageResource(R.drawable.ic_heart_img)
        }

        holder.itemView.imageLike.setOnClickListener {
            listener.onIconClick(position, club_data.club_id)
        }

        holder.itemView.viewData.setOnClickListener {
            context.startActivity(
                Intent(context, CategoryDetailActivity::class.java)
                    .putExtra("club_id", club_data.club_id)
                    .putExtra("callFrom", "")
            )
        }


         if (club_data.court_time != null) {
            if (club_data.court_time.size > 0) {
                 adapter =
                    ClubTimeSlotSearchAdapter(
                        context,
                        club_data.court_time,
                        this,
                        club_data.club_id
                    )
                holder.itemView.recycleTime.adapter = adapter
                holder.itemView.tvNoAvailability.visibility = View.GONE
                holder.itemView.textSection.visibility = View.VISIBLE
            } else {
                 holder.itemView.recycleTime.visibility = View.GONE
                holder.itemView.textSection.visibility = View.GONE
                holder.itemView.tvNoAvailability.visibility = View.VISIBLE
            }

        }else{
             holder.itemView.recycleTime.visibility = View.GONE
             holder.itemView.textSection.visibility = View.GONE
             holder.itemView.tvNoAvailability.visibility = View.VISIBLE
         }
    }

    override fun getItemCount() = arrayClub.size

    override fun selectedTime(scheduleId: String, clubID: String) {
        context.startActivity(
            Intent(context, CategoryDetailActivity::class.java)
                .putExtra("club_id", clubID)
                .putExtra("scheduleID", scheduleId)
                .putExtra("scheduleIDDate", SearchFragment.selectedDateFromFilter)
                .putExtra("callFrom", "")
        )

        Log.d("scheduleID", scheduleId)
        Log.d("scheduleIDt", clubID)


    }
}