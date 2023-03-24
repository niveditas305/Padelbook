package snow.app.padelbook.adapter

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items_notification.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.notificationList.NotifyData

class NotificationAdapter(
    var mcontext: Context, val arrayList: ArrayList<NotifyData>,
    val listener: NotificationInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isSelected = -1

    interface NotificationInterface {
        fun notifyData(position: Int, data: NotifyData)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = arrayList[position]
//        if (isSelected == position) {
//            holder.itemView.mainLayout.setBackgroundColor(
//                ContextCompat.getColor(
//                    mcontext,
//                    R.color.btn_clr_blue_light
//                )
//            )
//        } else {
//            holder.itemView.mainLayout.setBackgroundColor(
//                ContextCompat.getColor(
//                    mcontext,
//                    R.color.white
//                )
//            )
//        }

        if (data.seen.equals("0")) {
            holder.itemView.mainLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mcontext,
                    R.color.unread_clr
                )
            )
        } else {
            holder.itemView.mainLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mcontext,
                    R.color.white
                )
            )
        }

        if (data.image_file != null) {
            Glide.with(mcontext)
                .load(data.image_file)
                .placeholder(R.drawable.ic_user) // any placeholder to load at start
                .error(R.drawable.ic_user)// image url
                // any placeholder to load at start
                // .error(R.drawable.ic_user_default)  // any image in case of error
                .into(holder.itemView.dp)
        }
        if (data.time != null) {
            if (data.time.contains("min")) {

                if (data.time.split(" ")[0].toString().toInt() == 1) {
                    holder.itemView.tvTime.setText(
                        data.time.split(" ")[0].toString() + " " + mcontext.getString(
                            R.string.minute
                        ) + " " + mcontext.getString(R.string.ago)
                    )
                } else {
                    holder.itemView.tvTime.setText(
                        data.time.split(" ")[0].toString() + " " + mcontext.getString(
                            R.string.minutes
                        ) + " " + mcontext.getString(R.string.ago)
                    )
                }

            } else
                if (data.time.contains("hour")) {
                    Log.e("data.time", data.time)
                    Log.e("data.time", data.time)

                    if (data.time.split(" ")[0].toString().toInt() == 1) {
                        holder.itemView.tvTime.setText(
                            data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                R.string.hour
                            ) + " " + mcontext.getString(R.string.ago)
                        )
                    } else {
                        holder.itemView.tvTime.setText(
                            data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                R.string.hours
                            ) + " " + mcontext.getString(R.string.ago)
                        )
                    }

                } else
                    if (data.time.contains("day")) {

                        if (data.time.split(" ")[0].toString().toInt() == 1) {
                            holder.itemView.tvTime.setText(
                                data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                    R.string.day
                                ) + " " + mcontext.getString(R.string.ago)
                            )
                        } else {
                            holder.itemView.tvTime.setText(
                                data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                    R.string.days
                                ) + " " + mcontext.getString(R.string.ago)
                            )
                        }

                    } else
                        if (data.time.contains("week")) {

                            if (data.time.split(" ")[0].toString().toInt() == 1) {
                                holder.itemView.tvTime.setText(
                                    data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                        R.string.week
                                    ) + " " + mcontext.getString(R.string.ago)
                                )
                            } else {
                                holder.itemView.tvTime.setText(
                                    data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                        R.string.weeks
                                    ) + " " + mcontext.getString(R.string.ago)
                                )
                            }

                        } else
                            if (data.time.contains("month")) {

                                if (data.time.split(" ")[0].toString().toInt() == 1) {
                                    holder.itemView.tvTime.setText(
                                        data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                            R.string.month
                                        ) + " " + mcontext.getString(R.string.ago)
                                    )
                                } else {
                                    holder.itemView.tvTime.setText(
                                        data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                            R.string.months
                                        ) + " " + mcontext.getString(R.string.ago)
                                    )
                                }

                            } else
                                if (data.time.contains("year")) {

                                    if (data.time.split(" ")[0].toString().toInt() == 1) {
                                        holder.itemView.tvTime.setText(
                                            data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                                R.string.year
                                            ) + " " + mcontext.getString(R.string.ago)
                                        )
                                    } else {
                                        holder.itemView.tvTime.setText(
                                            data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                                R.string.years
                                            ) + " " + mcontext.getString(R.string.ago)
                                        )
                                    }

                                } else
                                    if (data.time.contains("sec")) {

                                        if (data.time.split(" ")[0].toString().toInt() == 1) {
                                            holder.itemView.tvTime.setText(
                                                data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                                    R.string.second
                                                ) + " " + mcontext.getString(R.string.ago)
                                            )
                                        } else {
                                            holder.itemView.tvTime.setText(
                                                data.time.split(" ")[0].toString() + " " + mcontext.getString(
                                                    R.string.seconds
                                                ) + " " + mcontext.getString(R.string.ago)
                                            )
                                        }

                                    }

        }


        //set text
        if (data.notification_type.equals("1")) {                  // AddPlayer
            if (data.club_name != null && data.match_date != null) {
                val dataToShow =
                    "<b>" + data.name + "</b>" + " " + mcontext.getString(R.string.invite_you_to_a_match) + " " +
                            "<b>" + data.match_date + ", " + data.match_time + "</b>" + " " + mcontext.getString(
                        R.string.at
                    ) + " " + "<b>" + data.club_name + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )

                //    holder.itemView.tvMainTitle.setText(mcontext.getString(R.string.notification_des)+" "+data.club_name+" "+mcontext.getString(R.string.is_on)+" "+data.date)
            }
        } else if (data.notification_type.equals("2")) {            // AddPlayerMatch
            if (data.name != null && data.match_date != null && data.club_name != null) {
                var datatoShow =
                    "<b>" + data.name + "</b>" + " " + mcontext.getString(R.string.join_txtt) + " " + "<b>" + data.match_date + ", " +
                            data.match_time + "</b>" + " " + mcontext.getString(
                        R.string.at
                    ) + " " + "<b>" + data.club_name + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(datatoShow, HtmlCompat.FROM_HTML_MODE_LEGACY)
                )
                // holder.itemView.tvMainTitle.setText(mcontext.getString(R.string.notification_des)+" "+data.club_name+" "+mcontext.getString(R.string.is_on)+" "+data.date)
            }
        } else if (data.notification_type.equals("3") || data.notification_type.equals("13")) {            // Accept Booking
            if (data.name != null && data.match_time != null) {
                val dataToShow =
                    "<b>" + data.name + "</b> " + " " + mcontext.getString(R.string.accept_your_match_request_on) + " " + "<b>" + data.match_date + ", " + data.match_time + "</b>" + " " +
                            mcontext.getString(R.string.at) + " " + "<b>" + data.court_name + "</b>" + " "

                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
            }
        } else if (data.notification_type.equals("4") || data.notification_type.equals("14")) {                                              // 4 -> Cancel Booking
            if (data.club_name != null && data.match_date != null && data.match_time != null) {
                val dataToShow =
                    "<b>" + data.name + "</b> " + " " + mcontext.getString(R.string.rejected_your_match_request) + " " + "<b>" + data.match_date + "</b> " +
                            mcontext.getString(R.string._at_) + " " + "<b>" + data.match_time + "</b> "
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
            }
        } else if (data.notification_type.equals("5") || data.notification_type.equals("17")) {
            if (data.name != null) {
                val dataToShow =
                    mcontext.getString(R.string.congrates) + " " + "<b>" + data.name + "</b> " + " " + mcontext.getString(
                        R.string.added_score_messge
                    )
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
            }
        } else if (data.notification_type.equals("6") || data.notification_type.equals("18")) {

            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    mcontext.getString(R.string.score_confirmed) + " " + "<b>" + data.match_date + "</b> " + " " + "<b>" + data.match_time + "</b> " + " " + mcontext.getString(
                        R.string.at
                    ) + " " + "<b>" + data.club_name + "</b> " + " " + mcontext.getString(R.string.by) + " " + "<b>" + data.name + "</b> "
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
            }
        } else if (data.notification_type.equals("7") || data.notification_type.equals("19")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    mcontext.getString(R.string.score_for_the_match) + " " + "<b>" + data.match_date + "</b> " + " " + "<b>" + data.match_time + "</b> " + " " + mcontext.getString(
                        R.string.at
                    ) + " " + "<b>" + data.club_name + "</b> " + " " + mcontext.getString(
                        R.string.rejected_by
                    ) + " " + "<b>" + data.name + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

                )
            }
        } else if (data.notification_type.equals("8") || data.notification_type.equals("15")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    "<b>" + data.name + "</b>" + " " + mcontext.getString(R.string.canceled_the_match_on) + " " + "<b>" + data.match_date + "</b> " + " " + mcontext.getString(
                        R.string.at
                    ) + " " + "<b>" + data.match_time + "</b> "
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

                )
            }
        } else if (data.notification_type.equals("9") || data.notification_type.equals("16")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    "<b>" + data.name + "</b>" + " " + mcontext.getString(R.string.will_not_make_it_for_the_match) + " " + "<b>" + data.date + "</b>" + " " + mcontext.getString(
                        R.string.at
                    ) + " " + "<b>" + data.match_time + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

                )
            }
        } else if (data.notification_type.equals("10")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    mcontext.getString(R.string.few_hours_have_a_match) + " " + "<b>" + data.club_name + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)
                )
            }
        } else if (data.notification_type.equals("11")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    mcontext.getString(R.string.reminder_you_have_a_lesson) + " " + "<b>" + data.match_time + "</b>" + " " + mcontext.getString(
                        R.string.at
                    ) + " " + "<b>" + data.club_name + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)
                )
            }
        } else if (data.notification_type.equals("12")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    "<b>" + data.name + "</b>" + mcontext.getString(R.string.invite_you_to_a_private_match_on) + " " + "<b>" + data.match_date + "</b>" + " " + mcontext.getString(
                        R.string.at
                    ) + " " + "<b>" + data.club_name + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

                )
            }
        } else if (data.notification_type.equals("20") || data.notification_type.equals("21")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    mcontext.getString(R.string.there_is_new_match_with_available_spot) + " " + "<b>" + data.name + "</b>" + " " + mcontext.getString(
                        R.string.for_text
                    ) + "<b>" + data.match_date + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

                )
            }
        } else if (data.notification_type.equals("22") || data.notification_type.equals("23")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    "To" + " " + "<b>" + data.name + "</b>" + " " + mcontext.getString(
                        R.string.canceled_the_match_on
                    ) + " " + "<b>" + data.match_date + ", " + data.match_time + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)

                )
            }
        } else if (data.notification_type.equals("24")) {
            holder.itemView.tvMainTitle.setText(mcontext.getString(R.string.do_you_want_to_play_padel))
        } else if (data.notification_type.equals("25")) {
            holder.itemView.tvMainTitle.setText(mcontext.getString(R.string.check_for_available_courts))

        } else if (data.notification_type.equals("26")) {
            holder.itemView.tvMainTitle.setText(mcontext.getString(R.string.congrates_your_level_just_got_higher))

        } else if (data.notification_type.equals("27")) {
            if (data.match_date != null && data.club_name != null) {
                val dataToShow =
                    mcontext.getString(R.string.canceled_the_match_on) + " " + "<b>" + data.match_date + "</b>" + " " + "due to slots are not booked"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)
                )

            }
        } else if (data.notification_type.equals("28")) {
            /*        if (data.date != null && data.time != null && data.club_name != null || data.name != null) {
                        holder.itemView.tvMainTitle.setText(mcontext.getString(R.string.congrates_your_level_just_got_higher))
                    }*/
            val dataToShow =
                mcontext.getString(R.string.canceled_the_match_on) + " " + "<b>" + data.match_date + "</b>" + " " + "due to slots are not booked"
            holder.itemView.tvMainTitle.setText(
                HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)
            )
        } else if (data.notification_type.equals("29")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                val dataToShow =
                    mcontext.getString(R.string.your_match_on) + " " + "<b>" + data.description + "</b>" + " " +
                            mcontext.getString(R.string.is_automatically_got_cancelled)
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(dataToShow, HtmlCompat.FROM_HTML_MODE_LEGACY)
                )
            }
        } else if (data.notification_type.equals("30")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                /* val dataToShow =
                     mcontext.getString(R.string.your_match_scheduled_on) + " " + "<b>" + data.match_date + "</b>" + "," + "<b>" + data.match_time + "</b>" + " " + mcontext.getString(
                         R.string.is_changed_to
                     ) + " " + "<b>" + data.description + "</b>"
                */
                val dataToShow =
                    mcontext.getString(R.string.your_match_scheduled_on) + " " + "<b>" + data.description + "</b>" + " " + mcontext.getString(
                        R.string.is_changed_to
                    ) + " " + "<b>" + data.match_date + "</b>" + "," + "<b>" + data.match_time + "</b>"

                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
            }

        } else if (data.notification_type.equals("35")) {
            if (data.match_date != null && data.match_time != null && data.club_name != null || data.name != null) {
                /* val dataToShow =
                     mcontext.getString(R.string.your_match_scheduled_on) + " " + "<b>" + data.match_date + "</b>" + "," + "<b>" + data.match_time + "</b>" + " " + mcontext.getString(
                         R.string.is_changed_to
                     ) + " " + "<b>" + data.description + "</b>"
                */
                val dataToShow =
                    mcontext.getString(R.string.your_match_scheduled_on) + " " + "<b>" + data.description + "</b>" + " " + mcontext.getString(
                        R.string.has_changed_court_to
                    ) + " " + "<b>" + data.court_name + "</b>"

                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
            }

        } else if (data.notification_type.equals("36")) {
            if (data.club_name != null && data.name != null && data.match_date != null && data.match_time != null) {
                /* val dataToShow =
                     mcontext.getString(R.string.your_match_scheduled_on) + " " + "<b>" + data.match_date + "</b>" + "," + "<b>" + data.match_time + "</b>" + " " + mcontext.getString(
                         R.string.is_changed_to
                     ) + " " + "<b>" + data.description + "</b>"
                */
                val dataToShow = mcontext.getString(R.string.to_front)+" "+"<b>" + data.club_name + "</b>" + " "+
                mcontext.getString(R.string.number_thirty_six_text) + " " + "<b>" + data.name + "</b>" + ", " +
                        "<b>" + data.match_date +" "+data.match_time + "</b>"

                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
            }

        } else if (data.notification_type.equals("31")) {
            if (data.club_name != null && data.description != null) {
                val dataToShow =
                    mcontext.getString(R.string.too) + " " + "<b>" + data.club_name + "</b>" + " " + mcontext.getString(
                        R.string.canceled_the_match_on
                    ) + " " + "<b>" + data.description + "</b>"
                holder.itemView.tvMainTitle.setText(
                    HtmlCompat.fromHtml(
                        dataToShow,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                )
            }

        }





        holder.itemView.mainLayout.setOnClickListener {
            //   isSelected = position
            notifyDataSetChanged()
            Log.e("matchid adapter", "---" + data.match_id)
            listener.notifyData(position, data)
        }

    }

    override fun getItemCount() = arrayList.size
}