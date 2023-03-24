package snow.app.padelbook.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_otp_screen.*
import kotlinx.android.synthetic.main.toolbar.view.*
import snow.app.padelbook.R
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.ui.EmailActivity.Companion.timerTime
import snow.app.padelbook.viewModel.OtpVerifyVM
import java.util.concurrent.TimeUnit

class OtpScreenActivity : BaseActivity() {
    private var otpVerify: OtpVerifyVM? = null
    var userID: String? = null
    var isFromForgot: Boolean = false
    var fromForgotValue: String = ""
    var emailNoValue:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_screen)

        otpVerify = OtpVerifyVM()
        userID = intent.extras?.get("userId") as String?

        if (intent.hasExtra("fromForgot")) {
            fromForgotValue = intent.getStringExtra("fromForgot").toString()
            isFromForgot = true
            emailNoValue =intent.getStringExtra("id").toString()
            startTimerForgot()
        } else if (intent.hasExtra("login")) {
            getOtpOnEmailTextView.visibility = View.VISIBLE
            tvResendOtp.visibility = View.GONE
            tvTimer.visibility = View.GONE

        } else {
            startTimer()
        }
        setToolbar()
        setOtpListener()
        observer()

        SessionClass(this).loginData?.let {
            if (SessionClass(this).loginData.language_type == "0") {
                changeLanguage("en")
            } else {
                changeLanguage("el")
            }
        }


    }

    private fun observer() {
        // < ---------------otp verify response---------------->
        otpVerify?.userOtp?.observe(this, Observer { user ->
            if (user.status) {
                showToast(this, user.message)
                SessionClass(this).loginData = user.data
                goToNextScreen(QusetionAnsScreen::class.java)
                finish()
            } else {
                showToast(this, user.message)
            }
        })
        otpVerify?.userFailure?.observe(this, Observer { user ->
            showToast(this, user)
        })
        otpVerify?.resendOtp?.observe(this, Observer { user ->
            user?.let {
                if (it.status == true) {
                    showToast(this, user.message)
                    startTimer()
                } else {
                    showToast(this, user.message)
                }
            }
        })

        otpVerify?.resendOtpForgot?.observe(this, Observer { user ->
            user?.let {
                if (it.status == true) {
                    showToast(this, user.message)
                    if (intent.hasExtra("login")) {
                        startTimer()
                    } else {
                        timerTime = 45000L
                        startTimerForgot()
                    }

                } else {
                    showToast(this, user.message)
                }
            }
        })
        otpVerify?.verifyForgotOtp?.observe(this, Observer { user ->
            user?.let {
                if (it.status == true) {
                    showToast(this, user.message)
                    user.data?.let {
                        startActivity(
                            Intent(
                                this@OtpScreenActivity,
                                ResetPasswordActivity::class.java
                            ).putExtra(
                                "userIdForgot", user.data?.id.toString()
                            )
                        )
                    }

                } else {
                    showToast(this, user.message)
                }
            }
        })
        // </--------------------------------------------------->
    }

    private fun setOtpListener() {
        getOtpOnEmailTextView.setOnClickListener {
            if (isNetworkConnected()) {
                if (isFromForgot) {
                    otpVerify?.resendOtpForgot(
                        this,emailNoValue , "2"
                    )
                } else {
                    otpVerify?.resendOtpForgot(
                        this, SessionClass(this).loginData.email, "2"
                    )
                }
            } else {
                showInternetToast()
            }
        }

        tvResendOtp.setOnClickListener {
            if (isFromForgot) {
                if (isNetworkConnected()) {
                    otpVerify?.resendOtpForgot(
                        this,
                        intent.getStringExtra("id").toString(),
                        intent.getStringExtra("fromForgot").toString()
                    )
                } else {
                    showInternetToast()
                }
            } else {
                if (isNetworkConnected()) {
                    otpVerify?.resendOtp(this, SessionClass(this).loginData.id.toString())
                } else {
                    showInternetToast()
                }
            }

        }
        otpView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == otpView.itemCount) {
                    /*  if (intent?.extras?.get("callFromSMSResetPass") == "callFromSMSResetPass") {
                          startActivity(
                              Intent(
                                  this@OtpScreenActivity,
                                  ResetPasswordActivity::class.java
                              )
                          )
                          finish()*/

                    if (isFromForgot) {
                        if (isNetworkConnected()) {
                            otpVerify?.verifyForgetOtp(
                                this@OtpScreenActivity,
                                intent.getStringExtra("id").toString(),
                                s.toString(),
                                "2"
                            )
                        } else {
                            showInternetToast()
                        }


                    } else {
                        // <--------------call api otp verify ----------->
                        if (isNetworkConnected()) {
                            otpVerify?.otpVerifyData(this@OtpScreenActivity, userID!!, s.toString())
                        } else {
                            showInternetToast()
                        }
                        // </-------------------------------------------->
                    }
                }
            }
        })
    }

    private fun setToolbar() {
        toolbar.tvTitle.visibility = View.VISIBLE
        if (isFromForgot) {
            toolbar.tvTitle.setText(getString(R.string.verify_otp))
        } else {
            toolbar.tvTitle.setText(getString(R.string.otp_title))
        }

        toolbar.separator.visibility = View.VISIBLE
        toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(45000, 1000) {
            override fun onFinish() {
                getOtpOnEmailTextView.isEnabled = true
                getOtpOnEmailTextView.visibility = View.VISIBLE
                tvResendOtp.visibility = View.GONE
                tvTimer.visibility = View.INVISIBLE

            }

            override fun onTick(millisUntilFinished: Long) {
                getOtpOnEmailTextView.isEnabled = false
                tvTimer.visibility = View.VISIBLE
                tvResendOtp.visibility = View.GONE
                getOtpOnEmailTextView.visibility = View.GONE

                tvTimer.text = getString(R.string._1_17) + " " + String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
            }
        }
        countDownTimer.start()
    }

    private fun startTimerForgot() {
        val countDownTimer = object : CountDownTimer(timerTime, 1000) {
            override fun onFinish() {


                tvTimer.visibility = View.INVISIBLE
                if (isFromForgot && fromForgotValue.equals("1")){
                    tvResendOtp.visibility = View.VISIBLE
                    getOtpOnEmailTextView.visibility = View.GONE
                    getOtpOnEmailTextView.isEnabled = false
                    tvResendOtp.isEnabled = true

                }else{
                    getOtpOnEmailTextView.visibility = View.VISIBLE
                    tvResendOtp.visibility = View.GONE
                    getOtpOnEmailTextView.isEnabled = true
                    tvResendOtp.isEnabled = false
                }


            }

            override fun onTick(millisUntilFinished: Long) {
                timerTime = millisUntilFinished
                tvResendOtp.isEnabled = false
                tvTimer.visibility = View.VISIBLE
                tvResendOtp.visibility = View.GONE
                getOtpOnEmailTextView.visibility = View.GONE

                tvTimer.text = getString(R.string._1_17) + " " + String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )
            }
        }
        countDownTimer.start()
    }
}