package snow.app.padelbook.ui

import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import snow.app.padelbook.R
import snow.app.padelbook.common.Utils
import snow.app.padelbook.utils.MyApplication
import snow.app.padelbook.utils.SafeClickListener
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


open class BaseActivity : AppCompatActivity() {
    private var device_token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun goToNextScreen(aClass: Class<*>?) {
        startActivity(Intent(this, aClass))
    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    fun changeLanguage(langType: String) {
        val res: Resources = this.resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.setLocale(Locale(langType))
        res.updateConfiguration(conf, dm)
    }

    open fun getDeviceToken(): String? {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            Log.e("devicetoken", "fire--  " + task.result)
            device_token = task.result
            //  showToast("device token"+device_token);

        }

        return device_token
    }


    fun isNetworkConnected(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    open fun showInternetToast() {
        Toast.makeText(MyApplication.appInstance.applicationContext, "No Internet Connection!", Toast.LENGTH_SHORT).show()
    }

    open fun isEmailValid(email: String?): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return Pattern.compile(emailPattern).matcher(email).matches()
    }

    fun dateFormatWithMonthNameFull(date: String): String {
       /* val myFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date)
        var myDate = SimpleDateFormat("EEEE, dd/MM").format(myFormat)
        return myDate*/
        val df = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
        try {
            val date = df.parse(date)
            val dateFormat = SimpleDateFormat("EEEE, dd/MM", Locale.ENGLISH)
            return dateFormat.format(date)
        } catch (ex: ParseException) {
            ex.message?.let { Log.d("Parse Exception", it) }
        }
        return ""
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun statusBarTransparent() {
        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = Color.TRANSPARENT
            }


            val rectangle = Rect()
            val window: Window = window
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle)
            val statusBarHeight: Int = rectangle.top

        }
    }


    fun dateFormatConversion(date: String): String {
     /*   val myFormat = SimpleDateFormat("dd MM yyyy").parse(date)
        var myDate = SimpleDateFormat("dd MMMM yyyy").format(myFormat)
        return myDate*/
        val df = SimpleDateFormat("dd MM yyyy", Locale.ENGLISH)
        try {
            val date = df.parse(date)
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            return dateFormat.format(date)
        } catch (ex: ParseException) {
            ex.message?.let { Log.d("Parse Exception", it) }
        }
        return ""
    }

    fun dateFormatConversionLocale(date: String, locale: String): String {
       /* val myFormat = SimpleDateFormat("dd MM yyyy").parse(date)
        var myDate = SimpleDateFormat("dd MMMM yyyy", Locale(locale)).format(myFormat)
        return myDate*/
        val df = SimpleDateFormat("dd MM yyyy" )
        try {
            val date = df.parse(date)
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale(locale))
            return dateFormat.format(date)
        } catch (ex: ParseException) {
            ex.message?.let { Log.d("Parse Exception", it) }
        }
        return ""
    }

    fun dateToApi(date: String): String {
        val myFormat = SimpleDateFormat("dd MMMM yyyy").parse(date)
        var myDate = SimpleDateFormat("dd-MM-yyyy").format(myFormat)
        return myDate
    }

    fun dateFormatConversionPutPattern(
        date: String,
        inputFormat: String,
        outputFormat: String
    ): String {
        val myFormat = SimpleDateFormat(inputFormat).parse(date)
        var myDate = SimpleDateFormat(outputFormat).format(myFormat)
        return myDate
    }

    fun dateFormatWithHyphanConversion(date: String): String {
    /*    var myDate: String? = null
        try {
            val myFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date)
            myDate = SimpleDateFormat("yyyy-MM-dd").format(myFormat)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return myDate ?: ""*/
        val df = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
        try {
            val date = df.parse(date)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            return dateFormat.format(date)
        } catch (ex: ParseException) {
            ex.message?.let { Log.d("Parse Exception", it) }
        }
        return ""
    }


    fun showSoftKeyboard(view: View, context: Context) {
        view.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            view.post(Runnable {
                val imm =
                    (context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                        showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                    }
            })
        }
        view.requestFocus()
    }


    fun hideSoftKeyboard(view: View, context: Context) {
        //  val v = this.currentFocus

        // If Soft Keyboard is visible, it will be hide
        if (view != null) {
            //If using a fragment use getActivity().getSystemService(...)
            val inputManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }


    fun showToast(context: Context?, toast_string: String?) {
        Toast.makeText(context, toast_string, Toast.LENGTH_SHORT).show()
    }


}