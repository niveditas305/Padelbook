package snow.app.padelbook.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_add_result.*
import kotlinx.android.synthetic.main.activity_add_result.scoreOne
import kotlinx.android.synthetic.main.activity_add_result.scoreTwo
import kotlinx.android.synthetic.main.popup_add_result_confirmation.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.toolbar.view.separator
import kotlinx.android.synthetic.main.toolbar.view.tvTitle
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.score.singleScoreDetailNew.Data
import snow.app.padelbook.viewModel.AddScoreVM
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AddResultActivity : BaseActivity() {
    private var mMatchDate: String = ""
    var dialogData: Dialog? = null
    private var addScoreVM: AddScoreVM? = null
    var matchId: String? = null
    var roundOne = ""
    var roundTwo = ""
    var roundThree = ""

//    var roundFour : ArrayList<String> = ArrayList()
//    var roundFive : ArrayList<String> = ArrayList()
//    var roundSix : ArrayList<String> = ArrayList()

    var setDataToPopup: List<Data> = ArrayList()
    var courtFeatureType = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_result)
        addScoreVM = AddScoreVM()


        matchId = intent.extras?.get("match_id") as String

        // <---------- single score detail api ---------->
        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            addScoreVM?.singleScoreDetailData(this, matchId!!)
        }
        // <---------------------------------------------->

        setToolbar()
        setClick()
        setEditTextClickable()
        listener()
    }

    private fun listener() {
        addScoreVM?.addScore?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    dialogData?.dismiss()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    dialogData?.dismiss()
                    showToast(this, user.message)
                }
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })

        // <-----------score detail response api ------------->
        addScoreVM?.singleScoreDetail?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    if (user.data != null) {
                        setDataToPopup = user.data
                    }
                    if (user.court_feature != null) {
                        courtFeatureType = user.court_feature
                    }


                    if (user.time != null && user.date != null) {


                        val myFormat =
                            SimpleDateFormat("EEEE dd MMM , HH:mm").parse(user.date + " , " + user.time)
                        var myDate = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz").format(myFormat)
                        Log.e("eeee", myDate)
                        val d: Date = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz").parse(myDate)
                        val cal: Calendar = Calendar.getInstance()
                        cal.setTime(d)
                     //   cal.add(Calendar.MINUTE, user.booking_time.toInt())
                        val newTime: String =
                            SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz").format(cal.getTime())
                        Log.e("new  eeee", newTime)

                        mMatchDate = newTime
                        val dateFormat: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz")
                        val call = Calendar.getInstance()
                        System.out.println(dateFormat.format(call.time))

                        Log.e("new 2 eeee", dateFormat.format(call.time))
                        if (dateFormat.parse(mMatchDate)
                                .after(dateFormat.parse(dateFormat.format(call.time)))
                        ) {
                            greyBackNew(etFirst)
                            greyBackNew(etSecond)
                            greyBackNew(etThird)
                            greyBackNew(etForth)
                            greyBackNew(etFivth)
                            greyBackNew(etSixth)
                        } else {
                        }

                        tvTitleDate.setText(user.date + " , " + user.time)
                    }
                    if (user.clubName != null) {
                        tvTitleClub.setText(user.clubName)
                    }
                    if (user.courtname != null && user.club_status != null) {
                        tvCourt.setText(user.courtname + " | " + user.club_status)
                    }
                    if (user.booking_time != null) {
                        tvAddResultTime.setText(user.booking_time + " min")
                    }

                    if (user.court_feature == "Double") {
                        // if teams has four players
                        for (i in user.data.indices) {
                            if (user.data[i].player_key == "1") {
                                setDataOnImageSec(
                                    circularImgOne, titleOne,
                                    scoreOne, user.data[i], plusIconOne, circularImgOneBorder

                                )
                            } else if (user.data[i].player_key == "2") {
                                setDataOnImageSec(
                                    circularImgTwoAdd, titleTwoAdd,
                                    scoreTwo, user.data[i], plusIconTwo, circularImgOneBorderTwo
                                )
                            } else if (user.data[i].player_key == "3") {
                                setDataOnImageSec(
                                    circularImgThreeAdd,
                                    titleThreeAdd,
                                    scoreThree,
                                    user.data[i],
                                    plusIconThree,
                                    circularImgOneBorderThree
                                )
                            } else {
                                setDataOnImageSec(
                                    circularImgFourAdd, titleFourAdd,
                                    scoreFour, user.data[i], plusIconFour, circularImgOneBorderFour
                                )
                            }
                        }
                    } else {
                        // if team has two players
                        imgFirstSecAdd.visibility = View.INVISIBLE
                        imgForthSecAdd.visibility = View.INVISIBLE


                        for (i in user.data.indices) {
                            if (user.data[i].player_key == "1") {
                                setDataOnImageSec(
                                    circularImgTwoAdd, titleTwoAdd,
                                    scoreTwo, user.data[i], plusIconTwo, circularImgOneBorderTwo
                                )
                            } else if (user.data[i].player_key == "2") {
                                setDataOnImageSec(
                                    circularImgThreeAdd,
                                    titleThreeAdd,
                                    scoreThree,
                                    user.data[i],
                                    plusIconThree,
                                    circularImgOneBorderThree
                                )
                            }
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


    private fun setDataOnImageSecForPopup(
        circularImg: CircleImageView, titleOne: TextView,
        scoreOne: TextView, data: Data, borderImageView: ImageView
    ) {
        if (data.user_type == "1") {                         // user role
            if (data.image_file != null) {
                if (data.request_status == "0") {
                    Glide.with(this)
                        .load(data.image_file)             // image url
                        //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)  // any image in case of error
                        .into(circularImg)
                } else {
                    Glide.with(this)
                        .load(data.image_file)             // image url
                        //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)  // any image in case of error
                        .into(circularImg)
                }
            } else {
                circularImg.setImageDrawable(null)
            }
            scoreOne.setText(data.score)
            titleOne.setText(data.name)
        } else {
            if (data.image_file != null) {                        // host role
                if (data.request_status == "0") {
                    Glide.with(this)
                        .load(data.image_file)             // image url
                        //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)  // any image in case of error
                        .into(circularImg)
                    borderImageView.visibility = View.VISIBLE
                } else {
                    Glide.with(this)
                        .load(data.image_file)             // image url
                        //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)  // any image in case of error
                        .into(circularImg)
                    borderImageView.visibility = View.VISIBLE
                }
            } else {
                borderImageView.visibility = View.GONE
                circularImg.setImageDrawable(null)
            }
        }
        scoreOne.setText(data.score)
        titleOne.setText(data.name)
    }


    private fun setDataOnImageSec(
        circularImg: CircleImageView, titleOne: TextView,
        scoreOne: TextView, data: Data, plusIcon: TextView, circularImgOneBorderTwo: ImageView
    ) {
        if (data.user_type == "1") {                  // user role
            if (data.image_file != null) {
                if (data.request_status == "0") {
                    Glide.with(this)
                        .load(data.image_file)             // image url
                        //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)  // any image in case of error
                        .into(circularImg)
                    plusIcon.visibility = View.GONE
                } else {
                    Glide.with(this)
                        .load(data.image_file)             // image url
                        //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)  // any image in case of error
                        .into(circularImg)
                    plusIcon.visibility = View.GONE
                }
            } else {
                circularImg.setImageDrawable(null)
                plusIcon.visibility = View.VISIBLE
            }
            scoreOne.setText(data.score)
            titleOne.setText(data.name)
        } else {                                                       // host role
            if (data.image_file != null) {
                if (data.request_status == "0") {
                    Glide.with(this)
                        .load(data.image_file)             // image url
                        //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)  // any image in case of error
                        .into(circularImg)
                    plusIcon.visibility = View.GONE
                    circularImgOneBorderTwo.visibility = View.VISIBLE
                } else {
                    Glide.with(this)
                        .load(data.image_file)             // image url
                        //.placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //.error(R.drawable.profileimg_home)  // any image in case of error
                        .into(circularImg)
                    plusIcon.visibility = View.GONE
                    circularImgOneBorderTwo.visibility = View.VISIBLE

                }
            } else {
                circularImgOneBorderTwo.visibility = View.GONE
                circularImg.setImageDrawable(null)
                plusIcon.visibility = View.VISIBLE
            }
        }
        scoreOne.setText(data.score)
        titleOne.setText(data.name)
    }

    override fun onResume() {
        super.onResume()
        if (etFirst.text.toString().isEmpty()) {
            blueBack(etFirst)
            greyBack(etSecond)
            greyBack(etThird)
            greyBack(etForth)
            greyBack(etFivth)
            greyBack(etSixth)
        } else if (etForth.text.toString().isEmpty()) {
            greyBack(etFirst)
            greyBack(etSecond)
            greyBack(etThird)
            blueBack(etForth)
            greyBack(etFivth)
            greyBack(etSixth)
        } else if (etSecond.text.toString().isEmpty()) {
            greyBack(etFirst)
            blueBack(etSecond)
            greyBack(etThird)
            greyBack(etForth)
            greyBack(etFivth)
            greyBack(etSixth)
        } else if (etFivth.text.toString().isEmpty()) {
            greyBack(etFirst)
            greyBack(etSecond)
            greyBack(etThird)
            greyBack(etForth)
            blueBack(etFivth)
            greyBack(etSixth)
        } else if (etThird.text.toString().isEmpty()) {
            greyBack(etFirst)
            greyBack(etSecond)
            blueBack(etThird)
            greyBack(etForth)
            greyBack(etFivth)
            greyBack(etSixth)
        } else if (etSixth.text.toString().isEmpty()) {
            greyBack(etFirst)
            greyBack(etSecond)
            greyBack(etThird)
            greyBack(etForth)
            greyBack(etFivth)
            blueBack(etSixth)
        } else {
            greyBack(etFirst)
            greyBack(etSecond)
            greyBack(etThird)
            greyBack(etForth)
            greyBack(etFivth)
            greyBack(etSixth)
        }


    }

    private fun setEditTextClickable() {

        etFirst.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    blueBack(etFirst)
                    greyBack(etSecond)
                    greyBack(etThird)
                    blueBack(etForth)
                    greyBack(etFivth)
                    greyBack(etSixth)
                } else {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    greyBack(etThird)
                    blueBack(etForth)
                    greyBack(etFivth)
                    greyBack(etSixth)

                }
            }

        })
        etForth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    greyBack(etThird)
                    blueBack(etForth)
                    greyBack(etFivth)
                    greyBack(etSixth)
                } else {
                    greyBack(etFirst)
                    blueBack(etSecond)
                    greyBack(etThird)
                    greyBack(etForth)
                    greyBack(etFivth)
                    greyBack(etSixth)
                }

                if(!etForth.text.toString().equals("")&&!etFirst.text.toString().equals(""))
                {
                if (etForth.text.toString().toInt() > etFirst.text.toString().toInt()) {
                    blueText(etForth)
                    greyText(etFirst)
                } else if (etForth.text.toString().toInt() == etFirst.text.toString().toInt()) {
                    greyText(etForth)
                    greyText(etFirst)
                } else {
                    blueText(etFirst)
                    greyText(etForth)
                }}
            }
        })

        etSecond.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    greyBack(etFirst)
                    blueBack(etSecond)
                    greyBack(etThird)
                    greyBack(etForth)
                    greyBack(etFivth)
                    greyBack(etSixth)
                } else {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    greyBack(etThird)
                    greyBack(etForth)
                    blueBack(etFivth)
                    greyBack(etSixth)
                }
            }
        })

        etFivth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    greyBack(etThird)
                    greyBack(etForth)
                    blueBack(etFivth)
                    greyBack(etSixth)

                } else {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    blueBack(etThird)
                    greyBack(etForth)
                    greyBack(etFivth)
                    greyBack(etSixth)
                }
