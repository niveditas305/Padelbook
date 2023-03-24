package snow.app.padelbook.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.items_home_two.view.*
import kotlinx.android.synthetic.main.items_results.view.*
import kotlinx.android.synthetic.main.items_results.view.circularImg
import kotlinx.android.synthetic.main.items_results.view.circularImgFour
import kotlinx.android.synthetic.main.items_results.view.circularImgFourBorder
import kotlinx.android.synthetic.main.items_results.view.circularImgThree
import kotlinx.android.synthetic.main.items_results.view.circularImgThreeBorder
import kotlinx.android.synthetic.main.items_results.view.circularImgTwo
import kotlinx.android.synthetic.main.items_results.view.clTop
import kotlinx.android.synthetic.main.items_results.view.imgFirstSec
import kotlinx.android.synthetic.main.items_results.view.imgForthSec
import kotlinx.android.synthetic.main.items_results.view.plusIconFirst
import kotlinx.android.synthetic.main.items_results.view.plusIconForth
import kotlinx.android.synthetic.main.items_results.view.plusIconSecond
import kotlinx.android.synthetic.main.items_results.view.plusIconThird
import kotlinx.android.synthetic.main.items_results.view.scoreFour
import kotlinx.android.synthetic.main.items_results.view.scoreOne
import kotlinx.android.synthetic.main.items_results.view.scoreThree
import kotlinx.android.synthetic.main.items_results.view.scoreTwo
import kotlinx.android.synthetic.main.items_results.view.title
import kotlinx.android.synthetic.main.items_results.view.titleFour
import kotlinx.android.synthetic.main.items_results.view.titleThree
import kotlinx.android.synthetic.main.items_results.view.titleTwo
import kotlinx.android.synthetic.main.items_results.view.tvTitle
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.matchResult.matchResultNEw.MatchResultData
import snow.app.padelbook.network.responses.matchResult.matchResultNEw.Participant

