package snow.app.padelbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.items_home_two.view.*
import snow.app.padelbook.R
import snow.app.padelbook.listener.ClickEvent
import snow.app.padelbook.network.responses.homeList.Match
import snow.app.padelbook.network.responses.homeList.Participant
import snow.app.padelbook.utils.SafeClickListener
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class HomeAdapterSecond(var context: Context, val listener : ClickEvent, var type: String,
                        val matchList: ArrayList<Match> ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_home_two, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var matchesData = matchList[position]
//holder.itemView.shimmer_view_container22.startShimmer()

        if(matchesData.date != null && matchesData.start_time != null && matchesData.time != null){
            var fullDate = dateFormatWithMonthNameFull(matchesData.date)+" | "+matchesData.start_time+" | "+matchesData.time+"'"

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
            
        }

        if(matchesData.club_name != null || matchesData.distance != null){
            holder.itemView.subHeading.setText(matchesData.club_name+" - "+matchesData.distance.replace(",","").toDouble().roundToInt()+" "+context.getString(R.string.km))
        }

        if(matchesData.status.equals("confirmed")){
            holder.itemView.radioBut.setImageResource(R.drawable.ic_radio_checked)
            holder.itemView.subHeadingTwo.setText(context.getString(R.string.selector_text))
            holder.itemView.circularImgThree.setImageResource(R.drawable.profileimg1)
        }
        else
        {
            holder.itemView.radioBut.setImageResource(R.drawable.ic_radio_unselected)
            holder.itemView.subHeadingTwo.setText(context.getString(R.string.unselector_text))
        }

        if(matchesData.court_feature.equals("Double")){
                // if teams has four players

            holder.itemView.imgFirstSec.visibility = View.VISIBLE
            holder.itemView.imgForthSec.visibility = View.VISIBLE
            holder.itemView.titleTwo.setText("")
            holder.itemView.scoreTwo.setText("")
            holder.itemView.plusIconSecond.visibility = View.VISIBLE
            holder.itemView.circularImgTwoBorderTwo.visibility = View.GONE
            holder.itemView.circularImgTwo.setImageResource(0)
            holder.itemView.circularImgTwo.setImageDrawable(null)
            holder.itemView.circularImgTwo.setBackgroundResource(R.drawable.stroke_circular)



            holder.itemView.titleFour.setText("")
            holder.itemView.scoreFour.setText("")
            holder.itemView.plusIconForth.visibility = View.VISIBLE
            holder.itemView.circularImgTwoBorderTwo.visibility = View.GONE
            holder.itemView.circularImgFour.setImageResource(0)
            holder.itemView.circularImgFour.setImageDrawable(null)
            holder.itemView.circularImgFour.setBackgroundResource(R.drawable.stroke_circular)


            holder.itemView.titleThree.setText("")
            holder.itemView.scoreThree.setText("")
            holder.itemView.plusIconThird.visibility = View.VISIBLE
            holder.itemView.circularImgTwoBorderTwo.visibility = View.GONE
            holder.itemView.circularImgThree.setImageResource(0)
            holder.itemView.circularImgThree.setImageDrawable(null)
            holder.itemView.circularImgThree.setBackgroundResource(R.drawable.stroke_circular)



            for (i in matchesData.participant_list.indices) {
                if (matchesData.participant_list[i].player_key.equals("1")) {
                    setDataOnImageSec(
                        holder.itemView.circularImg, holder.itemView.title,
                        holder.itemView.scoreOne, matchesData.participant_list[i],
                        holder.itemView.imageWhiteOne,holder.itemView.plusIconFirst,
                        holder.itemView.circularImgBorder
                    )
                } else if (matchesData.participant_list[i].player_key.equals("2")) {
                    setDataOnImageSec(
                        holder.itemView.circularImgTwo,
                        holder.itemView.titleTwo,
                        holder.itemView.scoreTwo,
                        matchesData.participant_list[i],
                        holder.itemView.imageWhiteTwo,
                        holder.itemView.plusIconSecond,
                        holder.itemView.circularImgTwoBorderTwo
                    )
                } else if (matchesData.participant_list[i].player_key.equals("3")) {
                    setDataOnImageSec(
                        holder.itemView.circularImgThree,
                        holder.itemView.titleThree,
                        holder.itemView.scoreThree,
                        matchesData.participant_list[i],
                        holder.itemView.imageWhiteThree,
                        holder.itemView.plusIconThird,
                        holder.itemView.circularImgThreeBorder
                    )
                } else if (matchesData.participant_list[i].player_key.equals("4")){
                    setDataOnImageSec(
                        holder.itemView.circularImgFour,
                        holder.itemView.titleFour,
                        holder.itemView.scoreFour,
                        matchesData.participant_list[i],
                        holder.itemView.imageWhiteFour,
                        holder.itemView.plusIconForth,
                        holder.itemView.circularImgFourBorder
                    )
                }
            }
        }
        else{
            // if team has two players
            holder.itemView.imgFirstSec.visibility = View.INVISIBLE
            holder.itemView.imgForthSec.visibility = View.INVISIBLE
            holder.itemView.titleThree.setText("")
            holder.itemView.scoreThree.setText("")
            holder.itemView.plusIconThird.visibility = View.VISIBLE
            //holder.itemView.circularImgThree.setImageResource(0)
            holder.itemView.circularImgThree.setImageDrawable(null)
            holder.itemView.circularImgThree.setBackgroundResource(R.drawable.stroke_circular)
            for (i in matchesData.participant_list.indices) {
                if (matchesData.participant_list[i].player_key.equals("1")) {

                    holder.itemView.circularImgTwo.setPadding(3, 3, 3, 3)
                    setDataOnImageSec(
                        holder.itemView.circularImgTwo,
                        holder.itemView.titleTwo,
                        holder.itemView.scoreTwo,
                        matchesData.participant_list[i],
                        holder.itemView.imageWhiteTwo,
                        holder.itemView.plusIconSecond,
                        holder.itemView.circularImgTwoBorderTwo
                    )
                } else if(matchesData.participant_list[i].player_key.equals("2")) {
                    setDataOnImageSec(
                        holder.itemView.circularImgThree,
                        holder.itemView.titleThree,
                        holder.itemView.scoreThree,
                        matchesData.participant_list[i],
                        holder.itemView.imageWhiteThree,
                        holder.itemView.plusIconThird,
                        holder.itemView.circularImgThreeBorder
                    )
                }
            }
        }

        holder.itemView.clTop.setOnClickListener {

          //  Toast.makeText(context,"click",Toast.LENGTH_SHORT).show()

            if(holder.itemView.circularImg.drawable == null ){
                if(matchesData.court_feature.equals("Single")) {
                    if(matchesData.participant_list.size<2) {
                        listener.clickEvent(matchesData.match_id, "2", matchesData.court_feature)
                    }
                }else{
                listener.clickEvent(
                    matchesData.match_id,
                    "1",
                    matchesData.court_feature
                )
                }
            }else if(holder.itemView.circularImgTwo.drawable == null){
                if(matchesData.court_feature.equals( "Double")) {
                    listener.clickEvent(matchesData.match_id, "2", matchesData.court_feature)
                }
                else{
                    listener.clickEvent(matchesData.match_id, "1", matchesData.court_feature)
                }

            }else if(holder.itemView.circularImgThree.drawable == null){
                if(matchesData.court_feature.equals("Double")) {
                    listener.clickEvent(matchesData.match_id, "3", matchesData.court_feature)
                }
                else{
                    listener.clickEvent(matchesData.match_id, "2", matchesData.court_feature)
                }

            }else if(holder.itemView.circularImgFour.drawable == null){
                if(matchesData.court_feature.equals("Single")) {
                }else {
                    listener.clickEvent(matchesData.match_id, "4", matchesData.court_feature)
                }
            }
            else{
Log.e("ffff","textt")
            }

        }


        holder.itemView.circularImg.setSafeOnClickListener{
            if( holder.itemView.circularImg.drawable == null){
                  listener.clickEvent(matchesData.match_id,
                      "1",matchesData.court_feature)
                  Log.d("matchID",matchesData.match_id)
              //    Log.d("playerKey",matchesData.participant_list[position].player_key)
            }
        }
        holder.itemView.circularImgTwo.setSafeOnClickListener{
            if( holder.itemView.circularImgTwo.drawable == null){
                if(matchesData.court_feature .equals( "Double")) {
                    listener.clickEvent(matchesData.match_id, "2",matchesData.court_feature)
                    Log.d("matchID", matchesData.match_id)
                 //   Log.d("playerKey", matchesData.participant_list[position].player_key)
                }
                else{
                    listener.clickEvent(matchesData.match_id, "1",matchesData.court_feature)
                }

            }
        }
        holder.itemView.circularImgThree.setSafeOnClickListener{
            if(holder.itemView.circularImgThree.drawable == null){
                if(matchesData.court_feature.equals("Double")) {
                    listener.clickEvent(
                        matchesData.match_id,
                        "3",matchesData.court_feature
                    )
                    Log.d("matchID", matchesData.match_id)
            //        Log.d("playerKey", matchesData.participant_list[position].player_key)
                }
                else{
                    listener.clickEvent(
                        matchesData.match_id,
                        "2",matchesData.court_feature
                    )
                }

            }
        }
        holder.itemView.circularImgFour.setSafeOnClickListener{
            if(holder.itemView.circularImgFour.drawable == null) {
                 listener.clickEvent(
                     matchesData.match_id,
                     "4",matchesData.court_feature)
                 Log.d("matchID",matchesData.match_id)
              //   Log.d("playerKey",matchesData.participant_list[position].player_key)

            }
        }

    }

    private fun setDataOnImageSec(
        circularImg: CircleImageView,
        title: TextView,
        scoreOne: TextView,
        participant: Participant,
        imageWhiteOne: CircleImageView,
        plusIcon: TextView,
        circularImgBorder: ImageView
    )
    {
         if(participant.user_type.equals("1")){                     // user role
             if(participant.image_file != null){
                 if(participant.request_status.equals("0")){
                     Glide.with(context)
                         .load(participant.image_file)
                         .placeholder(R.drawable.ic_user) // any placeholder to load at start
                         .error(R.drawable.ic_user)// image url
                         //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                         //.error(R.drawable.profileimg_home)  // any image in case of error
                         .into(circularImg)
                     imageWhiteOne.visibility = View.VISIBLE
                     plusIcon.visibility = View.GONE
                 }
                 else{
                     Glide.with(context)
                         .load(participant.image_file)
                         .placeholder(R.drawable.ic_user) // any placeholder to load at start
                         .error(R.drawable.ic_user)// image url
                         //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                         //.error(R.drawable.profileimg_home)  // any image in case of error
                         .into(circularImg)
                     imageWhiteOne.visibility = View.GONE
                     plusIcon.visibility = View.GONE
                 }
             }
             else{
                 circularImg.setBackgroundResource(R.drawable.stroke_circular)
                 circularImg.setImageDrawable(null)
                 plusIcon.visibility = View.VISIBLE
             }
         }
        else{                                                   // host role
             if(participant.image_file != null){

                 Glide.with(context)
                     .load(participant.image_file)
                     .placeholder(R.drawable.ic_user) // any placeholder to load at start
                     .error(R.drawable.ic_user)// image url
                     //.placeholder(R.drawable.profileimg_home)  // any placeholder to load at start
                     //.error(R.drawable.profileimg_home)        // any image in case of error
                     .into(circularImg)

               //  circularImg.setBackgroundResource(0)
                 plusIcon.visibility = View.GONE
                 circularImgBorder.visibility = View.VISIBLE

             }
             else{
                 circularImg.setBackgroundResource(R.drawable.stroke_circular)
                 circularImg.setImageDrawable(null)
                 plusIcon.visibility = View.VISIBLE
                // circularImgBorder.visibility = View.GONE
             }
        }


        if(participant.name != null){
            title.setText(participant.name)
        }
        if(participant.score != null){
            scoreOne.setText(participant.score)
        }
    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    fun dateFormatWithMonthNameFull(date : String) : String {
        val myFormat = SimpleDateFormat("yyyy-MM-dd").parse(date)
        var myDate = SimpleDateFormat("EEEE, dd/MM").format(myFormat)
        return myDate
    }

    override fun getItemCount() =  matchList.size


}


