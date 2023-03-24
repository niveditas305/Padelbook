package snow.app.padelbook.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login_screen.*
import kotlinx.android.synthetic.main.popup_quiz.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import snow.app.padelbook.R
import snow.app.padelbook.common.statusBarTransparent
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.viewModel.LoginVM

class LoginScreen : BaseActivity() {
    private var loginViewModel: LoginVM? = null
    private var loginPref: SessionClass? = null
    private var mCallbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        loginViewModel = LoginVM()
        loginPref = SessionClass(this)
        mCallbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(this)
        // Log.e("logintoken",SessionClass(this).deviceFCMToken)
        clicks()
        observer()
    }

    private fun observer() {

        //<-------------------login api Response ------------>
        loginViewModel?.userLogin?.observe(this, Observer { user ->
            if (user.status) {
                if (user.data.step_type == 1) {
                    startActivity(
                        Intent(this, OtpScreenActivity::class.java)
                            .putExtra("userId", user.data.id)
                            .putExtra("email",user.data.email)
                            .putExtra("login","from_login")
                    )
                } else if (user.data.step_type == 2) {
                    loginPref?.loginData = user.data
                    startActivity(Intent(this, QusetionAnsScreen::class.java))
                }
//                else if(user.data.step_type == 3){
//                    loginPref?.loginData = user.data
//                    startActivity(Intent(this, QuestAnsTwoActivity::class.java)
//                        .putExtra("QUIZ_ID",user.data.quiz_type_id))
//                }
                else {
                    showToast(this, user.message)
                    SessionClass(this).loginData = user.data
                    SessionClass(this).isLogin = true
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                }
            } else {
                showToast(this, user.message)
            }        // <-------------------------------------------------->


        // <------------------social login response ------------->

        })
        loginViewModel?.userSocialLogin?.observe(this, Observer { user ->
            if(user != null){
                if(user.status){
                    if (user.data.step_type == 1) {
                        startActivity(
                            Intent(this, OtpScreenActivity::class.java)
                                .putExtra("userId", user.data.id)
                        )
                    } else if (user.data.step_type == 2) {
                        loginPref?.loginData = user.data
                        startActivity(Intent(this, QusetionAnsScreen::class.java))
                    }

                    else {
                        showToast(this, user.message)
                        SessionClass(this).loginData = user.data
                        SessionClass(this).isLogin = true
                        startActivity(Intent(this, HomeActivity::class.java))
                        finishAffinity()
                    }
                }
                else{
                    showToast(this,user.message)
                }
            }
            else{
                showToast(this,getString(R.string.something_went_wrong))
            }
        })

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

        dialogData.tvDes.visibility = View.GONE
        dialogData.butSms.visibility = View.VISIBLE
        dialogData.popupSubmit.setText(getString(R.string.but_email))

        dialogData.butSms.setOnClickListener {
            startActivity(
                Intent(this, EmailActivity::class.java)
                    .putExtra("From", "SMS")
            )
        }
        dialogData.popupSubmit.setOnClickListener {
            startActivity(
                Intent(this, EmailActivity::class.java)
                    .putExtra("From", "Email")
            )
            dialogData.dismiss()
            finish()
        }
        dialogData.heading.setText(getString(R.string.heading_email_popup))

        dialogData.show()
    }

    fun clicks() {
        ivBack.setSafeOnClickListener {
            onBackPressed()
        }
        tvForgetPass.setSafeOnClickListener {
            showDialog()
        }

        tvLogin.setSafeOnClickListener {
            if (!isNetworkConnected()) {
                showToast(this, getString(R.string.no_internet_connection))
            } else {
                if (etEmail.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_email))
                } else if (!isEmailValid(etEmail.text.toString())) {
                    showToast(this, getString(R.string.please_enter_valid_email))
                } else if (etPassword.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_pass))
                } else {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.e("devicetoken", "fire--  " + task.result)

                            // <----------------call login api------------------->
                            loginViewModel?.
                            loginData(
                                this, etEmail.text.toString(), etPassword.text.toString(),
                                "1", task.result
                            )
                        }
                    }


                }
            }

        }

        tvFb.setSafeOnClickListener {
            signOutFB()
            LoginManager.getInstance()
                .logInWithReadPermissions(
                    this,
                    listOf("public_profile", "email", "user_friends")
                )
            loginWithFacebook()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        // for facebook signing in...
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
    }

    private fun loginWithFacebook() {
        LoginManager.getInstance()
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        Log.d("TAG", "" + AccessToken.getCurrentAccessToken().token)
                        val request =
                            GraphRequest.newMeRequest(result!!.accessToken) { jsonObject, response ->
                                Log.d("TAG", jsonObject.toString())
                                Log.d("responseFacebook", response.toString())
                                var iD = jsonObject.getString("id")
                                var email = ""
                                var name = ""
                                var image_url = ""
                                var facebookId = ""

                                if (jsonObject.has("email")) {
                                    email = jsonObject.getString("email")
                                    Log.v("tag", email)
                                }
                                if (jsonObject.has("picture")) {
                                    var imageUrl = jsonObject.getString("picture")
                                    val obj = JSONObject(imageUrl)
                                    val obj2 = obj.getJSONObject("data")

                                    image_url =
                                        "https://graph.facebook.com/" + iD + "/picture?type=large"
                                }
                                if (jsonObject.has("name")) {
                                    name = jsonObject.getString("name")
                                    Log.v("tag", name)
                                }
                                if (jsonObject.has("id")) {
                                    facebookId = jsonObject.getString("id")
                                    Log.v("tag", facebookId)
                                }

                                if (email == "") {
                                    showToast(this@LoginScreen, "Email is required!")
                                } else {

                                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                       if(task.isSuccessful) {
                                    loginViewModel?.socialLoginData(
                                        this@LoginScreen,
                                        facebookId,
                                        email, "",
                                        "1"
                                    )
                                        }
                                }

                                }

                            }

                        val parameters = Bundle()

                        parameters.putString("fields", "id, name,email,picture")
                        request.parameters = parameters
                        request.executeAsync()
                    }
                }

                override fun onCancel() {
                    showToast(this@LoginScreen, "Cancel")
                }

                override fun onError(error: FacebookException?) {
                    showToast(this@LoginScreen, error?.localizedMessage)
                }

            })
    }



    private fun signOutFB() {
        LoginManager.getInstance().logOut();
    }
}
