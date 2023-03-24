package snow.app.padelbook.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_email.*
import kotlinx.android.synthetic.main.activity_email.ccp
import kotlinx.android.synthetic.main.activity_email.edEmail
import kotlinx.android.synthetic.main.activity_email.etEmail
import kotlinx.android.synthetic.main.activity_email.etMobile
import kotlinx.android.synthetic.main.activity_otp_screen.*
import kotlinx.android.synthetic.main.popup_quiz.*
import kotlinx.android.synthetic.main.toolbar.*
import snow.app.padelbook.R
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.viewModel.OtpVerifyVM
import java.util.concurrent.TimeUnit

class EmailActivity : BaseActivity() {
    private var callFromSec: String? = null
    private var otpVerify: OtpVerifyVM? = null
    var isFromEmail = false

    companion object {
        var timerTime = 45000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        otpVerify = OtpVerifyVM()
        setToolbar()
        setClick()

        callFromSec = intent?.extras?.get("From") as String

        if (callFromSec == "Email") {
            isFromEmail = true
            llMobile.visibility = View.GONE
            edEmail.visibility = View.VISIBLE
            tvSubmit.setText(getString(R.string.but_email))

        } else {
            isFromEmail = false
            tvSubmit.setText(getString(R.string.sms_submit))
            llMobile.visibility = View.VISIBLE
            edEmail.visibility = View.GONE
        }

        observers()

        val locale: String = getResources().getConfiguration().locale.getCountry()
        Log.e("locale", "--" + locale)
        ccp.setDefaultCountryUsingNameCode(locale)
    }

    fun observers() {
        otpVerify?.resendOtpForgot?.observe(this, Observer { user ->
            user?.let {
                if (it.status == true) {
                    showToast(this, user.message)
                    startTimer()
                    tvSubmit.visibility = View.GONE

                    if (isFromEmail) {
                        showDialog()
                    } else {
                        startActivity(
                            Intent(this, OtpScreenActivity::class.java)
                                .putExtra("callFromSMSResetPass", "callFromSMSResetPass")
                                .putExtra("id", etMobile.text.toString())
                                .putExtra("fromForgot", "1")
                        )
                    }
                } else {
                    showToast(this, user.message)
                }
            }


        })
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(timerTime, 1000) {
            override fun onFinish() {
                tvResendOtpBtn.isEnabled = true
                tvResendOtpBtn.visibility = View.VISIBLE
                tvTimerButton.visibility = View.INVISIBLE
                tvSubmit.visibility = View.GONE

            }

            override fun onTick(millisUntilFinished: Long) {
                timerTime = millisUntilFinished
                tvResendOtpBtn.isEnabled = false
                tvTimerButton.visibility = View.VISIBLE
                tvResendOtpBtn.visibility = View.GONE
                tvSubmit.visibility = View.GONE


                tvTimerButton.text = getString(R.string._1_17) + " " + String.format(
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

    private fun setClick() {
        tvResendOtpBtn.setOnClickListener {
            if (callFromSec == "Email") {
                if (etEmail.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_email))
                } else if (!isEmailValid(etEmail.text.toString())) {
                    showToast(this, getString(R.string.please_enter_email))
                } else {
                    otpVerify?.resendOtpForgot(this, etEmail.text.toString(), "2")
                    //  showDialog()
                }
            } else {
                if (etMobile.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_mobile_no))
                } else {
                    if (isNetworkConnected()) {
                        otpVerify?.resendOtpForgot(this, etMobile.text.toString(), "1")
                    } else {
                        showInternetToast()
                    }


                    /*    startActivity(Intent(this,OtpScreenActivity::class.java)
                            .putExtra("callFromSMSResetPass","callFromSMSResetPass"))*/
                }
            }
        }

        tvTimerButton.setOnClickListener {


            if (callFromSec == "Email") {
                if (etEmail.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_email))
                } else if (!isEmailValid(etEmail.text.toString())) {
                    showToast(this, getString(R.string.please_enter_valid_email))
                } else {
                    startActivity(
                        Intent(this, OtpScreenActivity::class.java)
                            .putExtra("id", etEmail.text.toString())
                            .putExtra("fromForgot", "2")
                    )

                }
            } else {
                if (etMobile.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_mobile_no))
                } else {

                    startActivity(
                        Intent(this, OtpScreenActivity::class.java)
                            .putExtra("callFromSMSResetPass", "callFromSMSResetPass")
                            .putExtra("id", etMobile.text.toString())
                            .putExtra("fromForgot", "1")
                    )

                }
            }

        }

        tvSubmit.setOnClickListener {
            if (isNetworkConnected()) {
                if (callFromSec == "Email") {
                    if (etEmail.text.toString().isEmpty()) {
                        showToast(this, getString(R.string.please_enter_email))
                    } else if (!isEmailValid(etEmail.text.toString())) {
                        showToast(this, getString(R.string.please_enter_valid_email))
                    } else {
                        otpVerify?.resendOtpForgot(this, etEmail.text.toString(), "2")
                        //  showDialog()
                    }
                } else {
                    if (etMobile.text.toString().isEmpty()) {
                        showToast(this, getString(R.string.please_enter_mobile_no))
                    } else {

                        otpVerify?.resendOtpForgot(this, etMobile.text.toString(), "1")
                        /*    startActivity(Intent(this,OtpScreenActivity::class.java)
                                .putExtra("callFromSMSResetPass","callFromSMSResetPass"))*/
                    }
                }
            } else {
                showInternetToast()
            }


        }
    }

    private fun showDialog() {
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(true)
        dialogData.setContentView(R.layout.popup_quiz)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogData.imagePopup.setImageResource(R.drawable.ic_msg_box)

        dialogData.popupSubmit.setOnClickListener {
            //startActivity(Intent(this, ResetPasswordActivity::class.java))
            startActivity(
                Intent(this, OtpScreenActivity::class.java).putExtra("fromForgot", "2")
                    .putExtra("id", etEmail.text.toString())
            )
            dialogData.dismiss()

        }

        dialogData.tvDes.setText(getString(R.string.subheading_email_sent))
        dialogData.heading.setText(getString(R.string.heading_email_sent))
        dialogData.popupSubmit.setText(getString(R.string.email_sent))

        dialogData.show()
    }

    private fun setToolbar() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvTitle.visibility = View.VISIBLE
        tvTitle.setText(getString(R.string.email_heading))
        separator.visibility = View.VISIBLE
    }
}