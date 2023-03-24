package snow.app.padelbook.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_notification.*

import kotlinx.android.synthetic.main.items_notification.view.*
import kotlinx.android.synthetic.main.items_results.view.*
import kotlinx.android.synthetic.main.popup_accept_request.*
import kotlinx.android.synthetic.main.popup_accept_request.tvDes
import kotlinx.android.synthetic.main.popup_add_result_confirmation.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.toolbar.view.separator
import kotlinx.android.synthetic.main.toolbar.view.tvTitle
import snow.app.padelbook.R
import snow.app.padelbook.adapter.NotificationAdapter
import snow.app.padelbook.network.responses.notificationList.NotifyData
import snow.app.padelbook.network.responses.score.singleScoreDetailNew.Data
import snow.app.padelbook.viewModel.AddScoreVM
import snow.app.padelbook.viewModel.NotificationVM
import snow.app.padelbook.viewModel.NotifyCountReadVM

class NotificationActivity : BaseActivity(), NotificationAdapter.NotificationInterface,
    SwipeRefreshLayout.OnRefreshListener {
    private var adapter: NotificationAdapter? = null
    var notifyVM: NotificationVM? = null
    var arrayList: ArrayList<NotifyData> = ArrayList()
    var notifyReadVM: NotifyCountReadVM? = null
    var acceptStatus = -1
    var scoreRequestAccept = -1

    var addScoreVm: AddScoreVM? = null
    var selectedPosition = -1

    companion object {
        var callFromItemClick = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        notifyVM = NotificationVM()
        addScoreVm = AddScoreVM()
        notifyReadVM = NotifyCountReadVM()

        callFromItemClick = ""


        swipeRef.setOnRefreshListener(this);
        // <-------- notification list api-------->

        setToolbar()
        setClick()
        listener()
    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkConnected()) {
            showToast(this, "No internet connection")
        } else {
            notifyVM?.notificationListData(this)
        }

    }

    private fun setClick() {
        tvReadAll.setOnClickListener {
            if (isNetworkConnected()) {
                notifyReadVM?.readNotification(this, "0")
            } else {
                showInternetToast()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listener() {
        // <-------------- response for accept/ reject score request ---->
        notifyVM?.acceptRejectScore?.observe(this, Observer { user ->
            if (user != null) {
                if (scoreRequestAccept == 1) {   // accept score request
                    startActivity(Intent(this, ResultsActivity::class.java))
                    finish()
                } else {                          // reject score request
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })


        // <--------------- accept add player request
        notifyVM?.acceptRejectList?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    showToast(this, user.message)
                    if (acceptStatus == 1) {             // accept
                        startActivity(
                            Intent(this, GamesActivity::class.java)
                                .putExtra("matchId", user.data.match_id)
                        )
                    } else {                                    // reject
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        }) // </------------------------------>


        // <---------- notification list response -------->
        notifyVM?.notificationList?.observe(this, Observer { user ->
            if (user != null) {
                if (swipeRef != null) {
                    swipeRef.setRefreshing(false)
                }
                if (user.status) {
                    //  showToast(this,user.message)
                    arrayList.clear()
                    if (user.unread > 0) {
                        tvReadAll.visibility = View.VISIBLE
                    } else {
                        tvReadAll.visibility = View.GONE
                    }

                    if (user.data.isNotEmpty()) {
                        arrayList.addAll(user.data)
                        llNoResultsFound.visibility = View.GONE
                        rvNotify.visibility = View.VISIBLE
                    } else {
                        llNoResultsFound.visibility = View.VISIBLE
                        rvNotify.visibility = View.GONE
                    }
                    setAdapter()
                } else {
                    llNoResultsFound.visibility = View.VISIBLE
                    rvNotify.visibility = View.GONE
                    // showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
        // </----------------------------------------->

        // <-------- Notification Read -------------->
        notifyReadVM?.notifyRead?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    //  showToast(this,user.message)
                    if (callFromItemClick.equals("ItemClick")) {
                        arrayList[selectedPosition].seen = "1"
                        adapter?.notifyDataSetChanged()

                    } else {
                        if (!isNetworkConnected()) {
                            showToast(this, "No internet connection")
                        } else {
                            notifyVM?.notificationListData(this)
                        }
                    }
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
    }

    private fun setToolbar() {
        toolbarId.tvTitle.visibility = View.VISIBLE
        toolbarId.tvTitle.setText(getString(R.string.menu_one))
        toolbarId.separator.visibility = View.GONE
        toolbarId.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setAdapter() {
        adapter = NotificationAdapter(this, arrayList, this)
        rvNotify.adapter = adapter
    }

    private fun showDialog(data: NotifyData) {
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(true)
        dialogData.setContentView(R.layout.popup_accept_request)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (data.image_file != null) {
            Glide.with(this)
                .load(data.image_file)                    // image url
                .placeholder(R.drawable.ic_user_default)  // any placeholder to load at start
                // .error(R.drawable.ic_user_default)     // any image in case of error
                .into(dialogData.profile)
        }
        if (data.name != null) {
            if (data.notification_type.equals("36")) {
                if (data.club_name != null) {
                    dialogData.tvName.setText(data.club_name)
                }
            } else {
                dialogData.tvName.setText(data.name)
            }

        }

        if (data.score != null) {

            dialogData.tvTileTwo.setText(data.score)
        }


        if (data.name != null && data.club_name != null && data.match_date != null) {
            if (data.notification_type.equals("1")) {
                dialogData.tvDes.setText(
                    data?.name +    " " + getString(R.string.invited_you_to_a_match) +    " " + data?.match_date +
                            getString(R.string.at) +    " " + data?.club_name
                )


            } else if (data.notification_type.equals("36")) {
                if (data.club_name != null && data.name != null && data.match_date != null && data.match_time != null) {
                    dialogData.tvDes.setText(
                        getString(R.string.to_front)+" "+ data?.club_name + " " + getString(R.string.number_thirty_six_text) + " " + data?.name +", "+
                                data.match_date + " " + data.match_time
                    )
                }
            } else
                if (data.notification_type.equals("2")) {
                    dialogData.tvDes.setText(
                        data.name + " " + getString(R.string.join_txtt) + " " + data.match_date + " " + getString(
                            R.string.at
                        ) + " " + data.club_name
                    )
                    //  dialogData.tvDes.setText(data?.name+" requested you for play the match at " + data?.club_name+" on " + data?.date)

                } else {
                    dialogData.tvDes.setText(
                        data.name + " " + getString(R.string.join_txtt) + " " + data.match_date + " " + getString(
                            R.string.at
                        ) + " " + data.club_name
                    )

                    // dialogData.tvDes.setText(data?.name+" requested you for play the match at " + data?.club_name+" on " + data?.date)

                }
            // dialogData.tvDes.setText(data.title + " wants to participate in the game you have " + data.description)
        }
        dialogData.tvAccept.setOnClickListener {
            dialogData.dismiss()
            if (data.user_type == "2") {         // if role is host, then direct accept the request
                //  if(data.pay_type == "2") {     // if  host paid all amount
                // <-------- accept api ---------->
                if (!isNetworkConnected()) {
                    showToast(this, "No internet connection")
                } else {
                    acceptStatus = 1
                    notifyVM?.acceptRejectResponse(this, data.booking_id, data.match_id, "1", "")
                }
                //  }
            } else {
                if (data.pay_type == "2") {
                    if (!isNetworkConnected()) {
                        showToast(this, "No internet connection")
                    } else {
                        acceptStatus = 1
                        notifyVM?.acceptRejectResponse(
                            this,
                            data.booking_id,
                            data.match_id,
                            "1",
                            ""
                        )
                    }
                } else {
                    dialogData.dismiss()

                    Log.e("matchid", "---" + data.match_id)
                    startActivity(
                        Intent(this, GamesActivity::class.java)
                            .putExtra("notification", "notification")
                            .putExtra("matchId", data.match_id)

                            // .putExtra("playerKey", data.match_id)
                            .putExtra("bookingId", data.booking_id)
                    )
                }

            }

        }
        dialogData.tvReject.setOnClickListener {
            dialogData.dismiss()
            // <-------- reject api ---------->
            if (!isNetworkConnected()) {
                showToast(this, "No internet connection")
            } else {
                acceptStatus = 2
                notifyVM?.acceptRejectResponse(this, data.booking_id, data.match_id, "2", "")
            }
        }

        dialogData.show()
    }

    override fun notifyData(position: Int, data: NotifyData) {
        selectedPosition = position
        if (isNetworkConnected()) {
            callFromItemClick = "ItemClick"
            notifyReadVM?.readNotification(this, data.id)
        } else {
            showInternetToast()
        }



        if (data.notification_type.equals("1")) {
            // AddPlayer
            if (!data.matchInPast) {
                showDialog(data)
            }
        } else if (data.notification_type.equals("36")) {
            // AddPlayer
            if (!data.matchInPast) {
                showDialog(data)
            }
        } else if (data.notification_type.equals("2")) {            // AddPlayerMatch
            showDialog(data)
        } else if (data.notification_type.equals("3") || data.notification_type.equals("13")) {            // Accept Booking

            //Booking screen 3,4,9,10,11,13,14,16,20
            startActivity(
                Intent(this, GamesActivity::class.java)
                    .putExtra("notification", "notification")
                    .putExtra("matchId", data.match_id)
                    // .putExtra("playerKey", data.match_id)
                    .putExtra("bookingId", data.booking_id)
            )
        } else if (data.notification_type.equals("4") || data.notification_type.equals("14")) {                                              // 4 -> Cancel Booking
            /*   startActivity(
                   Intent(this, GamesActivity::class.java)
                       .putExtra("notification", "notification")
                       .putExtra("matchId", data.match_id)
                       // .putExtra("playerKey", data.match_id)
                       .putExtra("bookingId", data.booking_id)
               )*/
        } else if (data.notification_type.equals("5") || data.notification_type.equals("17")) {
            showScoreDialog(data)
        } else if (data.notification_type.equals("6") || data.notification_type.equals("18")) {
// result screen
            startActivity(Intent(this, ResultsActivity::class.java))
        } else if (data.notification_type.equals("7") || data.notification_type.equals("19")) {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
// 7,8,15,19,24,25,26 -- Home screen
        } else if (data.notification_type.equals("8") || data.notification_type.equals("15")) {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        } else if (data.notification_type.equals("9") || data.notification_type.equals("16")) {
            startActivity(
                Intent(this, GamesActivity::class.java)
                    .putExtra("notification", "notification")
                    .putExtra("matchId", data.match_id)
                    // .putExtra("playerKey", data.match_id)
                    .putExtra("bookingId", data.booking_id)
            )
        } else if (data.notification_type.equals("10")) {
            startActivity(
                Intent(this, GamesActivity::class.java)
                    .putExtra("notification", "notification")
                    .putExtra("matchId", data.match_id)
                    // .putExtra("playerKey", data.match_id)
                    .putExtra("bookingId", data.booking_id)
            )
        } else if (data.notification_type.equals("11")) {
            startActivity(
                Intent(this, GamesActivity::class.java)
                    .putExtra("notification", "notification")
                    .putExtra("matchId", data.match_id)
                    // .putExtra("playerKey", data.match_id)
                    .putExtra("bookingId", data.booking_id)
            )
        } else if (data.notification_type.equals("12")) {

        } else if (data.notification_type.equals("20") || data.notification_type.equals("21")) {
            startActivity(
                Intent(this, GamesActivity::class.java)
                    .putExtra("notification", "notification")
                    .putExtra("matchId", data.match_id)
                    // .putExtra("playerKey", data.match_id)
                    .putExtra("bookingId", data.booking_id)
            )
        } else if (data.notification_type.equals("22")) {
            // if public 22 // private 23
            startActivity(
                Intent(this, CategoryDetailActivity::class.java)
                    .putExtra("club_id", data.club_id)
                    .putExtra("scheduleID", "")
                    .putExtra("callFrom", "StartAMatch")
            )
//- Club details

            // if(data.match_type.equals(""))


        } else if (data.notification_type.equals("23")) {
            // if public 22 // private 23
            startActivity(
                Intent(this, CategoryDetailActivity::class.java)
                    .putExtra("club_id", data.club_id)
                    .putExtra("callFrom", "")
            )
//- Club details

            // if(data.match_type.equals(""))


        } else if (data.notification_type.equals("24")) {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        } else if (data.notification_type.equals("25")) {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        } else if (data.notification_type.equals("26")) {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        } else if (data.notification_type.equals("27")) {

        } else if (data.notification_type.equals("28")) {

        }

    }


    private fun showScoreDialog(data: NotifyData) {
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(true)
        dialogData.setContentView(R.layout.popup_add_result_confirmation)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // <--------single score detail api --------->
        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            addScoreVm?.singleScoreDetailData(this@NotificationActivity, data.match_id)
        }

        addScoreVm?.singleScoreDetail?.observe(this@NotificationActivity, Observer { user ->
            if (user != null) {
                if (user.status) {
                    showToast(this, user.message)

                    // <-------------- result score setting -------->
                    if (user.score.size > 0) {
                        for (i in user.score.indices) {

                            if (user.score[0] != null) {
                                dialogData?.teamOneFirst?.setText(user.score[0].teamA)
                                dialogData?.teamOneSecond?.setText(user.score[0].teamB)

                                if (user.score[0].teamA.toInt() > user.score[0].teamB.toInt()) {
                                    showBlueText(dialogData?.teamOneFirst)
                                    showGreyText(dialogData?.teamOneSecond)
                                } else if (user.score[0].teamA.toInt() == user.score[0].teamB.toInt()) {
                                    showGreyText(dialogData?.teamOneFirst)
                                    showGreyText(dialogData?.teamOneSecond)
                                } else {
                                    showBlueText(dialogData?.teamOneSecond)
                                    showGreyText(dialogData?.teamOneFirst)
                                }
                            }
                            if (user.score[1] != null) {
                                dialogData?.teamTwoFirst?.setText(user.score[1].teamA)
                                dialogData?.teamTwoSecond?.setText(user.score[1].teamB)
                                if (user.score[1].teamA.toInt() > user.score[1].teamB.toInt()) {
                                    showBlueText(dialogData?.teamTwoFirst)
                                    showGreyText(dialogData?.teamTwoSecond)
                                } else if (user.score[1].teamA.toInt() == user.score[1].teamB.toInt()) {
                                    showGreyText(dialogData?.teamTwoFirst)
                                    showGreyText(dialogData?.teamTwoSecond)
                                } else {
                                    showBlueText(dialogData?.teamTwoSecond)
                                    showGreyText(dialogData?.teamTwoFirst)
                                }

                            }
                            if (user.score[2] != null) {
                                dialogData?.teamThreeFirst?.setText(user.score[2].teamA)
                                dialogData?.teamThreeSecond?.setText(user.score[2].teamB)
                                if (user.score[2].teamA.toInt() > user.score[2].teamB.toInt()) {
                                    showBlueText(dialogData?.teamThreeFirst)
                                    showGreyText(dialogData?.teamThreeSecond)
                                } else if (user.score[2].teamA.toInt() == user.score[2].teamB.toInt()) {
                                    showGreyText(dialogData?.teamThreeFirst)
                                    showGreyText(dialogData?.teamThreeSecond)

                                } else {
                                    showBlueText(dialogData?.teamThreeSecond)
                                    showGreyText(dialogData?.teamThreeFirst)
                                }
                            }
                        }
                    }
                    // <----------------------------------------->


                    // <------for badge icon visibility ------->
                    if (user.win_status != null) {
                        if (user.win_status.equals("1")) {           // for win
                            dialogData?.teamF_winTag?.visibility = View.VISIBLE
                            dialogData?.teamS_winTag?.visibility = View.INVISIBLE
                        } else if (user.win_status.equals("2")) {     // defeat
                            dialogData?.teamF_winTag?.visibility = View.INVISIBLE
                            dialogData?.teamS_winTag?.visibility = View.VISIBLE
                        } else {
                            dialogData?.teamF_winTag?.visibility = View.INVISIBLE
                            dialogData?.teamS_winTag?.visibility = View.INVISIBLE
                        }
                    }
                    // </------------------------------------------->


                    // <------------- for team players --------->
                    if (user.court_feature != null) {
                        if (user.court_feature.equals("Double")) {

                            // if teams has four players
                            for (i in user.data.indices) {
                                if (user.data[i].player_key == "1") {
                                    setDataOnImageSecForPopup(
                                        dialogData?.circularImg!!, dialogData?.titlePop!!,
                                        dialogData?.scorePop!!, user.data[i]
                                    )
                                } else if (user.data[i].player_key == "2") {
                                    setDataOnImageSecForPopup(
                                        dialogData?.circularImgTwo!!, dialogData?.titleTwo!!,
                                        dialogData?.scoreTwoPop!!, user.data[i]
                                    )
                                } else if (user.data[i].player_key == "3") {
                                    setDataOnImageSecForPopup(
                                        dialogData?.circularImgThreePop!!,
                                        dialogData?.titleThreePop!!,
                                        dialogData?.scoreThreePop!!,
                                        user.data[i]
                                    )
                                } else {
                                    setDataOnImageSecForPopup(
                                        dialogData?.circularImgFour!!, dialogData?.titleFourPop!!,
                                        dialogData?.scoreFourPop!!, user.data[i]
                                    )
                                }
                            }
                        } else {
                            // if team has two players
                            dialogData?.imgFirstSec?.visibility = View.INVISIBLE
                            dialogData?.imgForthSec?.visibility = View.INVISIBLE


                            for (i in user.data.indices) {
                                if (user.data[i].player_key == "1") {
                                    setDataOnImageSecForPopup(
                                        dialogData?.circularImgTwo!!, dialogData?.titleTwo!!,
                                        dialogData?.scoreTwoPop!!, user.data[i]
                                    )

                                } else if (user.data[i].player_key == "2") {
                                    setDataOnImageSecForPopup(
                                        dialogData?.circularImgThreePop!!,
                                        dialogData?.titleThreePop!!,
                                        dialogData?.scoreThreePop!!,
                                        user.data[i]
                                    )
                                }
                            }
                        }
                    }
                    // <--------------------------------------->


                } else {
                    showToast(this@NotificationActivity, user.message)
                }
            } else {
                showToast(this@NotificationActivity, getString(R.string.something_went_wrong))
            }
        })


        dialogData.tvResults?.setOnClickListener {
            // <----------- api for accepting score --->
            if (!isNetworkConnected()) {
                showInternetToast()
            } else {
                scoreRequestAccept = 1
                notifyVM?.acceptRejectScore(this, data.match_id, "1") // 1 -> for accept
            }

        }
        dialogData.imgCard?.setOnClickListener {
            if (!isNetworkConnected()) {
                showInternetToast()
            } else {
                scoreRequestAccept = 2
                notifyVM?.acceptRejectScore(this, data.match_id, "2") // 2 -> for reject
            }
        }

        dialogData.show()
    }


    private fun showBlueText(teamOneFirst: TextView?) {
        teamOneFirst?.setTextColor(ContextCompat.getColor(this, R.color.theme_blue))
    }

    private fun showGreyText(teamOneFirst: TextView?) {
        teamOneFirst?.setTextColor(ContextCompat.getColor(this, R.color.contact_clr))
    }

    private fun setDataOnImageSecForPopup(
        circularImg: CircleImageView, titlePop: TextView,
        scorePop: TextView, data: Data
    ) {
        if (data.user_type == "1") {                     // user role
            if (data.image_file != null) {
                Glide.with(this)
                    .load(data.image_file)
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)// image url
                    //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                    //.error(R.drawable.profileimg_home)  // any image in case of error
                    .into(circularImg)
                //  plusIcon.visibility = View.GONE
            } else {
                // plusIcon.visibility = View.VISIBLE
                circularImg.setImageDrawable(null)
            }
        } else {                                                   // host role
            if (data.image_file != null) {

                Glide.with(this)
                    .load(data.image_file)
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)// image url
                    //.placeholder(R.drawable.profileimg_home)  // any placeholder to load at start
                    //.error(R.drawable.profileimg_home)        // any image in case of error
                    .into(circularImg)
                //  plusIcon.visibility = View.GONE
            } else {
                //  plusIcon.visibility = View.VISIBLE
                circularImg.setImageDrawable(null)
            }
        }

        if (data.name != null) {
            titlePop.setText(data.name)
        }
        if (data.score != null) {
            scorePop.setText(data.score)
        }

    }

    override fun onRefresh() {
        if (isNetworkConnected()) {
            notifyVM?.notificationListData(this)
        } else {

            showInternetToast()
        }
    }
}