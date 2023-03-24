package snow.app.padelbook.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_games.*
import kotlinx.android.synthetic.main.activity_games.bottomSec
import kotlinx.android.synthetic.main.activity_games.cancelPolicyText
import kotlinx.android.synthetic.main.activity_games.cancelText
import kotlinx.android.synthetic.main.activity_games.clHeader
import kotlinx.android.synthetic.main.activity_games.description
import kotlinx.android.synthetic.main.activity_games.ivBack
import kotlinx.android.synthetic.main.activity_games.mainSec
import kotlinx.android.synthetic.main.activity_games.separatorToolbar
import kotlinx.android.synthetic.main.activity_games.titleFour
import kotlinx.android.synthetic.main.activity_games.titleOne
import kotlinx.android.synthetic.main.activity_games.titleThree
import kotlinx.android.synthetic.main.activity_games.tvCourtName
import kotlinx.android.synthetic.main.activity_games.tvScore
import kotlinx.android.synthetic.main.activity_games.tvTime
import kotlinx.android.synthetic.main.activity_games.tvTitle
import kotlinx.android.synthetic.main.activity_games.tvTitleToolbar
import kotlinx.android.synthetic.main.activity_games.tvTitleTwo
import kotlinx.android.synthetic.main.cancel_succeded_popup.*
import kotlinx.android.synthetic.main.popup_cancel_booking.*
import kotlinx.android.synthetic.main.popup_cancel_booking.tvCancel
import kotlinx.android.synthetic.main.popup_cancel_request.*
import kotlinx.android.synthetic.main.popup_game_summary.*
import kotlinx.android.synthetic.main.popup_game_summary.tvTile
import kotlinx.android.synthetic.main.toolbar_icon.view.ivBack
import kotlinx.android.synthetic.main.toolbar_icon.view.profile_image
import snow.app.padelbook.R
import snow.app.padelbook.adapter.GamesAdapter
import snow.app.padelbook.common.IMAGES_URL
import snow.app.padelbook.fragments.ContactActivity
import snow.app.padelbook.fragments.ContactActivity.Companion.callBackToAddMember
import snow.app.padelbook.network.responses.courtresponse.CategoryData
import snow.app.padelbook.network.responses.courtresponse.ClubTime
import snow.app.padelbook.network.responses.courtresponse.CourtData
import snow.app.padelbook.network.responses.singleMatchDetail.Data
import snow.app.padelbook.network.responses.singleMatchDetail.PlayerLisr
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.ui.cardscreen.CardListingActivity
import snow.app.padelbook.viewModel.CreateBookingVM
import snow.app.padelbook.viewModel.NotificationVM
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GamesActivity : BaseActivity(), GamesAdapter.OnClickListener {
    var matchType = 0
    private var tempData: Data? = null
    private var ttt: String = "1"
    private var mMatchDate: String = ""
    private var adapter: GamesAdapter? = null
    private var createBookVM: CreateBookingVM? = null
    private var loginPref: SessionClass? = null
    var list: ArrayList<String> = ArrayList()
    var isSelectedCancel = true
    var selectedDate: String? = null
    var selectedTime: ClubTime? = null
    var selectedAvailable_CateID: CategoryData? = null
    var courtData: CourtData? = null
    var paymentMethod = "1"
    var paymentType = "1"
    var matchID: String? = ""
    var playerKeyID: String? = ""
    var bookingTime: String? = null
    var bookingID = ""
    var playerKey = ""
    var callFromHome_Data: Data? = null
    var tt = ""
    var notifyVM: NotificationVM? = null
    var MatchType = ""
    var call_from: String? = null
    var payAmonuntAlready = ""
    var matchInPast: Boolean = false
    var isWebsiteMatch: Boolean = false
    var isMatchScoreAdded: Boolean = false
    var playerListSize: Int = 0
    var matchTypeCourt: String = ""
    var isClubPass: String? = "0"
    var club_pass_id: String? = "0"
    var paid_price: String? = "0"
    var isUserSelected = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)
        list.clear()
        loginPref = SessionClass(this)
        notifyVM = NotificationVM()
        createBookVM = CreateBookingVM()
        call_from = intent?.getStringExtra("CALL_FROM")
        if (call_from.equals("StartAMatch")) {
            MatchType = "StartAMatch"
        } else {
            MatchType = ""
        }
        tvCancelNew.setOnClickListener {
            showDialogCancelTwoNew(tempData!!)
        }
        getDataThroughIntent()        // when call from court fragment
        setToolbar()
        setClick()


        setImageData(R.drawable.ic_group_231, R.color.light_grey)
        setData()                     // set data according to call From which fragment
        listener()

        imgCard.setOnClickListener {

            Log.e("dd", "" + imgCard.drawable.constantState)
            Log.e("dd", "" + resources.getDrawable(R.drawable.ic_group_231).constantState)
            // booking cancel by owner

            if (ttt.equals("2")) {
                // showConfirmDialog()
                showDialogCancelTwoNew2(tempData!!)
                // showDialogCancal(tempData!!)
            } else {
                //  paymentType = "2" //online
                //online

                if (MatchType.equals("StartAMatch")) {
                    matchType = 0
                } else {
                    matchType = 1
                }
                paymentMethod = "2"//(pay_type)
                //  tvPay.performClick()
                Log.e("timeee", "--" + selectedAvailable_CateID)
                Log.e("match type", "games--" + matchType)
                Log.e("courtData", "games--" + courtData)
                startActivityForResult(
                    Intent(this, CardListingActivity::class.java)
                        .putExtra("bookingid", bookingID).putExtra("matchId", matchID)
                        .putExtra("type", "booking")
                        .putExtra("paymentMethod", paymentMethod)
                        .putExtra("paymentType", paymentType)
                        .putExtra("courtData", Gson().toJson(courtData))
                        .putExtra("selectedTime", Gson().toJson(selectedTime))
                        .putExtra("match_type", matchType)
                        .putExtra("paid_price", paid_price)
                        .putExtra("isClubPass", isClubPass)
                        .putExtra("club_pass_id", club_pass_id)
                        .putExtra("selectedDate", selectedDate)
                        .putExtra(
                            "selectedAvailable_CateID",
                            Gson().toJson(selectedAvailable_CateID)
                        ), 1


                )
            }
        }
        /*  imageWhiteTwo.setOnClickListener {
              if (intent.hasExtra("from")) {
                  Log.e("ddd", "11111")
              } else if (intent.hasExtra("FromBookingList")) {     // call from booking list
                  Log.e("ddd", "2222")
              } else {

                  if (intent.hasExtra("from") || intent.hasExtra("FromBookingList")
                      || callFraomHome_AsHost == "host"
                  ) {
                      circularImgTwo.performClick()
                  }
                  else
                  {

                  titleTwo.setText("")
                  titleThree.setText("")
                  titleFour.setText("")
                  tvScoreTwo.setText("")
                  tvScoreThree.setText("")
                  tvScoreFour.setText("")

                  circularImgThree.setImageResource(0)
                  circularImgFour.setImageResource(0)
                  circularImgTwo.setImageResource(0)
                  plusTextFour.visibility=View.VISIBLE
                  plusTextThree.visibility=View.VISIBLE
                  playerKeyID = "2"
                  createBookVM?.singleMatchDetailData(this, matchID!!)
              }
              }
          }
          imageWhiteThree.setOnClickListener {
              if (intent.hasExtra("from")) {
                  Log.e("ddd", "11111")
              } else if (intent.hasExtra("FromBookingList")) {     // call from booking list
                  Log.e("ddd", "2222")
              } else {
                  if (intent.hasExtra("from") || intent.hasExtra("FromBookingList")
                      || callFraomHome_AsHost == "host"
                  ) {
                      circularImgThree.performClick()
                  }
                  else
                  {
                  titleTwo.setText("")
                  titleThree.setText("")
                  titleFour.setText("")
                  tvScoreTwo.setText("")
                  tvScoreThree.setText("")
                  tvScoreFour.setText("")

                  circularImgThree.setImageResource(0)
                  circularImgFour.setImageResource(0)
                  circularImgTwo.setImageResource(0)
                  plusTextFour.visibility=View.VISIBLE
                  plusTextTwo.visibility=View.VISIBLE
                  playerKeyID = "3"
                  createBookVM?.singleMatchDetailData(this, matchID!!)
              }
              }
          }
          imageWhiteFour.setOnClickListener {
              if (intent.hasExtra("from")) {

              } else if (intent.hasExtra("FromBookingList")) {     // call from booking list

              } else {
                  if (intent.hasExtra("from") || intent.hasExtra("FromBookingList")
                      || callFraomHome_AsHost == "host"
                  ) {
                      circularImgFour.performClick()
                  }
                  else
                  {
                  titleTwo.setText("")
                  titleThree.setText("")
                  titleFour.setText("")
                  tvScoreTwo.setText("")
                  tvScoreThree.setText("")
                  tvScoreFour.setText("")

                  circularImgThree.setImageResource(0)
                  circularImgFour.setImageResource(0)
                  circularImgTwo.setImageResource(0)
                  plusTextTwo.visibility=View.VISIBLE
                  plusTextThree.visibility=View.VISIBLE
                  playerKeyID = "4"
                  createBookVM?.singleMatchDetailData(this, matchID!!)
              }
              }
          }*/
    }

    override fun onResume() {
        super.onResume()
        //  paymentType = "1"
        paymentMethod = "1" //pay at club
        // hit api again when new member added
        if (callBackToAddMember.equals("1")) {
            if (!isNetworkConnected()) {
                showInternetToast()
            } else {
                callBackToAddMember = ""
                createBookVM?.singleMatchDetailData(this, matchID!!)

            }
        }
    }


    private fun getDataThroughIntent() {
        // when call from court
        courtData = Gson().fromJson(intent.getStringExtra("CourtData"), CourtData::class.java)
        Log.d("selectedData", courtData.toString())

        selectedDate = intent.extras?.get("SelectedDate") as String?
        Log.d("selectedTime", selectedDate.toString())

        selectedTime = Gson().fromJson(intent.getStringExtra("SelectedTime"), ClubTime::class.java)
        Log.d("selectedTime", selectedTime.toString())

        selectedAvailable_CateID = Gson().fromJson(
            intent.getStringExtra("selectedAvailableData"),
            CategoryData::class.java
        )
        selectedAvailable_CateID?.is_club_pass?.let {
            isClubPass = selectedAvailable_CateID?.is_club_pass.toString()
        } ?: kotlin.run {
            isClubPass = "0"
        }
        selectedAvailable_CateID?.club_pass?.let {
            club_pass_id = selectedAvailable_CateID?.club_pass?.id.toString()
        } ?: kotlin.run {
            club_pass_id = "0"
        }

        if (selectedDate != null) {
            val myFormat =
                SimpleDateFormat("HH:mm").parse(selectedTime?.start_time)
            val cal: Calendar = Calendar.getInstance()
            cal.setTime(myFormat)
            selectedAvailable_CateID?.time?.toInt()?.let { cal.add(Calendar.MINUTE, it) }
            val newTime: String =
                SimpleDateFormat("HH:mm").format(cal.getTime())
            Log.e("new  eeee", newTime)
            var fullDate =
                dateFormatWithMonthNameFull(selectedDate!!) + ", " + selectedTime?.start_time + "-" + newTime

            if (fullDate.contains("Mon")) {
                tvTitle.setText(fullDate.replace("Monday", getString(R.string.monday)))
            } else

                if (fullDate.contains("Tues")) {
                    tvTitle.setText(fullDate.replace("Tuesday", getString(R.string.tuesday)))
                } else
                    if (fullDate.contains("Wed")) {
                        tvTitle.setText(fullDate.replace("Wednesday", getString(R.string.wednes)))
                    } else

                        if (fullDate.contains("Thurs")) {
                            tvTitle.setText(fullDate.replace("Thursday", getString(R.string.thurs)))
                        } else

                            if (fullDate.contains("Frid")) {
                                tvTitle.setText(
                                    fullDate.replace(
                                        "Friday",
                                        getString(R.string.friday)
                                    )
                                )
                            } else
                                if (fullDate.contains("Satur")) {
                                    tvTitle.setText(
                                        fullDate.replace(
                                            "Saturday",
                                            getString(R.string.satu)
                                        )
                                    )
                                } else

                                    if (fullDate.contains("Sund")) {
                                        tvTitle.setText(
                                            fullDate.replace(
                                                "Sunday",
                                                getString(R.string.sunday)
                                            )
                                        )
                                    }


        }
        tvTitleTwo.setText(courtData?.clubName)


        if (courtData?.court_type.equals("Open")) {
            tvCourtName.setText(courtData?.name + " | " + getString(R.string.open))
        } else
            if (courtData?.court_type.equals("Roofed")) {
                tvCourtName.setText(courtData?.name + " | " + getString(R.string.roofed))
            } else
                if (courtData?.court_type.equals("Indoors")) {
                    tvCourtName.setText(courtData?.name + " | " + getString(R.string.indoors))
                } else {
                    tvCourtName.setText(courtData?.name + " | " + courtData?.court_type)
                }
        // tvCourtName.setText(courtData?.name + " | " + courtData?.court_type)
        tvTime.setText(selectedAvailable_CateID?.time + " min")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("reseeess", resultCode.toString())
        matchID = data?.getStringExtra("matchId")
        if (resultCode == Activity.RESULT_CANCELED) {
        } else {
            if (requestCode == 0) {// match

                showDialogFromHome()
            } else if (requestCode == 1) {// booking
                showDialog()

            }
        }
    }

    private fun listener() {          // when call from court
        createBookVM?.createBooking?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    if (paymentMethod.equals("2")) {
                        if (user.data != null) {
                            if (user.data.match_id != null) {
                                matchID = user.data.match_id
                            }
                            if (user.data.id != null) {
                                bookingID = user.data.id
                            }
                            /*    startActivityForResult(
                                    Intent(
                                        this,
                                        CardListingActivity::class.java
                                    ).putExtra("bookingid", user.data.id).putExtra("matchId", matchID)
                                        .putExtra("type", "booking"), 1
                                )*/

                            if (MatchType.equals("StartAMatch")) {
                                matchType = 0
                            } else {
                                matchType = 1
                            }
                            Log.e("timeee", "--" + selectedAvailable_CateID)
                            Log.e("match type", "games--" + matchType)
                            Log.e("courtData", "games--" + courtData)
                            startActivityForResult(
                                Intent(this, CardListingActivity::class.java)
                                    .putExtra("bookingid", bookingID).putExtra("matchId", matchID)
                                    .putExtra("type", "booking")
                                    .putExtra("paymentMethod", paymentMethod)
                                    .putExtra("paymentType", paymentType)
                                    .putExtra("courtData", Gson().toJson(courtData))
                                    .putExtra("selectedTime", Gson().toJson(selectedTime))
                                    .putExtra("match_type", matchType)
                                    .putExtra("paid_price", paid_price)
                                    .putExtra("isClubPass", isClubPass)
                                    .putExtra("club_pass_id", club_pass_id)
                                    .putExtra("selectedDate", selectedDate)
                                    .putExtra(
                                        "selectedAvailable_CateID",
                                        Gson().toJson(selectedAvailable_CateID)
                                    ), 1
                            )

                        }
                    } else {
                        if (user.data != null) {
                            showToast(this, user.message)
                            if (user.data.match_id != null) {
                                matchID = user.data.match_id
                            }
                            if (user.data.id != null) {
                                bookingID = user.data.id
                            }
                            showDialog()
                        } else {
                            showToast(this, "No Data Found")
                        }
                    }
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, "Something went wrong")
            }
        })

        // <-----------create match response --------->    // when call from home
        createBookVM?.createMatch?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {

                    if (paymentMethod.equals("2")) {
                        if (user.data != null) {
                            /*   startActivityForResult(
                                   Intent(
                                       this,
                                       CardListingActivity::class.java
                                   ).putExtra("bookingid", user.data.id).putExtra("matchId", matchID)
                                       .putExtra("type", "match"), 0
                               )*/

                            if (MatchType.equals("StartAMatch")) {
                                matchType = 0
                            } else {
                                matchType = 1
                            }

                            Log.e("timeee", "--" + selectedAvailable_CateID)
                            Log.e("match type", "games--" + matchType)
                            Log.e("courtData", "games--" + courtData)
                            startActivityForResult(
                                Intent(this, CardListingActivity::class.java)
                                    .putExtra("bookingid", bookingID).putExtra("matchId", matchID)
                                    .putExtra("type", "booking")
                                    .putExtra("paymentMethod", paymentMethod)
                                    .putExtra("paymentType", paymentType)
                                    .putExtra("courtData", Gson().toJson(courtData))
                                    .putExtra("selectedTime", Gson().toJson(selectedTime))
                                    .putExtra("match_type", matchType)
                                    .putExtra("paid_price", paid_price)
                                    .putExtra("isClubPass", isClubPass)
                                    .putExtra("club_pass_id", club_pass_id)
                                    .putExtra("selectedDate", selectedDate)
                                    .putExtra(
                                        "selectedAvailable_CateID",
                                        Gson().toJson(selectedAvailable_CateID)
                                    ), 1
                            )
                        }
                    } else {
                        showToast(this, user.message)
                        showDialogFromHome()
                    }
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, "Something went wrong")
            }
        })

        // <------- response single match detail -------------->   // when call from home
        createBookVM?.singleMatchDetail?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    isWebsiteMatch = user.isWebsiteMatch
                    matchInPast = user.matchInPast
                    user.data?.let {

                        tempData = user.data
                        mMatchType = user.data.match_type
                        isMatchScoreAdded = user.isMatchScoreAdded
                        cancellationTitle.text =
                            getString(R.string._24) + " " + user.data.cancel_policy + " " + getString(
                                R.string._24_
                            )
                        cancellationTitlee.text =
                            getString(R.string._24) + " " + user.data.cancel_policy + " " + getString(
                                R.string._24_
                            )
                        if (intent.hasExtra("from")) {


                            // setAdapter("from", courtData?.court_feature, user.data.match_type)
                            Log.e("ddd", "11111")
                        } else if (intent.hasExtra("FromBookingList")) {     // call from booking list
                            Log.e("ddd", "2222")
                        } else {
                            Log.e("ddd", "3333")
                            tempData = user.data
                            var courtFeat = intent.extras?.get("courtFeatureType") as String?

                            if (!SessionClass(this).loginData.id.equals(user.data.user_id)) {
                                var rr = ""
                                for (i in 0..user.data.player_Lisr.size - 1) {
                                    if (user.data.player_Lisr.get(i).customer_id.toString()
                                            .equals(SessionClass(this).loginData.id)
                                    ) {
                                        rr = "1"
                                        recycleSec.visibility = View.GONE
                                        break
                                    }
                                }

                                if (rr.equals("")) {
                                    if (playerKeyID.equals("2")) {

                                        if (courtFeat.equals("Double")) {

                                            Glide.with(this)
                                                .load(SessionClass(this).loginData.image_file)
                                                .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                                .error(R.drawable.ic_user)// image url
                                                .into(circularImgTwo)
                                            titleTwo.setText(SessionClass(this).loginData.name)
                                            plusTextTwo.visibility = View.GONE
                                            imageWhiteTwo.visibility = View.VISIBLE
                                        } else {
                                            Glide.with(this)
                                                .load(SessionClass(this).loginData.image_file)
                                                .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                                .error(R.drawable.ic_user)// image url
                                                .into(circularImgThree)
                                            titleThree.setText(SessionClass(this).loginData.name)

                                            plusTextThree.visibility = View.GONE
                                            imageWhiteThree.visibility = View.VISIBLE
                                        }

                                    }

                                    if (playerKeyID.equals("3")) {
                                        Glide.with(this)

                                            .load(SessionClass(this).loginData.image_file)
                                            .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                            .error(R.drawable.ic_user)// image url
                                            .into(circularImgThree)
                                        titleThree.setText(SessionClass(this).loginData.name)
                                        plusTextThree.visibility = View.GONE
                                        imageWhiteThree.visibility = View.VISIBLE
                                    }

                                    if (playerKeyID.equals("4")) {
                                        Glide.with(this)
                                            .load(SessionClass(this).loginData.image_file)
                                            .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                            .error(R.drawable.ic_user)// image url
                                            .into(circularImgFour)
                                        titleFour.setText(SessionClass(this).loginData.name)
                                        plusTextFour.visibility = View.GONE
                                        imageWhiteFour.visibility = View.VISIBLE

                                    }
                                }
                            } else {
                                Log.e("ddd", "visbile")
                            }

                        }

                        //new code inside
                        if (user.data.match_type != null) {

                            if (user.data.match_type.equals("0")) {

                                lockImgFour.visibility = View.GONE
                                lockImgThree.visibility = View.GONE
                                lockImgTwo.visibility = View.GONE
                            } else {
                                if (tempData!!.court_feature.equals("Double")) {
                                    //new code inside
                                    lockImgFour.visibility = View.VISIBLE
                                    lockImgThree.visibility = View.VISIBLE
                                    lockImgTwo.visibility = View.VISIBLE
                                } else {
                                    lockImgFour.visibility = View.VISIBLE
                                    lockImgThree.visibility = View.VISIBLE

                                }
                            }
                        }

                        list.clear()
                        if (user.data.user_id.equals(loginPref?.loginData?.id)) {
                            callFraomHome_AsHost = "host"

                            /*    val df = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
                                try {
                                    val date = df.parse(date)
                                    val dateFormat = SimpleDateFormat("EEEE, dd/MM", Locale.ENGLISH)
                                    return dateFormat.format(date)
                                } catch (ex: ParseException) {
                                    ex.message?.let { Log.d("Parse Exception", it) }
                                }
                                */

                            val myFormat =
                                SimpleDateFormat("EEEE dd MMM , HH:mm", Locale.ENGLISH)
                            val dateFormat: DateFormat =
                                SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz", Locale.ENGLISH)
                            var myDate: String? = null

                            try {
                                val date = myFormat.parse(user.data.date + " , " + user.data.time)
                                myDate =
                                    SimpleDateFormat(
                                        "EEE MMM dd HH:mm:ss zzzz",
                                        Locale.ENGLISH
                                    ).format(date)
                                Log.e("eeee", myDate)
                            } catch (ex: ParseException) {
                                ex.message?.let { Log.d("Parse Exception", it) }
                            }


                            try {
                                val d  =
                                    SimpleDateFormat(
                                        "EEE MMM dd HH:mm:ss zzzz",
                                        Locale.ENGLISH
                                    )
                                val dd = d.parse(
                                    myDate
                                )
                                val cal: Calendar = Calendar.getInstance()
                                cal.time = dd
                                // cal.add(Calendar.MINUTE, user.data.booking_time.toInt())
                                val newTime: String =
                                    SimpleDateFormat(
                                        "EEE MMM dd HH:mm:ss zzzz",
                                        Locale.ENGLISH
                                    ).format(
                                        cal.getTime()
                                    )
                                Log.e("new  eeee", newTime)



                                mMatchDate = newTime


                            } catch (ex: ParseException) {
                                ex.message?.let { Log.d("Parse Exception", it) }
                            }
                            val call = Calendar.getInstance()
                            System.out.println(dateFormat.format(call.time))

                            Log.e("new 2 eeee", dateFormat.format(call.time))
                            if (dateFormat.parse(mMatchDate)
                                    .after(dateFormat.parse(dateFormat.format(call.time)))
                            ) {

                                tvPay.background.setColorFilter(
                                    ContextCompat.getColor(this, R.color.btn_grey_clr),
                                    PorterDuff.Mode.SRC_ATOP
                                )

                                imgCard.background.setColorFilter(
                                    ContextCompat.getColor(this, R.color.red_color),
                                    PorterDuff.Mode.SRC_ATOP
                                )

                            } else {
                                imgCard.visibility = View.GONE
                            }
                            if (intent.hasExtra("forback")) {
                                bookingScreenDataThree()
                            } else {
                                bookingScreenData()
                            }
                            // when call from home as host
                        } else {
                            callFraomHome_AsHost = ""
                            //   if(fromHomeDialog == "1"){

                            if (user.data.bookiing_status != null) {
                                //new code inside
                                if (user.data.bookiing_status.equals("2")) {     //   2 -> accept
                                    tvTitleToolbar.setText(getString(R.string.booking_details))
                                    mainSec.visibility = View.VISIBLE
                                    bottomSecNew.visibility = View.VISIBLE
                                    recycleSec.visibility = View.GONE


                                    bottomSec.visibility = View.GONE

                                } else if (user.data.bookiing_status.equals("1")) {                                     //    0 -> pending
                                    fromHomeDialog = ""
                                    recycleSec.visibility = View.GONE
                                    showDialogCancal(user.data)

                                } else {
                                    //   joinMatchScreenData(user.data.pay_type)
                                    joinMatchScreenData(user.data.payment_type)

                                }
                            }
                            //   }
                        }


                        if (user.data.bookiing_id != null) {
                            bookingID = user.data.bookiing_id
                        }
                        callFromHome_Data = user.data

                        //  payAmonuntAlready = user.data.pay_type
                        payAmonuntAlready = user.data.payment_type

                        // if (user.data.pay_type.equals("1")) {
                        if (user.data.payment_type.equals("1") || user.data.payment_type.equals("2")) {
                            //new code inside
                            list.add(getString(R.string.pay_your_part))
                            Log.e("ddd", "6666666")
                            recycleSec.visibility = View.VISIBLE
                            if (user.data.player_Lisr.get(0).customer_id.toString()
                                    .equals(SessionClass(this).loginData.id) && user.data.player_Lisr.get(
                                    0
                                ).player_key.toInt() != 0
                            ) {
                                recycleSec.visibility = View.GONE

                            }

                            if (tt.equals("1")) {
                                showDialog()
                                var rr = ""
                                for (i in 0..user.data.player_Lisr.size - 1) {
                                    if (user.data.player_Lisr.get(i).customer_id.toString()
                                            .equals(SessionClass(this).loginData.id)
                                    ) {
                                        recycleSec.visibility = View.GONE
                                        break
                                    }
                                }
                            }

                        } else {
                            //new code inside
                            Log.e("ddd", "8888")
                            recycleSec.visibility = View.GONE
                            list.add(getString(R.string.pay_your_part))
                        }

                        if (user.data.time != null && user.data.date != null) {

                            var fullDate = user.data.date + ", " + user.data.time

                            if (fullDate.contains("Mon")) {
                                tvTitle.setText(
                                    fullDate.replace(
                                        "Monday",
                                        getString(R.string.monday)
                                    )
                                )
                            } else

                                if (fullDate.contains("Tues")) {
                                    tvTitle.setText(
                                        fullDate.replace(
                                            "Tuesday",
                                            getString(R.string.tuesday)
                                        )
                                    )
                                } else
                                    if (fullDate.contains("Wed")) {
                                        tvTitle.setText(
                                            fullDate.replace(
                                                "Wednesday",
                                                getString(R.string.wednes)
                                            )
                                        )
                                    } else

                                        if (fullDate.contains("Thurs")) {
                                            tvTitle.setText(
                                                fullDate.replace(
                                                    "Thursday",
                                                    getString(R.string.thurs)
                                                )
                                            )
                                        } else

                                            if (fullDate.contains("Frid")) {
                                                tvTitle.setText(
                                                    fullDate.replace(
                                                        "Friday",
                                                        getString(R.string.friday)
                                                    )
                                                )
                                            } else
                                                if (fullDate.contains("Satur")) {
                                                    tvTitle.setText(
                                                        fullDate.replace(
                                                            "Saturday",
                                                            getString(R.string.satu)
                                                        )
                                                    )
                                                } else

                                                    if (fullDate.contains("Sund")) {
                                                        tvTitle.setText(
                                                            fullDate.replace(
                                                                "Sunday",
                                                                getString(R.string.sunday)
                                                            )
                                                        )
                                                    }


                            //  tvTitle.setText(user.data.date + ", " + user.data.time)
                        }
                        if (user.data.clubName != null) {
                            tvTitleTwo.setText(user.data.clubName)
                        }
                        if (user.data.courtname != null && user.data.club_status != null) {

                            if (user.data.court_type.equals("Open")) {
                                tvCourtName.setText(user.data.courtname + " | " + getString(R.string.open))
                            } else
                                if (user.data.court_type.equals("Roofed")) {
                                    tvCourtName.setText(user.data.courtname + " | " + getString(R.string.roofed))
                                } else
                                    if (user.data.court_type.equals("Indoors")) {
                                        tvCourtName.setText(
                                            user.data.courtname + " | " + getString(
                                                R.string.indoors
                                            )
                                        )
                                    } else {
                                        tvCourtName.setText(user.data.courtname + " | " + user.data.court_type)
                                    }


                        }
                        if (user.data.booking_time != null) {
                            bookingTime = user.data.booking_time
                            tvTime.setText(user.data.booking_time + " min")
                        }

                        if (user.data.court_feature != null) {
                            courtFeature = user.data.court_feature

                            setAdapter("", user.data.court_feature, user.data.match_type)


                            if (user.data.court_feature.equals("Double")) {
                                if (tempData!!.user_id.equals(loginPref!!.loginData.id)) {
                                    if (tempData!!.match_type.equals("0")) {
                                        dataTxt.text = getString(R.string.txt2)
                                    } else {
                                        dataTxt.text = getString(R.string.text1)

                                    }
                                } else {

                                    if (tempData!!.court_feature.toLowerCase().equals("single")) {
                                        dataTxt.text = getString(R.string.txt4)
                                    } else {
                                        dataTxt.text = getString(R.string.txt3)

                                    }

                                }
                                //  dataTxt.text = resources.getString(R.string.games_data)
                                // if team have four members
                                for (i in user.data.player_Lisr.indices) {
                                    if (user.data.player_Lisr[i].player_key.equals("1")) {
                                        //new code inside
                                        playerKey = "1"
                                        circularImg.setPadding(4, 4, 4, 4)

                                        setDataOnImageSec(
                                            circularImg,
                                            titleOne,
                                            tvScore,
                                            user.data.player_Lisr[i],
                                            imageWhiteOne,
                                            lockImg,
                                            plusTextOne, circularImgBorder
                                        )
                                    } else if (user.data.player_Lisr[i].player_key.equals("2")) {
                                        playerKey = "2"
                                        setDataOnImageSec(
                                            circularImgTwo,
                                            titleTwo,
                                            tvScoreTwo,
                                            user.data.player_Lisr[i],
                                            imageWhiteTwo,
                                            lockImgTwo,
                                            plusTextTwo, circularImgBorderTwo
                                        )
                                    } else if (user.data.player_Lisr[i].player_key.equals("3")) {
                                        playerKey = "3"
                                        setDataOnImageSec(
                                            circularImgThree,
                                            titleThree,
                                            tvScoreThree,
                                            user.data.player_Lisr[i],
                                            imageWhiteThree,
                                            lockImgThree,
                                            plusTextThree, circularImgBorderThree
                                        )
                                    } else if (user.data.player_Lisr[i].player_key.equals("4")) {
                                        playerKey = "4"
                                        setDataOnImageSec(
                                            circularImgFour,
                                            titleFour,
                                            tvScoreFour,
                                            user.data.player_Lisr[i],
                                            imageWhiteFour,
                                            lockImgFour,
                                            plusTextFour, circularImageFourBorder
                                        )
                                    }
                                }
                            } else {
                                if (tempData!!.user_id.equals(loginPref!!.loginData.id)) {
                                    if (tempData!!.match_type.equals("0")) {
                                        dataTxt.text = getString(R.string.txt2)
                                    } else {
                                        dataTxt.text = getString(R.string.text1)

                                    }
                                } else {

                                    if (tempData!!.court_feature.toLowerCase().equals("single")) {
                                        dataTxt.text = getString(R.string.txt4)
                                    } else {
                                        dataTxt.text = getString(R.string.txt3)

                                    }

                                }
                                //  dataTxt.text = resources.getString(R.string.games_data2)
                                // if team has two members
                                imgFirstSec.visibility = View.INVISIBLE
                                imgForthSec.visibility = View.INVISIBLE

                                for (i in user.data.player_Lisr.indices) {

                                    if (user.data.player_Lisr[i].player_key.equals("1")) {
                                        circularImgTwo.setPadding(4, 4, 4, 4)
                                        playerKey = "1"
                                        setDataOnImageSec(
                                            circularImgTwo,
                                            titleTwo,
                                            tvScoreTwo,
                                            user.data.player_Lisr[i],
                                            imageWhiteTwo,
                                            lockImgTwo,
                                            plusTextTwo, circularImgBorderTwo
                                        )
                                    } else if (user.data.player_Lisr[i].player_key.equals("2")) {
                                        playerKey = "2"
                                        setDataOnImageSec(
                                            circularImgThree,
                                            titleThree,
                                            tvScoreThree,
                                            user.data.player_Lisr[i],
                                            imageWhiteThree,
                                            lockImgThree,
                                            plusTextThree, circularImgBorderThree
                                        )
                                    }
                                }
                            }

                            //only in case of isWebsiteMatch and status !=2
                            if (user.data.bookiing_status != "2") {
                                // match created from website
                                if (user.isWebsiteMatch) {
                                    // if team have four members

                                    if (user.playerKeyToShow == 1) {
                                        playerKey = "1"
                                        circularImg.setPadding(4, 4, 4, 4)


                                        setDataOnImageSec(
                                            circularImg,
                                            titleOne,
                                            tvScore,
                                            null,
                                            imageWhiteOne,
                                            lockImg,
                                            plusTextOne, circularImgBorder
                                        )
                                    } else if (user.playerKeyToShow == 2) {
                                        playerKey = "2"
                                        setDataOnImageSec(
                                            circularImgTwo,
                                            titleTwo,
                                            tvScoreTwo,
                                            null,
                                            imageWhiteTwo,
                                            lockImgTwo,
                                            plusTextTwo, circularImgBorderTwo
                                        )
                                    } else if (user.playerKeyToShow == 3) {
                                        playerKey = "3"
                                        setDataOnImageSec(
                                            circularImgThree,
                                            titleThree,
                                            tvScoreThree,
                                            null,
                                            imageWhiteThree,
                                            lockImgThree,
                                            plusTextThree, circularImgBorderThree
                                        )
                                    } else if (user.playerKeyToShow == 4) {
                                        playerKey = "4"
                                        setDataOnImageSec(
                                            circularImgFour,
                                            titleFour,
                                            tvScoreFour,
                                            null,
                                            imageWhiteFour,
                                            lockImgFour,
                                            plusTextFour, circularImageFourBorder
                                        )
                                    }

                                }
                            }

                        }
                        if (user.data.bookiing_status.equals("2")) {     //   2 -> accept
                            tvTitleToolbar.setText(getString(R.string.booking_details))
                            mainSec.visibility = View.VISIBLE
                            tvCancelNew.visibility = View.VISIBLE
                            recycleSec.visibility = View.GONE
                            bottomSec.visibility = View.GONE
                            bottomSecNew.visibility = View.VISIBLE
                            if (user.isCancelAllowed == false) {
                                tvCancelNew.isEnabled = false
                                tvCancelNew.setBackgroundColor(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.light_grey
                                    )
                                )
                                tvCancelNew.setColorFilter(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.white
                                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                                );

                            } else {
                                tvCancelNew.isEnabled = true
                                tvCancelNew.setBackgroundColor(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.red_color
                                    )
                                )
                                tvCancelNew.setColorFilter(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.white
                                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                                );


                            }

                            if (user.data.court_feature == "Single") {
                                if (user.data.player_Lisr.size == 2 || user.data.match_type == "1") {
                                    description.visibility = View.GONE
                                } else {
                                    //new code inside
                                    description.visibility = View.VISIBLE

                                    //Old text
                                    /*obj.lblAbout.text = LocalizationSystem.sharedInstance.localizedStringForKey(key: "The player who started the match has to accept your request. The court will be confirmed as soon as 2 players have joined the match. You will get a confirmation.", comment: "")*/
                                    dataTxt.setText(getString(R.string.des_text_booking_deatils_not_confirmed))
                                }
                            } else if (user.data.court_feature == "Double") {
                                if (user.data.player_Lisr.size == 4 || user.data.match_type == "1") {
                                    description.visibility = View.GONE
                                } else {
                                    description.visibility = View.VISIBLE

                                    //Old text
                                    /*obj.lblAbout.text = LocalizationSystem.sharedInstance.localizedStringForKey(key: "The player who started the match has to accept your request. The court will be confirmed as soon as 2 players have joined the match. You will get a confirmation.", comment: "")*/
                                    dataTxt.setText(getString(R.string.des_text_booking_deatils_not_confirmed))
                                }
                            }
                            /*  if (mMatchType.equals("0")) {
                                  dataTxt.setText(getString(R.string.des_text_booking_deatils_not_confirmed))
                                  description.visibility = View.VISIBLE
                                *//*  if (user.isCancelAllowed){
                                    tvCancelNew.visibility = View.VISIBLE
                                }else{
                                    tvCancelNew.visibility = View.GONE
                                }*//*
                            }*/
                        }


                        if (user.data.match_type != null) {
                            if (user.data.match_type.equals("0")) {
                                lockImgFour.visibility = View.GONE
                                lockImgThree.visibility = View.GONE
                                lockImgTwo.visibility = View.GONE
                            } else {
                                if (user.data.court_feature.equals("Double")) {
                                    lockImgFour.visibility = View.VISIBLE
                                    lockImgThree.visibility = View.VISIBLE
                                    lockImgTwo.visibility = View.VISIBLE
                                } else {
                                    lockImgFour.visibility = View.VISIBLE
                                    lockImgThree.visibility = View.VISIBLE

                                }
                                /*   lockImgFour.visibility = View.VISIBLE
                                   lockImgThree.visibility = View.VISIBLE
                                   lockImgTwo.visibility = View.VISIBLE*/
                            }
                        }

                        if (tempData?.court_feature?.equals("Double")!! && tempData?.player_Lisr?.size!! < 4) {
                            tvPay.isEnabled = false
                            tvPay.setBackgroundTintList(getResources().getColorStateList(R.color.light_grey));


                        } else if (tempData?.court_feature?.equals("Single")!! && tempData?.player_Lisr?.size!! < 2) {

                            tvPay.isEnabled = false
                            tvPay.setBackgroundTintList(getResources().getColorStateList(R.color.light_grey));


                        } else {

                            tvPay.isEnabled = true
                            tvPay.setBackgroundTintList(getResources().getColorStateList(R.color.btn_clr_blue));

                        }

                        if (user.data.match_type.equals("0")) {
                            imgCard.visibility = View.GONE
                        }

                        val userSelected = user.data.player_Lisr.filter { it ->
                            it.customer_id.toString() == loginPref?.loginData?.id

                        }
                        if (userSelected.isNotEmpty()) {
                            isUserSelected = userSelected.first().customer_id
                        }
                        Log.e("isUserSelected", "--" + isUserSelected)
                    }
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, "Something went wrong")
            }
        })
        // <--------------------------------------------------->


        // <---------------cancel booking by owner  or user ---------->
        createBookVM?.bookingCancelOwner?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {

                    if (cancelBookingUser.equals("1")) {      // cancel by user
                        showDialogCancelSucceded()
                    } else {    // cancel by owner
                        showToast(this, user.message)
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }

                } else {
                    showToast(this, user.message)
                }

            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })


        // <----------- accept request api response --------/>
        notifyVM?.acceptRejectList?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    if (!isNetworkConnected()) {
                        showToast(this, "No Internet Connection")
                    } else {
                        matchID = user.data.match_id
                        mMatchType = user.data.match_detail.match_type.toString()
                        //show popup new
                        showDialogFromHomeaCC()
                        //   createBookVM?.singleMatchDetailData(this, user.data.match_id)
                    }
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))

            }
        })
    }

    private fun showConfirmDialog() {
        val alertdialog = AlertDialog.Builder(this)
        alertdialog.setTitle("Are you sure, you want to cancel booking?")
        alertdialog.setCancelable(false)

        alertdialog.setPositiveButton("Yes") { dialog, which ->
            if (!isNetworkConnected()) {
                showToast(this, "No internet Connection")
            } else {
                createBookVM?.bookingCancelByOwnerData(this, bookingID)
            }
        }
        alertdialog.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(alertdialog: DialogInterface?, which: Int) {
                alertdialog?.dismiss()
            }
        })
        alertdialog.show()
    }

    private fun setDataOnImageSec(
        circularImg: CircleImageView, title: TextView,
        tvScore: TextView, playerLisr: PlayerLisr?, imageWhiteOne: CircleImageView,
        lockImg: ImageView, plusTextOne: TextView, circularImgBorder: ImageView
    ) {
        playerLisr?.let {
            if (it.user_type.equals("1")) {                     // user role
                if (it.image_file != null) {
                    if (it.request_status.equals("0")) {
                        Glide.with(this)
                            .load(it.image_file)
                            .placeholder(R.drawable.ic_user) // any placeholder to load at start
                            .error(R.drawable.ic_user)// image url
                            //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                            //.error(R.drawable.profileimg_home)  // any image in case of error
                            .into(circularImg)
                        imageWhiteOne.visibility = View.VISIBLE
                        lockImg.visibility = View.GONE
                        plusTextOne.visibility = View.GONE
                    } else {
                        Glide.with(this)
                            .load(it.image_file)
                            .placeholder(R.drawable.ic_user) // any placeholder to load at start
                            .error(R.drawable.ic_user)// image url
                            //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                            //.error(R.drawable.profileimg_home)  // any image in case of error
                            .into(circularImg)
                        imageWhiteOne.visibility = View.GONE
                        lockImg.visibility = View.GONE
                        plusTextOne.visibility = View.GONE
                    }
                } else {
                    lockImg.visibility = View.VISIBLE
                    plusTextOne.visibility = View.VISIBLE
                    circularImg.setImageDrawable(null)
                }
            } else {                                                   // host role
                if (it.image_file != null) {

                    Glide.with(this)
                        .load(it.image_file)
                        .placeholder(R.drawable.ic_user) // any placeholder to load at start
                        .error(R.drawable.ic_user)// image url
                        //.placeholder(R.drawable.profileimg_home)  // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)        // any image in case of error
                        .into(circularImg)
                    imageWhiteOne.visibility = View.GONE
                    lockImg.visibility = View.GONE
                    plusTextOne.visibility = View.GONE
                    circularImgBorder.visibility = View.VISIBLE
                } else {
                    circularImg.setImageDrawable(null)
                    imageWhiteOne.visibility = View.VISIBLE
                    lockImg.visibility = View.VISIBLE
                    plusTextOne.visibility = View.VISIBLE
                    circularImgBorder.visibility = View.GONE

                }
            }

            if (it.name != null) {
                title.setText(it.name)
            }
            if (it.score != null) {
                tvScore.setText(it.score)
            }
        } ?: kotlin.run {

            Glide.with(this)
                .load(loginPref?.loginData?.image_file)
                .placeholder(R.drawable.ic_user) // any placeholder to load at start
                .error(R.drawable.ic_user)// image url
                //.placeholder(R.drawable.profileimg_home)  // any placeholder to load at start
                //.error(R.drawable.profileimg_home)        // any image in case of error
                .into(circularImg)
            imageWhiteOne.visibility = View.VISIBLE
            lockImg.visibility = View.GONE
            plusTextOne.visibility = View.GONE

            loginPref?.loginData?.name?.let {
                title.text = it
            }
            loginPref?.loginData?.score?.let {
                tvScore.text = it
            }


        }


    }

    fun setData() {
        // when call from court fragment
        if (intent.hasExtra("from")) {

            Log.e("MatchType", MatchType)
            if (MatchType.equals("StartAMatch")) {
                list.add(getString(R.string.pay_your_part))
                //   list.add(getString(R.string.pay_your_all_amounts))
                setAdapter("from", courtData?.court_feature, "0")
            } else {
                list.add(getString(R.string.pay_your_part))
                list.add(getString(R.string.pay_your_all_amounts))
                setAdapter("from", courtData?.court_feature, "1")
            }

            cancellationTitle.text =
                getString(R.string._24) + " " + courtData?.cancel_policy + " " + getString(R.string._24_)
            cancellationTitlee.text =
                getString(R.string._24) + " " + courtData?.cancel_policy + " " + getString(R.string._24_)

            tvPay.setText(getString(R.string.club))
            tvPay.background.setColorFilter(
                ContextCompat.getColor(this, R.color.btn_grey_clr),
                PorterDuff.Mode.SRC_ATOP
            )
            imgCard.visibility = View.GONE
            //  toolbar_join.visibility = View.VISIBLE
            toolbar_join.visibility = View.GONE
            // toolbar_join.ivBack.visibility = View.VISIBLE
            toolbar_join.ivBack.visibility = View.GONE

            loginPref?.loginData?.image_file?.let {
                if (loginPref?.loginData?.image_file!!.startsWith("http")) {
                    Glide.with(this)
                        .load(loginPref?.loginData?.image_file)             // image url
                        .placeholder(R.drawable.ic_user) // any placeholder to load at start
                        .error(R.drawable.ic_user)  // any image in case of error
                        .into(toolbar_join.profile_image)
                } else {
                    Glide.with(this)
                        .load(IMAGES_URL + loginPref?.loginData?.image_file)             // image url
                        .placeholder(R.drawable.ic_user) // any placeholder to load at start
                        .error(R.drawable.ic_user)  // any image in case of error
                        .into(toolbar_join.profile_image)
                }
            } ?: kotlin.run {
                Glide.with(this)
                    .load(R.drawable.ic_user)             // image url
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)  // any image in case of error
                    .into(toolbar_join.profile_image)
            }



            clHeader.visibility = View.VISIBLE
            clUsers.visibility = View.GONE
        } else if (intent.hasExtra("FromBookingList")) {     // call from booking list
            matchID = intent.extras?.get("matchId") as String?

            if (!isNetworkConnected()) {
                showInternetToast()
            } else {
                createBookVM?.singleMatchDetailData(this, matchID!!)

            }

            bookingScreenDataThree()
        } else {
            // when call from home fragment
            //   matchID = intent.extras?.get("matchId") as String?
            matchID = intent.getStringExtra("matchId")
            playerKeyID = intent.getStringExtra("playerKey")
            //  playerKeyID = intent.extras?.get("playerKey") as String?
            var courtFeat = intent.extras?.get("courtFeatureType") as String?
            /*

                       if(SessionClass(this).loginData.id.equals())
                       {

                           if(playerKeyID.equals("2")){

                               if(courtFeat.equals("Double")){

                                   Glide.with(this)
                                       .load(SessionClass(this).loginData.image_file)             // image url
                                       .into(circularImgTwo)
                                   titleTwo.setText(SessionClass(this).loginData.name)
                                   plusTextTwo.visibility = View.GONE
                                   imageWhiteTwo.visibility = View.VISIBLE
                               }
                               else{
                                   Glide.with(this)
                                       .load(SessionClass(this).loginData.image_file)             // image url
                                       .into(circularImgThree)
                                   titleThree.setText(SessionClass(this).loginData.name)

                                   plusTextThree.visibility = View.GONE
                                   imageWhiteThree.visibility = View.VISIBLE
                               }

                           }

                           if(playerKeyID.equals("3")){
                               Glide.with(this)
                                   .load(SessionClass(this).loginData.image_file)             // image url
                                   .into(circularImgThree)
                               titleThree.setText(SessionClass(this).loginData.name)
                               plusTextThree.visibility = View.GONE
                               imageWhiteThree.visibility = View.VISIBLE
                           }

                          if(playerKeyID.equals("4")) {
                              Glide.with(this)
                                  .load(SessionClass(this).loginData.image_file)             // image url
                                  .into(circularImgFour)
                              titleFour.setText(SessionClass(this).loginData.name)
                              plusTextFour.visibility = View.GONE
                              imageWhiteFour.visibility = View.VISIBLE

                          }
                          }
           */

            // <--------match data details api when call from home fragment---------->
            if (!isNetworkConnected()) {
                showToast(this, "No Internet Connection")
            } else {
                createBookVM?.singleMatchDetailData(this, matchID!!)
            }
            // <---------------------------------------------------------------------->

            joinMatchScreenData("")

            tvPay.setSafeOnClickListener {
                paymentMethod = "1" //pay at club
                if (payAmonuntAlready.equals("1")) {
                    if (intent.hasExtra("notification")) {
                        var bookingId = intent.extras?.get("bookingId") as String
                        if (!isNetworkConnected()) {
                            showToast(this, "No internet connection")
                        } else {
                            tt = "1"
                            // <-------- accept api ---------->
                            notifyVM?.acceptRejectResponse(
                                this,
                                bookingId,
                                matchID!!,
                                "1",
                                paymentType
                            )
                        }
                    } else {
                        if (!isNetworkConnected()) {
                            showToast(this, "No internet connection")
                        } else {
                            Log.e("paymentType", "" + paymentType)
                            Log.e("playerKeyID", "" + playerKeyID)
                            Log.e("matchID", "" + matchID)
                            createBookVM?.createMatchData(
                                this,
                                paymentType,
                                playerKeyID!!,
                                matchID!!
                            )
                        }
                    }
                } else {
                    // when call from notification screen who has accept request but doesn't pay their part
                    // if (paymentMethod == "2") {
                    if (intent.hasExtra("notification")) {
                        var bookingId = intent.extras?.get("bookingId") as String
                        if (!isNetworkConnected()) {
                            showToast(this, "No internet connection")
                        } else {
                            // <-------- accept api ---------->
                            notifyVM?.acceptRejectResponse(
                                this,
                                bookingId,
                                matchID!!,
                                "1",
                                paymentType
                            )
                        }
                    } else {
                        // when call from home screen
                        if (!isNetworkConnected()) {
                            showToast(this, "No internet connection")
                        } else {
                            createBookVM?.createMatchData(
                                this,
                                paymentType,
                                playerKeyID!!,
                                matchID!!
                            )
                        }
                    }
                    //  } else {
                    //  showToast(this, "Please select payment method")
                    // }
                }
            }

        }
    }

    private fun joinMatchScreenData(payType: String) {
        //new code
        if (mMatchType.equals("0")) {
            imgCard.visibility = View.GONE
        } else {
            imgCard.visibility = View.VISIBLE
        }

        clUsers.visibility = View.VISIBLE
        clHeader.visibility = View.VISIBLE
        toolbar_join.visibility = View.GONE

        /*   tvPay.background.setColorFilter(
            ContextCompat.getColor(this, R.color.btn_clr_blue),
            PorterDuff.Mode.SRC_ATOP
        )*/
        if (payType.equals("1")) { //single
            tvPay.isEnabled = false
            imgCard.isEnabled = false
            tvPay.background.setColorFilter(
                ContextCompat.getColor(this, R.color.btn_grey_clr),
                PorterDuff.Mode.SRC_ATOP
            )
            imgCard.background.setColorFilter(
                ContextCompat.getColor(this, R.color.btn_grey_clr),
                PorterDuff.Mode.SRC_ATOP
            )
            tvPay.setText(getString(R.string.club))
        } else {
            tvPay.isEnabled = true
            tvPay.background.setColorFilter(
                ContextCompat.getColor(this, R.color.btn_grey_clr),
                PorterDuff.Mode.SRC_ATOP
            )
            tvPay.setText(getString(R.string.club))
        }

        /* imgCard.setOnClickListener {

             startActivity(Intent(this,CardListingActivity::class.java))
         }*/

    }

    private fun setAdapter(type: String, courtFeature: String?, payType: String) {
        adapter = GamesAdapter(
            this, list, this, selectedAvailable_CateID, courtFeature,
            type, callFromHome_Data, payType
        )
        recycleGames.adapter = adapter
    }

    private fun setImageData(imageView: Int, btnColor: Int) {
        imgCard.setImageResource(imageView)
        imgCard.isEnabled = false
        val gradientDrawable = (imgCard.background as GradientDrawable).mutate()
        (gradientDrawable as GradientDrawable).setColor(ContextCompat.getColor(this, btnColor))
    }

    private fun bookingScreenData() {
        ttt = "2"
        setImageData(R.drawable.ic_group_320, R.color.red_color)
        clUsers.visibility = View.VISIBLE
        toolbar_join.visibility = View.GONE
        clHeader.visibility = View.VISIBLE
        tvTitleToolbar.setText(getString(R.string.booking_details))

/*if(tempData!!.user_id.equals(loginPref!!.loginData.id))
{
    if(tempData!!.match_type.equals("0"))
    {
       dataTxt.text=getString(R.string.text1)
    }
    else
    {
        dataTxt.text= getString(R.string.txt2)

    }
}
        else
{

    if(tempData!!.court_feature.toLowerCase().equals("single"))
    {
        dataTxt.text= getString(R.string.txt4)
    }
    else
    {
        dataTxt.text= getString(R.string.txt3)

    }

}*/

        description.visibility = View.VISIBLE
        recycleGames.visibility = View.GONE
        tvPay.setText(getString(R.string.add_scoree))


        /* imgCard.setOnClickListener {           // booking cancel by owner
             showConfirmDialog()
         }*/

        tvPay.setOnClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz")
            val call = Calendar.getInstance()
            System.out.println(dateFormat.format(call.time))

            Log.e("new 2 eeee", dateFormat.format(call.time))
            if (dateFormat.parse(mMatchDate)
                    .after(dateFormat.parse(dateFormat.format(call.time)))
            ) {
                Log.e("qqq", "11111")

            } else {
                Log.e("qqq", "22222")
                if (!isMatchScoreAdded) {
                    startActivity(
                        Intent(this, AddResultActivity::class.java)
                            .putExtra("match_id", matchID)
                    )
                } else {
                    tvPay.setText(getString(R.string.add_scoree))
                    tvPay.isEnabled = false
                    tvPay.background.setColorFilter(
                        ContextCompat.getColor(this, R.color.btn_grey_clr),
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
            }


        }
    }

    private fun bookingScreenDataThree() {
        ttt = "3"
        setImageData(R.drawable.ic_group_320, R.color.red_color)
        clUsers.visibility = View.VISIBLE
        toolbar_join.visibility = View.GONE
        clHeader.visibility = View.VISIBLE
        tvTitleToolbar.setText(getString(R.string.booking_details))

/*if(tempData!!.user_id.equals(loginPref!!.loginData.id))
{
    if(tempData!!.match_type.equals("0"))
    {
       dataTxt.text=getString(R.string.text1)
    }
    else
    {
        dataTxt.text= getString(R.string.txt2)

    }
}
        else
{

    if(tempData!!.court_feature.toLowerCase().equals("single"))
    {
        dataTxt.text= getString(R.string.txt4)
    }
    else
    {
        dataTxt.text= getString(R.string.txt3)

    }

}*/

        description.visibility = View.VISIBLE
        recycleGames.visibility = View.GONE
        tvPay.setText(getString(R.string.add_scoree))


        /* imgCard.setOnClickListener {           // booking cancel by owner
             showConfirmDialog()
         }*/

        tvPay.setOnClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz")
            val call = Calendar.getInstance()
            System.out.println(dateFormat.format(call.time))

            Log.e("new 2 eeee", dateFormat.format(call.time))
            if (dateFormat.parse(mMatchDate)
                    .after(dateFormat.parse(dateFormat.format(call.time)))
            ) {
                Log.e("qqq", "11111")

            } else {
                Log.e("qqq", "22222")
                if (!isMatchScoreAdded) {
                    startActivity(
                        Intent(this, AddResultActivity::class.java)
                            .putExtra("match_id", matchID)
                    )
                } else {
                    tvPay.setText(getString(R.string.add_scoree))
                    tvPay.isEnabled = false
                    tvPay.background.setColorFilter(
                        ContextCompat.getColor(this, R.color.btn_grey_clr),
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
            }


        }
    }

    override fun onPause() {
        super.onPause()
        toolbar_join.ivBack.visibility = View.GONE
    }

    private fun setClick() {

        ivBack.setSafeOnClickListener {
            if (ttt.equals("2")) {
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            } else if (ttt.equals("3")) {
                onBackPressed()
            } else {
                onBackPressed()
            }
        }
        toolbar_join.ivBack.setSafeOnClickListener {

            onBackPressed()

        }
        circularImgTwo.setSafeOnClickListener {
            if (!isWebsiteMatch) {
                if (intent.hasExtra("from") || intent.hasExtra("FromBookingList")
                    || callFraomHome_AsHost.equals("host")
                ) {
                    if (courtFeature.equals("Double")) {
                        if (circularImgTwo.drawable == null) {
                            if (matchInPast) {

                            } else {
                                startActivity(
                                    Intent(this, ContactActivity::class.java)
                                        .putExtra("AddPlayer", "AddPlayer")
                                        .putExtra("player_key", "2")
                                        .putExtra("match_id", matchID)
                                )
                            }

                            Log.d("PLAYERKEY", playerKey)
                            Log.d("MATCHID", matchID!!)
                        }
                    } else {
                        if (circularImgTwo.drawable == null) {
                            if (matchInPast) {

                            } else {
                                startActivity(
                                    Intent(this, ContactActivity::class.java)
                                        .putExtra("AddPlayer", "AddPlayer")
                                        .putExtra("player_key", "1")
                                        .putExtra("match_id", matchID)
                                )
                                Log.d("PLAYERKEY", playerKey)
                                Log.d("MATCHID", matchID!!)
                            }
                        }
                    }
                } else {
                    if (intent.hasExtra("notification")) {
                    } else {
                        if (isUserSelected == 0) {
                            titleTwo.setText("")
                            titleThree.setText("")
                            titleFour.setText("")
                            tvScoreTwo.setText("")
                            tvScoreThree.setText("")
                            tvScoreFour.setText("")

                            circularImgThree.setImageResource(0)
                            circularImgFour.setImageResource(0)
                            circularImgTwo.setImageResource(0)
                            plusTextFour.visibility = View.VISIBLE
                            plusTextThree.visibility = View.VISIBLE
                            playerKeyID = "2"
                            Glide.with(this)
                                .load(SessionClass(this).loginData.image_file)
                                .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                .error(R.drawable.ic_user)// image url
                                .into(circularImgTwo)
                            titleTwo.setText(SessionClass(this).loginData.name)
                            plusTextTwo.visibility = View.GONE
                            imageWhiteTwo.visibility = View.VISIBLE
                            //  createBookVM?.singleMatchDetailData(this, matchID!!)
                        }
                    }
                }
            }
        }
        circularImg.setSafeOnClickListener {
            if (intent.hasExtra("from") || intent.hasExtra("FromBookingList") ||
                callFraomHome_AsHost.equals("host")
            ) {
                if (circularImg.drawable == null) {
                    if (matchInPast) {

                    } else {
                        startActivity(
                            Intent(this, ContactActivity::class.java)
                                .putExtra("AddPlayer", "AddPlayer")
                                .putExtra("player_key", "3")
                                .putExtra("match_id", matchID)
                        )
                        Log.d("PLAYERKEY", playerKey)
                        Log.d("MATCHID", matchID!!)
                    }
                }

            } else {


            }
        }
        circularImgThree.setSafeOnClickListener {
            if (!isWebsiteMatch) {
                if (intent.hasExtra("from") || intent.hasExtra("FromBookingList") ||
                    callFraomHome_AsHost.equals("host")
                ) {
                    if (courtFeature.equals("Double")) {
                        if (circularImgThree.drawable == null) {
                            if (matchInPast) {

                            } else {
                                startActivity(
                                    Intent(this, ContactActivity::class.java)
                                        .putExtra("AddPlayer", "AddPlayer")
                                        .putExtra("player_key", "3")
                                        .putExtra("match_id", matchID)
                                )
                                Log.d("PLAYERKEY", playerKey)
                                Log.d("MATCHID", matchID!!)
                            }
                        }
                    } else {
                        if (circularImgThree.drawable == null) {
                            if (matchInPast) {

                            } else {
                                startActivity(
                                    Intent(this, ContactActivity::class.java)
                                        .putExtra("AddPlayer", "AddPlayer")
                                        .putExtra("player_key", "2")
                                        .putExtra("match_id", matchID)
                                )
                                Log.d("PLAYERKEY", playerKey)
                                Log.d("MATCHID", matchID!!)
                            }
                        }
                    }
                } else {
                    if (intent.hasExtra("notification")) {
                    } else {
                        if (isUserSelected == 0) {
                            titleTwo.setText("")
                            titleThree.setText("")
                            titleFour.setText("")
                            tvScoreTwo.setText("")
                            tvScoreThree.setText("")
                            tvScoreFour.setText("")

                            circularImgThree.setImageResource(0)
                            circularImgFour.setImageResource(0)
                            circularImgTwo.setImageResource(0)
                            plusTextFour.visibility = View.VISIBLE
                            plusTextTwo.visibility = View.VISIBLE
                            playerKeyID = "3"
                            //  createBookVM?.singleMatchDetailData(this, matchID!!)
                            Glide.with(this)
                                .load(SessionClass(this).loginData.image_file)
                                .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                .error(R.drawable.ic_user)// image url
                                .into(circularImgThree)
                            titleThree.setText(SessionClass(this).loginData.name)
                            plusTextThree.visibility = View.GONE
                            imageWhiteThree.visibility = View.VISIBLE

                        }
                    }
                }
            }
        }
        circularImgFour.setSafeOnClickListener {
            if (!isWebsiteMatch) {
                if (intent.hasExtra("from") || intent.hasExtra("FromBookingList") ||
                    callFraomHome_AsHost.equals("host")
                ) {

                    if (circularImgFour.drawable == null) {
                        if (matchInPast) {

                        } else {
                            startActivity(
                                Intent(this, ContactActivity::class.java)
                                    .putExtra("AddPlayer", "AddPlayer")
                                    .putExtra("player_key", "4")
                                    .putExtra("match_id", matchID)
                            )
                            Log.d("PLAYERKEY", playerKey)
                            Log.d("MATCHID", matchID!!)
                        }

                    }
                } else {
                    if (intent.hasExtra("notification")) {
                    } else {
                        if (isUserSelected == 0) {
                            titleTwo.setText("")
                            titleThree.setText("")
                            titleFour.setText("")
                            tvScoreTwo.setText("")
                            tvScoreThree.setText("")
                            tvScoreFour.setText("")

                            circularImgThree.setImageResource(0)
                            circularImgFour.setImageResource(0)
                            circularImgTwo.setImageResource(0)
                            plusTextTwo.visibility = View.VISIBLE
                            plusTextThree.visibility = View.VISIBLE
                            playerKeyID = "4"
                            Glide.with(this)
                                .load(SessionClass(this).loginData.image_file)
                                .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                .error(R.drawable.ic_user)// image url
                                .into(circularImgFour)
                            titleFour.setText(SessionClass(this).loginData.name)
                            plusTextFour.visibility = View.GONE
                            imageWhiteFour.visibility = View.VISIBLE
                            //  createBookVM?.singleMatchDetailData(this, matchID!!)
                        }

                    }
                }
            }

        }

        cancelText.setSafeOnClickListener {
            if (isSelectedCancel) {
                cancelPolicyText.visibility = View.VISIBLE
                isSelectedCancel = false
            } else {
                cancelPolicyText.visibility = View.GONE
                isSelectedCancel = true
            }
        }
        cancelTextt.setSafeOnClickListener {
            if (isSelectedCancel) {
                cancelPolicyTextt.visibility = View.VISIBLE
                isSelectedCancel = false
            } else {
                cancelPolicyTextt.visibility = View.GONE
                isSelectedCancel = true
            }
        }
    }

    private fun showDialogFromHome() {
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.popup_game_summary)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );

        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.dismiss()

        dialogData.popupSubmit.setOnClickListener {
            fromHomeDialog = "1"
            dialogData.dismiss()

            // <--------match data details api when call from home fragment---------->
            if (!isNetworkConnected()) {
                showToast(this@GamesActivity, "No Internet Connection")
            } else {
                createBookVM?.singleMatchDetailData(this, matchID ?: "0")
            }
            // <---------------------------------------------------------------------->

        }

        dialogData.tvDes.setText(getString(R.string.game_description))
        dialogData.tvTile.setText(getString(R.string.game_heading))
        dialogData.show()
    }

    private fun showDialogFromHomeaCC() {
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.popup_game_summary_accept)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );

        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.dismiss()

        dialogData.popupSubmit.setOnClickListener {
            //   fromHomeDialog = "1"
            dialogData.dismiss()

            // <--------match data details api when call from home fragment---------->
            if (!isNetworkConnected()) {
                showToast(this@GamesActivity, "No Internet Connection")
            } else {
                createBookVM?.singleMatchDetailData(this, matchID ?: "0")
                // createBookVM?.singleMatchDetailData(this, user.data.match_id)
            }
            // <---------------------------------------------------------------------->

        }

        // dialogData.tvDes.setText(getString(R.string.game_description))
        // dialogData.tvTile.setText(getString(R.string.game_heading))
        dialogData.show()
    }

    private fun showDialogCancal(data: Data) {
        // dialog to show pending request

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.popup_cancel_booking, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        var mAlertDialogg = mBuilder.show()
        val lp = WindowManager.LayoutParams()
        mAlertDialogg.setCancelable(false)
        lp.copyFrom(mAlertDialogg?.getWindow()!!.getAttributes())
        lp.gravity = Gravity.TOP
        lp.width = RelativeLayout.LayoutParams.MATCH_PARENT
        mAlertDialogg.getWindow()!!.setAttributes(lp)
        mAlertDialogg.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        mAlertDialogg.getWindow()!!.setGravity(Gravity.TOP);

        if (data.time != null && data.date != null) {
            mAlertDialogg.tvTitlepopup.setText(data.date + " , " + data.time)
        }
        if (data.clubName != null) {
            mAlertDialogg.tvTitleTwoPopup.setText(data.clubName)
        }
        if (data.courtname != null && data.club_status != null) {
            mAlertDialogg.tvCourtNamepopup.setText(data.courtname + " | " + data.court_type)
        }
        if (data.booking_time != null) {
            mAlertDialogg.tvTimePopup.setText(data.booking_time + " min")
        }

        mAlertDialogg.tvCancel.setSafeOnClickListener {
            mAlertDialogg.dismiss()
            showDialogCancelTwo(data)
        }
        mAlertDialogg.ivBack.setSafeOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }

        mAlertDialogg.show()
    }

    private fun showDialogCancelTwo(data: Data) {
        //
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.popup_cancel_request)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.dismiss()

        dialogData.tvNo.setSafeOnClickListener {
            cancelBookingUser = "1"
            dialogData.dismiss()
            if (!isNetworkConnected()) {
                showToast(this, "No internet connection")
            } else {
                createBookVM?.bookingCancelByUserData(this, data.bookiing_id)
            }
        }
        dialogData.tvCancel.setSafeOnClickListener {
            dialogData.dismiss()
            showDialogCancal(data)
        }
        dialogData.show()
    }

    private fun showDialogCancelTwoNew2(data: Data) {
        //
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.popup_cancel_request)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.dismiss()
        if (data.user_id.equals(loginPref!!.loginData.id)) {

            if (data!!.match_type.equals("0"))// public
            {
                dialogData.tvTile.text = getString(R.string.zzz)
            } else {
                dialogData.tvTile.text = getString(R.string.vvv)
            }
        } else {

            dialogData.tvTile.text = getString(R.string.sss)


        }
        dialogData.tvNo.setSafeOnClickListener {
            cancelBookingUser = "1"
            dialogData.dismiss()
            if (!isNetworkConnected()) {
                showToast(this, "No internet connection")
            } else {
                createBookVM?.bookingCancelByOwnerData(this, data.bookiing_id)
            }
        }
        dialogData.tvCancel.setSafeOnClickListener {
            dialogData.dismiss()
            if (data.user_id.equals(loginPref!!.loginData.id)) {
            } else {
                showDialogCancal(data)
            }
        }
        dialogData.show()
    }

    private fun showDialogCancelTwoNew(data: Data) {
        //
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.popup_cancel_request)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.dismiss()
        dialogData.tvTile.text = getString(R.string.cancel_txtt)
        dialogData.tvNo.setSafeOnClickListener {
            cancelBookingUser = "1"
            dialogData.dismiss()
            if (!isNetworkConnected()) {
                showToast(this, "No internet connection")
            } else {
                createBookVM?.bookingCancelByUserData(this, data.bookiing_id)
            }
        }
        dialogData.tvCancel.setSafeOnClickListener {
            dialogData.dismiss()
            // showDialogCancal(data)
        }
        dialogData.show()
    }

    private fun showDialogCancelTwoTemp() {
        //
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.popup_cancel_request)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.dismiss()

        dialogData.tvNo.setSafeOnClickListener {
            cancelBookingUser = "1"
            dialogData.dismiss()
            if (!isNetworkConnected()) {
                showToast(this, "No internet connection")
            } else {
                createBookVM?.bookingCancelByOwnerData(this, bookingID)
            }
        }
        dialogData.tvCancel.setSafeOnClickListener {
            dialogData.dismiss()
            // showDialogCancal(data)
        }
        dialogData.show()
    }

    private fun showDialogCancelSucceded() {
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.cancel_succeded_popup)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.dismiss()
        if (tempData!! != null && tempData!!.user_id.equals(loginPref!!.loginData.id)) {

            if (!MatchType.equals("StartAMatch"))// public
            {
                dialogData.tvTile.text = getString(R.string.c1)
            } else {
                dialogData.tvTile.text = getString(R.string.c2)
            }
        } else {

            dialogData.tvTile.text = getString(R.string.c3)


        }
        dialogData.cancelSucceded.setSafeOnClickListener {
            dialogData.dismiss()
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
        dialogData.show()
    }


    private fun showDialog() {
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.popup_game_summary)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData?.dismiss()

        if (intent.hasExtra("from")) {
            if (!MatchType.equals("StartAMatch")) {

                dialogData?.tvTile.setText(getString(R.string.ttt))

                dialogData?.tvDes.setText(getString(R.string.ddd))
            } else {
                dialogData?.tvDes.setText(getString(R.string.public_booking_des))
                dialogData?.tvTile.setText(getString(R.string.public_booking_title))
            }
        } else {
            dialogData?.tvDes.setText(getString(R.string.game_description))
            dialogData?.tvTile.setText(getString(R.string.game_heading))
        }

        dialogData?.popupSubmit.setSafeOnClickListener {
            dialogData?.dismiss()


            if (intent.hasExtra("from")) {      // when call from court Fragment

                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    createBookVM?.singleMatchDetailData(this@GamesActivity, matchID ?: "0")

                }
                bookingScreenData()

            }

            description.visibility = View.VISIBLE
            recycleGames.visibility = View.GONE
            tvTitleToolbar.setText(getString(R.string.booking_details))
            tvPay.setText(getString(R.string.add_scoree))
            tvPay.setOnClickListener {

                val dateFormat: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz")
                val call = Calendar.getInstance()
                System.out.println(dateFormat.format(call.time))

                Log.e("new 2 eeee", dateFormat.format(call.time))
                if (dateFormat.parse(mMatchDate)
                        .after(dateFormat.parse(dateFormat.format(call.time)))
                ) {
                    Log.e("wwwwww", "11111")
                } else {
                    Log.e("wwwwww", "22222")
                    if (!isMatchScoreAdded) {
                        startActivity(
                            Intent(this, AddResultActivity::class.java)
                                .putExtra("match_id", matchID)
                        )
                    } else {
                        tvPay.setText(getString(R.string.add_scoree))
                        tvPay.isEnabled = false
                        tvPay.background.setColorFilter(
                            ContextCompat.getColor(this, R.color.btn_grey_clr),
                            PorterDuff.Mode.SRC_ATOP
                        )
                    }
                }
            }
        }
        dialogData.show()
    }

    private fun setToolbar() {
        ivBack.setSafeOnClickListener {
            onBackPressed()
        }

        tvTitleToolbar.visibility = View.VISIBLE
        separatorToolbar.visibility = View.VISIBLE
        tvTitleToolbar.setText(getString(R.string.game_title))
    }

    override fun onClick(position: String, paidprice: String) {
        /* if (position.equals(getString(R.string.pay_your_part))) {
             paymentMethod = "1" //single
         } else {
             paymentMethod = "2" //split
         }*/

        paid_price = paidprice

        if (MatchType.equals("StartAMatch")) {
            imgCard.visibility = View.GONE
        } else {
            imgCard.visibility = View.VISIBLE
        }

        if (position.equals(getString(R.string.pay_your_part))) {
            paymentType = "2" //split

            if (selectedAvailable_CateID?.is_club_pass == 1) {
                tvPay.text = getString(R.string.confirm)
                imgCard.visibility = View.GONE
            } else {
                tvPay.text = getString(R.string.club)

                if (MatchType.equals("StartAMatch")) {
                    imgCard.visibility = View.GONE
                } else {
                    imgCard.visibility = View.VISIBLE
                }
            }

            if (mMatchType.equals("0")) {
                imgCard.visibility = View.GONE
            } else {
                if (MatchType.equals("StartAMatch")) {
                    imgCard.visibility = View.GONE

                } else {
                    imgCard.visibility = View.VISIBLE
                }
            }

        } else {
            paymentType = "1" //single
        }
        tvPay.isEnabled = true
        imgCard.isEnabled = true
        if (intent.hasExtra("from")) {

            // when call from court screen

            tvPay.background.setColorFilter(
                ContextCompat.getColor(this, R.color.btn_clr_blue),
                PorterDuff.Mode.SRC_ATOP
            )



            tvPay.setSafeOnClickListener {
                paymentMethod = "1"

                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    if (MatchType.equals("StartAMatch")) {
                        // if call from start a match screen, public booking is created

                        createBookVM?.createBookingData(
                            this,
                            courtData!!.club_id,
                            courtData!!.court_id.toString(),
                            dateFormatWithHyphanConversion(selectedDate.toString()),
                            selectedTime?.start_time.toString(),
                            selectedAvailable_CateID?.category_id.toString(),
                            paymentType,
                            selectedTime?.schedule_id.toString(),
                            paymentMethod,
                            "0",
                            selectedAvailable_CateID?.time.toString(),
                            selectedAvailable_CateID?.price.toString(), paid_price.toString(),
                            "", "0", isClubPass.toString(), club_pass_id.toString()
                        )
                    } else {
                        Log.e("selectedTime", Gson().toJson(selectedTime))
                        // if call from home, favourite, search screen, private booking is created
                        createBookVM?.createBookingData(
                            this,
                            courtData!!.club_id,
                            courtData!!.court_id.toString(),
                            dateFormatWithHyphanConversion(selectedDate.toString()),
                            selectedTime?.start_time.toString(),
                            selectedAvailable_CateID?.category_id.toString(),
                            paymentType,
                            selectedTime?.schedule_id.toString(),
                            paymentMethod,
                            "1",
                            selectedAvailable_CateID?.time.toString(),
                            selectedAvailable_CateID?.price.toString(),
                            paid_price.toString(),
                            "",
                            "0",
                            isClubPass.toString(),
                            selectedAvailable_CateID?.club_pass?.id.toString()
                        )
                    }
                }

                //    showDialog()
            }

            //  toolbar_join.visibility= View.VISIBLE
            clHeader.visibility = View.VISIBLE
//            clUsers.visibility= View.GONE
        }
        tvPay.background.setColorFilter(
            ContextCompat.getColor(this, R.color.btn_clr_blue),
            PorterDuff.Mode.SRC_ATOP
        )

        if (ttt.equals("2")) {
            imgCard.background.setColorFilter(
                ContextCompat.getColor(this, R.color.red_color),
                PorterDuff.Mode.SRC_ATOP
            )

            // showDialogCancal(tempData!!)
        } else {
            imgCard.background.setColorFilter(
                ContextCompat.getColor(this, R.color.btn_clr_blue),
                PorterDuff.Mode.SRC_ATOP
            )
        }

    }

    companion object {
        var fromHomeDialog = ""
        var cancelBookingUser = ""
        var callFraomHome_AsHost = ""
        var courtFeature = ""
        var mMatchType = ""
    }

}