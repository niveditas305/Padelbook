package snow.app.padelbook.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_register_screen.*
import kotlinx.android.synthetic.main.toolbar.*
import snow.app.padelbook.R
import snow.app.padelbook.viewModel.RegisterVM
import android.text.style.UnderlineSpan

import android.R.string.no
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import snow.app.padelbook.common.BASE_URL_ONE
import snow.app.padelbook.common.TERMS_AND_CONDITIONS
import snow.app.padelbook.session.SessionClass


class RegisterScreen : BaseActivity() {

    private var registerVM: RegisterVM? = null
    var language_type = "0"
    private var loginPref: SessionClass? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
        loginPref = SessionClass(this)
        // statusBarTransparent()
        registerVM = RegisterVM()
        clicks()
        observer()
        val locale: String = getResources().getConfiguration().locale.getCountry()
        Log.e("locale", "--" + locale)
        ccp.setDefaultCountryUsingNameCode(locale)


    }

    private fun seeMoreClick(tvDesTwo: TextView) {
        // <------------- for see more click
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val i = Intent("android.intent.action.VIEW")
                i.data = Uri.parse(/*BASE_URL_ONE + "club/login/terms_and_conditions"*/TERMS_AND_CONDITIONS)
                startActivity(i)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        val termsText = getString(R.string.i_accept_terms_and_conditions)
        val startIndex = termsText.indexOf(getString(R.string.terms_n_conditions))
        val endIndex =  termsText.length
        val spannableString = SpannableString(termsText)

        Log.d("startIndex", startIndex.toString())
        Log.d("endIndex", endIndex.toString())

        //only the word '...see more' is clickable

        spannableString.setSpan(UnderlineSpan(), startIndex,endIndex, 0)

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // --------------------------------------------------/>
        tvDesTwo.text = spannableString
        tvDesTwo.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun observer() {
        registerVM?.userRegister?.observe(this, Observer { user ->
            showToast(this, user.message)
            if (user.status) {
                showToast(this, user.message)
                loginPref?.loginData = user.data

                if(user.data.language_type == "0"){
                    changeLanguage("en")
                }
                else{
                    changeLanguage("el")
                }
                startActivity(
                    Intent(this, OtpScreenActivity::class.java)
                        .putExtra("userId", user.data.id)
                )
                finish()
            } else {
                showToast(this, user.message)
            }
        })
    }

    fun clicks() {
        ivBack.setSafeOnClickListener {
            onBackPressed()
        }
        llMobile.setOnClickListener {
            llMobile.setBackgroundTintList(getResources().getColorStateList(R.color.theme_blue));

        }

        etMobile.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                llMobile.background =
                    ContextCompat.getDrawable(this, R.drawable.rect_edit_border_cp)
            } else {
                llMobile.background = ContextCompat.getDrawable(this, R.drawable.rect_edit_border)
            }
        }
        seeMoreClick(termCondition)
        tvRegister.setSafeOnClickListener {
            if (!isNetworkConnected()) {
                showInternetToast()
            } else {
                if (etName.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_name))
                } else if (etEmail.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_email))
                } else if (!isEmailValid(etEmail.text.toString().trim())) {
                    showToast(this, getString(R.string.please_enter_valid_email))
                } else if (etPassword.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_pass))
                } else if (etPassword.text.toString().length < 6) {
                    showToast(this, getString(R.string.pass_validation))
                } else if (etConfirmPass.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_confirm_password))
                } else if (etConfirmPass.text.toString() != etPassword.text.toString()) {
                    showToast(this, getString(R.string.pass_and_confirm_pass_should_be_same))
                } else if (etMobile.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_mobile_no))
                } else if (!rbTerms.isChecked) {
                    showToast(this, getString(R.string.please_accept_terms_and_conditions))
                } else {
                    if (ccp.selectedCountryCodeWithPlus.equals("+30")) {
                        language_type = "1"
                    } else {
                        language_type = "0"
                    }

                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            registerVM?.registerData(
                                this,
                                etName.text.toString(),
                                etEmail.text.toString(),
                                etPassword.text.toString(),
                                etConfirmPass.text.toString(),
                                /*ccp.selectedCountryCodeWithPlus+ */etMobile.text.toString(), ccp.selectedCountryCodeWithPlus,
                                "1",
                                task.result, language_type
                            )
                        }
                    }
                }
            }

        }
    }
}