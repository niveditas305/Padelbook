package snow.app.padelbook.ui

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome_screen.*
import snow.app.padelbook.R

class WelcomeScreen : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)

        statusBarTransparent()
        //views clicks
        clicks()
        changeLanguage("el")
        tvLogin.text = getString(R.string.login)
        tvRegister.text = getString(R.string.register)
        tvOr.text = getString(R.string.or)
    }

    fun clicks() {
        tvLogin.setSafeOnClickListener {
            startActivity(Intent(this@WelcomeScreen, LoginScreen::class.java))
        }
        tvRegister.setSafeOnClickListener {
            startActivity(Intent(this@WelcomeScreen, RegisterScreen::class.java))
        }
        clLanguage.setOnClickListener {
            if (changeLanguageTextView.text.equals("GR")) {
                changeLanguage("en")
                changeLanguageTextView.text = "EN"
            } else {
                changeLanguage("el")
                changeLanguageTextView.text = "GR"

            }
            tvLogin.text = getString(R.string.login)
            tvRegister.text = getString(R.string.register)
            tvOr.text = getString(R.string.or)
        }

    }


}