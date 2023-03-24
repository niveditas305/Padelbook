package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_games.*
import kotlinx.android.synthetic.main.items_home_two.view.*
import snow.app.padelbook.R
import snow.app.padelbook.listener.ClickEvent
import snow.app.padelbook.network.responses.bookingResponse.BookingData
import snow.app.padelbook.network.responses.bookingResponse.Participant
import snow.app.padelbook.utils.SafeClickListener
import java.text.SimpleDateFormat

class BookingAdapter(
    var context: Context, val arrayList: ArrayList<BookingData>,
    val listener: ClickEvent
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_home_two, parent, false)
        return HomeAdapterSecond.ViewHolder(view)
    }

    fun dateFormatWithMonthNameFull(date: String): String {
        val myFormat = SimpleDateFormat("yyyy-MM-dd").parse(date)
        var myDate = SimpleDateFormat("EEEE, dd/MM").format(myFormat)
        return myDate
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var matchData = arrayList[position]


        if (!matchData.match_type.equals("0"))
        {
            holder.itemView.lockImgTwo.visibility=View.VISIBLE
            holder.itemView.lockImgThree.visibility=View.VISIBLE
            holder.itemView.lockImgFour.visibility=View.VISIBLE
            holder.itemView.lockImgFirst.visibility=View.VISIBLE
        }



          //  if (matchData.date != null && matchData.start_time != null && matchData.time != null) {
                var fullDate =
                    dateFormatWithMonthNameFull(matchData.date) + " | " + matchData.start_time + " | " + matchData.time + "'"
        if(fullDate.contains("Mon"))
        {
            holder.itemView.tvTitle.setText(fullDate.replace("Monday",context.getString(R.string.monday)))
        }
        else

            if(fullDate.contains("Tues"))
            {
                holder.itemView.tvTitle.setText(fullDate.replace("Tuesday",context.getString(R.string.tuesday)))
            }
            else
                if(fullDate.contains("Wed"))
                {
                    holder.itemView.tvTitle.setText(fullDate.replace("Wednesday",context.getString(R.string.wednes)))
                }
                else

                    if(fullDate.contains("Thurs"))
                    {
                        holder.itemView.tvTitle.setText(fullDate.replace("Thursday",context.getString(R.string.thurs)))
                    }
                    else

                        if(fullDate.contains("Frid"))
                        {
                            holder.itemView.tvTitle.setText(fullDate.replace("Friday",context.getString(R.string.friday)))
                        }
                        else
                            if(fullDate.contains("Satur"))
                            {
                                holder.itemView.tvTitle.setText(fullDate.replace("Saturday",context.getString(R.string.satu)))
                            }
                            else

                                if(fullDate.contains("Sund"))
                                {
                                    holder.itemView.tvTitle.setText(fullDate.replace("Sunday",context.getString(R.string.sunday)))
                                }

        //  }

        if (matchData.club_name != null || matchData.distance != null) {
            holder.itemView.subHeading.setText(
                matchData.club_name + " - " + matchData.distance.replace(
                    ",",
                    ""
                ) + "km"
            )
        }

        if (matchData.status.equals("confirmed")) {
            holder.itemView.radioBut.setImageResource(R.drawable.ic_radio_checked)
         //   holder.itemView.radioBut.isChecked = true
            holder.itemView.subHeadingTwo.setText(context.getString(R.string.selector_text))
          //  holder.itemView.circularImgThree.setImageResource(R.drawable.profileimg1)
        } else {
            holder.itemView.radioBut.setImageResource(R.drawable.ic_radio_unselected)
            // holder.itemView.radioBut.isChecked = false
            holder.itemView.subHeadingTwo.setText(context.getString(R.string.unselector_text))
        }
        if (matchData.court_feature == "Double") {
            // if teams has four players
            for (i in matchData.participant_list.indices) {
                if (matchData.participant_list[i].player_key == "1") {
                    setDataOnImageSec(
                        holder.itemView.circularImg, holder.itemView.title,
                        holder.itemView.scoreOne, matchData.participant_list[i],
                        holder.itemView.plusIconFirst, holder.itemView.circularImgBorder
                    )
                } else if (matchData.participant_list[i].player_key == "2") {
                    setDataOnImageSec(
                        holder.itemView.circularImgTwo, holder.itemView.titleTwo,
                        holder.itemView.scoreTwo, matchData.participant_list[i],
                        holder.itemView.plusIconSecond, holder.itemView.circularImgTwoBorderTwo
                    )
                } else if (matchData.participant_list[i].player_key == "3") {
                    setDataOnImageSec(
                        holder.itemView.circularImgThree, holder.itemView.titleThree,
                        holder.itemView.scoreThree, matchData.participant_list[i],
                        holder.itemView.plusIconThird, holder.itemView.circularImgThreeBorder
                    )
                } else {
                    setDataOnImageSec(
                        holder.itemView.circularImgFour, holder.itemView.titleFour,
                        holder.itemView.scoreFour, matchData.participant_list[i],
                        holder.itemView.plusIconForth, holder.itemView.circularImgFourBorder
                    )
                }
            }
        } else {
            // if team has two players
            holder.itemView.imgFirstSec.visibility = View.INVISIBLE
            holder.itemView.imgForthSec.visibility = View.INVISIBLE


            for (i in matchData.participant_list.indices) {
                if (matchData.participant_list[i].player_key == "1") {
                    setDataOnImageSec(
                        holder.itemView.circularImgTwo, holder.itemView.titleTwo,
                        holder.itemView.scoreTwo, matchData.participant_list[i],
                        holder.itemView.plusIconSecond, holder.itemView.circularImgTwoBorderTwo
                    )
                } else if (matchData.participant_list[i].player_key == "2") {
                    setDataOnImageSec(
                        holder.itemView.circularImgThree, holder.itemView.titleThree,
                        holder.itemView.scoreThree, matchData.participant_list[i],
                        holder.itemView.plusIconThird, holder.itemView.circularImgThreeBorder
                    )
                }
            }
        }

        holder.itemView.clTop.setOnClickListener {
            listener.clickForBookingDetail(matchData)
        }

//        holder.itemView.circularImg.setSafeOnClickListener{
//            if( holder.itemView.circularImg.drawable == null){
//                listener.clickEvent(matchData.match_id,
//                    matchData.participant_list[position].player_key)
//
//            }
//        }
//        holder.itemView.circularImgTwo.setSafeOnClickListener{
//            if( holder.itemView.circularImgTwo.drawable == null){
//                if(matchData.court_feature == "Double") {
//                    listener.clickEvent(
//                        matchData.match_id,
//                        matchData.participant_list[position].player_key)
//                }
//                else{
//                    listener.clickEvent(
//                        matchData.match_id,
//                        "1"
//                    )
//                }
//
//            }
//        }
//        holder.itemView.circularImgThree.setSafeOnClickListener{
//            if(holder.itemView.circularImgThree.drawable == null){
//                if(matchData.court_feature == "Double") {
//                    listener.clickEvent(
//                        matchData.match_id,
//                        matchData.participant_list[position].player_key)
//                }
//                else{
//                    listener.clickEvent(
//                        matchData.match_id,
//                        "2"
//                    )
//                }
//
//            }
//        }
//        holder.itemView.circularImgFour.setSafeOnClickListener{
//            if(holder.itemView.circularImgFour.drawable == null) {
//                listener.clickEvent(
//                    matchData.match_id,
//                    matchData.participant_list[position].player_key
//                )
//            }
//        }

    }

    private fun setDataOnImageSec(
        circularImg: CircleImageView,
        title: TextView,
        scoreOne: TextView,
        participant: Participant,
        plusIcon: TextView,
        circularBorder: ImageView
    ) {
        if (participant.user_type.equals("1")) {                     // user role
            if (participant.image_file != null) {
                Glide.with(context)
                    .load(participant.image_file)
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)// image url
                    //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                    //.error(R.drawable.profileimg_home)  // any image in case of error
                    .into(circularImg)
                plusIcon.visibility = View.GONE
            } else {
                plusIcon.visibility = View.VISIBLE
                circularImg.setImageDrawable(null)
            }
        } else {                                                   // host role
            if (participant.image_file != null) {
                circularImg.setPadding(4, 4, 4, 4)

                Glide.with(context)
                    .load(participant.image_file)
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)// image url
                    //.placeholder(R.drawable.profileimg_home)  // any placeholder to load at start
                    //.error(R.drawable.profileimg_home)        // any image in case of error
                    .into(circularImg)
                plusIcon.visibility = View.GONE
                circularImg.setBorderColor(ContextCompat.getColor(context, R.color.theme_blue))
                circularBorder.visibility = View.VISIBLE

            } else {
                plusIcon.visibility = View.VISIBLE
                circularImg.setImageDrawable(null)
                circularBorder.visibility = View.GONE
            }
        }


        if (participant.name != null) {
            title.setText(participant.name)
        }
        if (participant.score != null) {
            scoreOne.setText(participant.score)
        }
    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun getItemCount() = arrayList.size

}