if(etFivth.text.toString().equals(""))
{}
                else{
                    if(etFivth.text.toString().toInt() > etSecond.text.toString().toInt()){
                        blueText(etFivth)
                        greyText(etSecond)
                    }
                    else if(etFivth.text.toString().toInt() == etSecond.text.toString().toInt()){
                        greyText(etFivth)
                        greyText(etSecond)
                    }
                    else{
                        blueText(etSecond)
                        greyText(etFivth)
                    }
            }
            }
        })

        etThird.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    blueBack(etThird)
                    greyBack(etForth)
                    greyBack(etFivth)
                    greyBack(etSixth)
                } else {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    greyBack(etThird)
                    greyBack(etForth)
                    greyBack(etFivth)
                    blueBack(etSixth)
                }
            }
        })

        etSixth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    greyBack(etThird)
                    greyBack(etForth)
                    greyBack(etFivth)
                    blueBack(etSixth)
                } else {
                    greyBack(etFirst)
                    greyBack(etSecond)
                    greyBack(etThird)
                    greyBack(etForth)
                    greyBack(etFivth)
                    greyBack(etSixth)
                    hideSoftKeyboard(etSixth, this@AddResultActivity)
                }

                if(etSixth.text.toString().equals(""))
                {

                }
                else{

                if(etSixth.text.toString().toInt() > etThird.text.toString().toInt()){
                    blueText(etSixth)
                    greyText(etThird)
                }
                else if(etSixth.text.toString().toInt() == etThird.text.toString().toInt()){
                    greyText(etSixth)
                    greyText(etThird)
                }
                else
                {
                    blueText(etThird)
                    greyText(etSixth)
                }}
            }
        })

    }

    private fun blueText(etText: EditText) {
        etText.setTextColor(ContextCompat.getColor(this, R.color.theme_blue))

    }

    private fun greyText(etText: EditText) {
        etText.setTextColor(ContextCompat.getColor(this, R.color.contact_clr))

    }

    private fun setClick() {
        tvScore.setOnClickListener {
            val dateFormat: DateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz")
            val call = Calendar.getInstance()
            System.out.println(dateFormat.format(call.time))

            Log.e("new 2 eeee", dateFormat.format(call.time))
            if (dateFormat.parse(mMatchDate)
                    .after(dateFormat.parse(dateFormat.format(call.time)))
            ) {
                showToast(this, "Match not completed yet")
            } else {

                if (etFirst.text.toString().isEmpty()) {
                    showToast(this, "Please enter round one score")
                } else if (etForth.text.toString().isEmpty()) {
                    showToast(this, "Please enter round one score")
                } else if (etSecond.text.toString().isEmpty()) {
                    showToast(this, "Please enter round two score")
                } else if (etFivth.text.toString().isEmpty()) {
                    showToast(this, "Please enter round two score")
                }
                /*  else if(etThird.text.toString().isEmpty()){
                            showToast(this,"Please enter round three score")
                        }
                        else if(etSixth.text.toString().isEmpty()){
                            showToast(this,"Please enter round three score")
                        }*/
                else {

                    roundOne = etFirst.text.toString() + "," + etForth.text.toString()
                    //   roundFour.add(etForth.text.toString())

                    roundTwo = etSecond.text.toString() + "," + etFivth.text.toString()
                    //    roundFive.add(etFivth.text.toString())

                    roundThree = etThird.text.toString() + "," + etSixth.text.toString()
                    //   roundSix.add(etSixth.text.toString())

                    var teamFirstRoundF = etFirst.text.toString()
                    var teamFirstRoundS = etForth.text.toString()

                    var teamSecondRoundF = etSecond.text.toString()
                    var teamSecondRoundS = etFivth.text.toString()

                    var teamThirdRoundF = etThird.text.toString()
                    var teamRoundThirdS = etSixth.text.toString()

                    if (teamFirstRoundF.toInt() > teamFirstRoundS.toInt() && teamSecondRoundF.toInt() > teamSecondRoundS.toInt()) {
                        var t = "0"
                        if (teamThirdRoundF.equals("")) {
                            t = "0"
                        } else {
                            t = teamThirdRoundF
                        }
                        var t2 = "0"
                        if (teamThirdRoundF.equals("")) {
                            t2 = "0"
                        } else {
                            t2 = teamRoundThirdS
                        }
                        showDialog(
                            teamFirstRoundF,
                            teamFirstRoundS,
                            teamSecondRoundF,
                            teamSecondRoundS,
                            t,
                            t2
                        )
                    } else
                        if (teamFirstRoundF.toInt() < teamFirstRoundS.toInt() && teamSecondRoundF.toInt() < teamSecondRoundS.toInt()) {
                            var t = "0"
                            if (teamThirdRoundF.equals("")) {
                                t = "0"
                            } else {
                                t = teamThirdRoundF
                            }
                            var t2 = "0"
                            if (teamThirdRoundF.equals("")) {
                                t2 = "0"
                            } else {
                                t2 = teamRoundThirdS
                            }
                            showDialog(
                                teamFirstRoundF,
                                teamFirstRoundS,
                                teamSecondRoundF,
                                teamSecondRoundS,
                                t,
                                t2
                            )
                            // showDialog(teamFirstRoundF,teamFirstRoundS,teamSecondRoundF,teamSecondRoundS,"0","0")
                        } else {

                            if (etThird.text.toString().isEmpty()) {
                                showToast(this, "Please enter round three score")
                            } else if (etSixth.text.toString().isEmpty()) {
                                showToast(this, "Please enter round three score")
                            } else {
                                showDialog(
                                    teamFirstRoundF,
                                    teamFirstRoundS,
                                    teamSecondRoundF,
                                    teamSecondRoundS,
                                    teamThirdRoundF,
                                    teamRoundThirdS
                                )
                            }
                        }
                }

            }
        }
    }

    private fun blueBack(et: EditText) {
        et.isEnabled = true
        et.setFocusable(true)
        et.setFocusableInTouchMode(true);
        showSoftKeyboard(et, this)              // show keyboard
        val drawable = et.background as GradientDrawable
        drawable.setStroke(2, ContextCompat.getColor(this, R.color.theme_blue))

    }

    private fun greyBack(et: EditText) {
        // et.isEnabled = false
        //  et.setFocusable(false)


        val drawable = et.background as GradientDrawable
        drawable.setStroke(2, ContextCompat.getColor(this, R.color.light_grey))
    }

    private fun greyBackNew(et: EditText) {
        et.isEnabled = false
        et.setFocusable(false)


        val drawable = et.background as GradientDrawable
        drawable.setStroke(2, ContextCompat.getColor(this, R.color.light_grey))
    }

    private fun setToolbar() {
        toolbarId.ivBack.setOnClickListener {
            onBackPressed()
        }
        toolbarId.tvTitle.visibility = View.VISIBLE
        toolbarId.tvTitle.setText(getString(R.string.add_score))
        toolbarId.separator.visibility = View.VISIBLE
    }

    private fun showDialog(
        TeamFirst_RF: String, TeamFirst_RS: String, TeamSecond_RF: String, TeamSecond_RS: String,
        TeamThird_RF: String, TeamThird_RS: String
    ) {
        dialogData = Dialog(this)
        dialogData?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData?.setCancelable(true)
        dialogData?.setContentView(R.layout.popup_add_result_confirmation)
        dialogData?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData?.window?.setGravity(Gravity.CENTER)
        dialogData?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
var countt=0
var countt2=0
        dialogData?.teamOneFirst?.setText(TeamFirst_RF)
        dialogData?.teamOneSecond?.setText(TeamFirst_RS)
        if (TeamFirst_RF.toInt() > TeamFirst_RS.toInt()) {
            countt=countt+1
            showBlueText(dialogData?.teamOneFirst)
            showGreyText(dialogData?.teamOneSecond)
        } else if (TeamFirst_RF.toInt() == TeamFirst_RS.toInt()) {

            showGreyText(dialogData?.teamOneFirst)
            showGreyText(dialogData?.teamOneSecond)
        } else {
            countt2=countt2+1
            showBlueText(dialogData?.teamOneSecond)
            showGreyText(dialogData?.teamOneFirst)
        }


        dialogData?.teamTwoFirst?.setText(TeamSecond_RF)
        dialogData?.teamTwoSecond?.setText(TeamSecond_RS)
        if (TeamSecond_RF.toInt() > TeamSecond_RS.toInt()) {
            countt=countt+1
            showBlueText(dialogData?.teamTwoFirst)
            showGreyText(dialogData?.teamTwoSecond)
        } else if (TeamSecond_RF.toInt() == TeamSecond_RS.toInt()) {

            showGreyText(dialogData?.teamTwoFirst)
            showGreyText(dialogData?.teamTwoSecond)
        } else {
            countt2=countt2+1
            showBlueText(dialogData?.teamTwoSecond)
            showGreyText(dialogData?.teamTwoFirst)
        }


        dialogData?.teamThreeFirst?.setText(TeamThird_RF)
        dialogData?.teamThreeSecond?.setText(TeamThird_RS)
        if (TeamThird_RF.toInt() > TeamThird_RS.toInt()) {
            countt=countt+1
            showBlueText(dialogData?.teamThreeFirst)
            showGreyText(dialogData?.teamThreeSecond)
        } else if (TeamThird_RF.toInt() == TeamThird_RS.toInt()) {

            showGreyText(dialogData?.teamThreeFirst)
            showGreyText(dialogData?.teamThreeSecond)
        } else {
            countt2=countt2+1
            showBlueText(dialogData?.teamThreeSecond)
            showGreyText(dialogData?.teamThreeFirst)
        }


        var teamOneTotal = TeamFirst_RF.toInt() + TeamSecond_RF.toInt() + TeamThird_RF.toInt()
        var teamTwoTotal = TeamFirst_RS.toInt() + TeamSecond_RS.toInt() + TeamThird_RS.toInt()

        Log.d("teamOne", teamOneTotal.toString())
        Log.d("teamOne", teamTwoTotal.toString())

        if (  countt  >=2) {
            dialogData?.teamF_winTag?.visibility = View.VISIBLE
            dialogData?.teamS_winTag?.visibility = View.INVISIBLE
        }  else if (countt2 >=2) {
            dialogData?.teamF_winTag?.visibility = View.INVISIBLE
            dialogData?.teamS_winTag?.visibility = View.VISIBLE
        } else {
            dialogData?.teamF_winTag?.visibility = View.INVISIBLE
            dialogData?.teamS_winTag?.visibility = View.INVISIBLE
        }
/*

        if (teamOneTotal > teamTwoTotal) {
            dialogData?.teamF_winTag?.visibility = View.VISIBLE
            dialogData?.teamS_winTag?.visibility = View.INVISIBLE
        } else if (teamOneTotal < teamTwoTotal) {
            dialogData?.teamF_winTag?.visibility = View.INVISIBLE
            dialogData?.teamS_winTag?.visibility = View.VISIBLE
        } else {
            dialogData?.teamF_winTag?.visibility = View.INVISIBLE
            dialogData?.teamS_winTag?.visibility = View.INVISIBLE
        }
*/




        if (courtFeatureType.equals("Double")) {

            // if teams has four players
            for (i in setDataToPopup.indices) {
                if (setDataToPopup[i].player_key == "1") {
                    setDataOnImageSecForPopup(
                        dialogData?.circularImg!!,
                        dialogData?.titlePop!!,
                        dialogData?.scorePop!!,
                        setDataToPopup[i],
                        dialogData?.circularImgBorderOne!!
                    )
                } else if (setDataToPopup[i].player_key == "2") {
                    setDataOnImageSecForPopup(
                        dialogData?.circularImgTwo!!,
                        dialogData?.titleTwo!!,
                        dialogData?.scoreTwoPop!!,
                        setDataToPopup[i],
                        dialogData?.circularImgBorderTwo!!
                    )
                } else if (setDataToPopup[i].player_key == "3") {
                    setDataOnImageSecForPopup(
                        dialogData?.circularImgThreePop!!,
                        dialogData?.titleThreePop!!,
                        dialogData?.scoreThreePop!!,
                        setDataToPopup[i],
                        dialogData?.circularImgBorderThree!!
                    )
                } else {
                    setDataOnImageSecForPopup(
                        dialogData?.circularImgFour!!,
                        dialogData?.titleFourPop!!,
                        dialogData?.scoreFourPop!!,
                        setDataToPopup[i],
                        dialogData?.circularImgBorderFour!!
                    )
                }
            }
        } else {
            // if team has two players
            dialogData?.imgFirstSec?.visibility = View.INVISIBLE
            dialogData?.imgForthSec?.visibility = View.INVISIBLE


            for (i in setDataToPopup.indices) {
                if (setDataToPopup[i].player_key == "1") {
                    setDataOnImageSecForPopup(
                        dialogData?.circularImgTwo!!,
                        dialogData?.titleTwo!!,
                        dialogData?.scoreTwoPop!!,
                        setDataToPopup[i],
                        dialogData?.circularImgBorderTwo!!
                    )

                } else if (setDataToPopup[i].player_key == "2") {
                    setDataOnImageSecForPopup(
                        dialogData?.circularImgThreePop!!,
                        dialogData?.titleThreePop!!,
                        dialogData?.scoreThreePop!!,
                        setDataToPopup[i],
                        dialogData?.circularImgBorderThree!!
                    )
                }
            }
        }


        dialogData?.imgCard?.setOnClickListener {
            dialogData?.dismiss()
        }


        dialogData?.tvResults?.setOnClickListener {
            // <------------add score api---------->
            if (!isNetworkConnected()) {
                showInternetToast()
            } else {
                addScoreVM?.addScoreData(this, roundOne, roundTwo, roundThree, matchId!!)
            }
        }

        dialogData?.show()
    }

    private fun showBlueText(teamOneFirst: TextView?) {
        teamOneFirst?.setTextColor(ContextCompat.getColor(this, R.color.theme_blue))
    }

    private fun showGreyText(teamOneFirst: TextView?) {
        teamOneFirst?.setTextColor(ContextCompat.getColor(this, R.color.contact_clr))
    }

}