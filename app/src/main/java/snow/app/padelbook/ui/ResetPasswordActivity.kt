package snow.app.padelbook.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_email.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.etPassword
import kotlinx.android.synthetic.main.activity_reset_password.tvSubmit
import kotlinx.android.synthetic.main.toolbar.*
import snow.app.padelbook.R
import snow.app.padelbook.viewModel.OtpVerifyVM

class ResetPasswordActivity : BaseActivity() {
    var userIdForgot: String? = null
    private var otpVerify: OtpVerifyVM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        otpVerify = OtpVerifyVM()
        setToolbar()
        if (intent.hasExtra("userIdForgot")) {
            userIdForgot = intent.getStringExtra("userIdForgot")
        }
        setClick()
        observers()

    }

    fun observers() {
        otpVerify?.resetForgotPassword?.observe(this, Observer { user ->
            user?.let {
                if (it.status == true) {
                    showToast(this, user.message)
                    startActivity(Intent(this, LoginScreen::class.java))
                    finishAffinity()
                } else {
                    showToast(this, user.message)
                }
            }


        })
    }

    private fun setClick() {
        tvSubmit.setOnClickListener {
            if (etPassword.text.toString().isEmpty()) {
                showToast(this, getString(R.string.please_enter_pass))
            } else if (etPassword.text.toString().length < 6) {
                showToast(this, getString(R.string.pass_validation))
            } else if (edConfirm.text.toString().isEmpty()) {
                showToast(this, getString(R.string.please_enter_confirm_password))

            } else if (edConfirm.text.toString() != etPassword.text.toString()) {
                showToast(this, getString(R.string.pass_and_confirm_pass_should_be_same))
            } else {
                if (isNetworkConnected()) {
                    otpVerify?.resetPassword(
                        this,
                        userIdForgot.toString(),
                        edConfirm.text.toString()
                    )
                } else {
                    showInternetToast()
                }
            }

        }
    }

    private fun setToolbar() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvTitle.visibility = View.VISIBLE
        tvTitle.setText(getString(R.string.reset_password))
        separator.visibility = View.VISIBLE
    }

}