class ResultAdapter(var mcontext: Context,val matchList: ArrayList<MatchResultData>)  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_results, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var matchData = matchList[position]

        val drawable = (holder.itemView.clTop).background as GradientDrawable
        drawable.setStroke(1,ContextCompat.getColor(mcontext,R.color.btn_grey_clr))

        if(matchData.date != null && matchData.time != null){
            holder.itemView.tvTitle.setText(matchData.date+" | "+matchData.time)

            var fullDate = matchData.date+" | "+matchData.time

            if(fullDate.contains("Mon"))
            {
                holder.itemView.tvTitle.setText(fullDate.replace("Monday",mcontext.getString(R.string.monday)))
            }
            else

                if(fullDate.contains("Tues"))
                {
                    holder.itemView.tvTitle.setText(fullDate.replace("Tuesday",mcontext.getString(R.string.tuesday)))
                }
                else
                    if(fullDate.contains("Wed"))
                    {
                        holder.itemView.tvTitle.setText(fullDate.replace("Wednesday",mcontext.getString(R.string.wednes)))
                    }
                    else

                        if(fullDate.contains("Thurs"))
                        {
                            holder.itemView.tvTitle.setText(fullDate.replace("Thursday",mcontext.getString(R.string.thurs)))
                        }
                        else

                            if(fullDate.contains("Frid"))
                            {
                                holder.itemView.tvTitle.setText(fullDate.replace("Friday",mcontext.getString(R.string.friday)))
                            }
                            else
                                if(fullDate.contains("Satur"))
                                {
                                    holder.itemView.tvTitle.setText(fullDate.replace("Saturday",mcontext.getString(R.string.satu)))
                                }
                                else

                                    if(fullDate.contains("Sund"))
                                    {
                                        holder.itemView.tvTitle.setText(fullDate.replace("Sunday",mcontext.getString(R.string.sunday)))
                                    }
        }

        if(matchData.clubName != null && matchData.distance != null){
           holder.itemView.tvTitleTwo.setText(matchData.clubName+" - "+matchData.distance.replace(",","")+"km")
        }

        if(matchData.win_status != null) {
            if (matchData.win_status.equals("1")) {
                holder.itemView.badgeIcon.visibility = View.VISIBLE
                holder.itemView.badgeIconTwo.visibility = View.INVISIBLE
            } else if (matchData.win_status.equals("2")) {
                holder.itemView.badgeIcon.visibility = View.INVISIBLE
                holder.itemView.badgeIconTwo.visibility = View.VISIBLE
            } else {
                holder.itemView.badgeIcon.visibility = View.INVISIBLE
                holder.itemView.badgeIconTwo.visibility = View.INVISIBLE
            }
        }

        if(matchData.court_feature == "Double"){
            // if teams has four players
            for (i in matchData.participant_list.indices) {
                if (matchData.participant_list[i].player_key == "1") {
                    setDataOnImageSec(
                        holder.itemView.circularImg, holder.itemView.title,
                        holder.itemView.scoreOne, matchData.participant_list[i],
                        holder.itemView.plusIconFirst, holder.itemView.circularImgOneBorder
                    )
                } else if (matchData.participant_list[i].player_key == "2") {
                    setDataOnImageSec(
                        holder.itemView.circularImgTwo, holder.itemView.titleTwo,
                        holder.itemView.scoreTwo, matchData.participant_list[i],
                        holder.itemView.plusIconSecond, holder.itemView.circularImgTwoBorder
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
        }
        else {
            // if team has two players
            holder.itemView.imgFirstSec.visibility = View.GONE
            holder.itemView.imgForthSec.visibility = View.GONE

            for (i in matchData.participant_list.indices) {
                if (matchData.participant_list[i].player_key == "1") {
                    setDataOnImageSec(
                        holder.itemView.circularImgTwo, holder.itemView.titleTwo,
                        holder.itemView.scoreTwo, matchData.participant_list[i],
                        holder.itemView.plusIconSecond , holder.itemView.circularImgTwoBorder
                    )
                } else if(matchData.participant_list[i].player_key == "2") {
                    setDataOnImageSec(
                        holder.itemView.circularImgThree, holder.itemView.titleThree,
                        holder.itemView.scoreThree, matchData.participant_list[i],
                        holder.itemView.plusIconThird, holder.itemView.circularImgThreeBorder
                    )
                }
            }
        }

//
//           if(matchData.team != null) {
//               if (matchData.team.equals("1")) {
//                   if(matchData.win_status != null) {
//                       if (matchData.win_status.equals("1")) {           // for win
//                           holder.itemView.badgeIcon.visibility = View.VISIBLE
//                           holder.itemView.badgeIconTwo.visibility = View.INVISIBLE
//                       } else if (matchData.win_status.equals("2")) {     // defeat
//                           holder.itemView.badgeIcon.visibility = View.INVISIBLE
//                           holder.itemView.badgeIconTwo.visibility = View.VISIBLE
//                       } else {
//                           holder.itemView.badgeIcon.visibility = View.INVISIBLE
//                           holder.itemView.badgeIconTwo.visibility = View.INVISIBLE
//                       }
//                   }
//               } else {
//                   if(matchData.win_status != null) {
//                       if (matchData.win_status.equals("1")) {           // for win
//                           holder.itemView.badgeIcon.visibility = View.INVISIBLE
//                           holder.itemView.badgeIconTwo.visibility = View.VISIBLE
//                       } else if (matchData.win_status.equals("2")) {     // defeat
//                           holder.itemView.badgeIcon.visibility = View.VISIBLE
//                           holder.itemView.badgeIconTwo.visibility = View.INVISIBLE
//                       } else {
//                           holder.itemView.badgeIcon.visibility = View.INVISIBLE
//                           holder.itemView.badgeIconTwo.visibility = View.INVISIBLE
//                       }
//                   }
//               }
//           }

            for(i in matchData.schedule_id.indices){

                if(matchData.schedule_id[0] != null) {
                    holder.itemView.teamFirst_roundOne.setText(matchData.schedule_id[0].teamA)
                    holder.itemView.teamSecond_roundOne.setText(matchData.schedule_id[0].teamB)
                    if(matchData.schedule_id[0].teamA.toInt() > matchData.schedule_id[0].teamB.toInt()){
                        showBlueText(holder.itemView.teamFirst_roundOne)
                        showGreyText(holder.itemView.teamSecond_roundOne)
                    }
                    else if(matchData.schedule_id[0].teamA.toInt() == matchData.schedule_id[0].teamB.toInt()){
                        showGreyText(holder.itemView.teamFirst_roundOne)
                        showGreyText(holder.itemView.teamSecond_roundOne)
                    }
                    else{
                        showBlueText(holder.itemView.teamSecond_roundOne)
                        showGreyText(holder.itemView.teamFirst_roundOne)
                    }
                }
                if(matchData.schedule_id[1] != null){
                    holder.itemView.teamFirst_roundTwo.setText(matchData.schedule_id[1].teamA)
                    holder.itemView.teamSecond_roundTwo.setText(matchData.schedule_id[1].teamB)
                    if(matchData.schedule_id[1].teamA.toInt() > matchData.schedule_id[1].teamB.toInt()){
                        showBlueText(holder.itemView.teamFirst_roundTwo)
                        showGreyText(holder.itemView.teamSecond_roundTwo)
                    }
                    else if(matchData.schedule_id[1].teamA.toInt() == matchData.schedule_id[1].teamB.toInt()){
                        showGreyText(holder.itemView.teamFirst_roundTwo)
                        showGreyText(holder.itemView.teamSecond_roundTwo)
                    }
                    else{
                        showBlueText(holder.itemView.teamSecond_roundTwo)
                        showGreyText(holder.itemView.teamFirst_roundTwo)
                    }
                }
                if(matchData.schedule_id[2] != null){
                    holder.itemView.teamFirst_roundThree.setText(matchData.schedule_id[2].teamA)
                    holder.itemView.teamSecond_roundThree.setText(matchData.schedule_id[2].teamB)
                    if(matchData.schedule_id[2].teamA.toInt() > matchData.schedule_id[2].teamB.toInt()){
                        showBlueText(holder.itemView.teamFirst_roundThree)
                        showGreyText(holder.itemView.teamSecond_roundThree)
                    }
                    else if(matchData.schedule_id[2].teamA.toInt() == matchData.schedule_id[2].teamB.toInt()){
                        showGreyText(holder.itemView.teamFirst_roundThree)
                        showGreyText(holder.itemView.teamSecond_roundThree)
                    }
                    else{
                        showBlueText(holder.itemView.teamSecond_roundThree)
                        showGreyText(holder.itemView.teamFirst_roundThree)
                    }
                }
            }

    }


    private fun showBlueText(teamOneFirst: TextView?) {
        teamOneFirst?.setTextColor(ContextCompat.getColor(mcontext,R.color.theme_blue))
    }
    private fun showGreyText(teamOneFirst: TextView?) {
        teamOneFirst?.setTextColor(ContextCompat.getColor(mcontext,R.color.contact_clr))
    }

    private fun setDataOnImageSec(
        circularImg: CircleImageView,
        title: TextView,
        scoreOne: TextView,
        participant: Participant,
        plusIcon: TextView,
        circularBorder: ImageView
    )
    {
        if(participant.user_type == "1"){                     // user role
            if(participant.image_file != null){
                Glide.with(mcontext)
                    .load(participant.image_file)
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)// image url
                    //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                    //.error(R.drawable.profileimg_home)  // any image in case of error
                    .into(circularImg)
                plusIcon.visibility = View.GONE
            }
            else{
                plusIcon.visibility = View.VISIBLE
                circularImg.setImageDrawable(null)
            }
        }
        else{                                                   // host role
            if(participant.image_file != null){

                Glide.with(mcontext)
                    .load(participant.image_file)
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)// image url
                    //.placeholder(R.drawable.profileimg_home)  // any placeholder to load at start
                    //.error(R.drawable.profileimg_home)        // any image in case of error
                    .into(circularImg)
                plusIcon.visibility = View.GONE
                circularBorder.visibility = View.VISIBLE
            }
            else{
                circularBorder.visibility = View.GONE
                plusIcon.visibility = View.VISIBLE
                circularImg.setImageDrawable(null)
            }
        }

        if(participant.name != null){
            title.setText(participant.name)
        }
        if(participant.score != null){
            scoreOne.setText(participant.score)
        }
    }

    override fun getItemCount() = matchList.size
}