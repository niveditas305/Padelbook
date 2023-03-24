package snow.app.padelbook.fragments

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import snow.app.padelbook.R
import snow.app.padelbook.utils.MyApplication
import snow.app.padelbook.utils.SafeClickListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

open class BaseFragment : Fragment() {
companion object
{
    var mcontextNew: Activity? = null
}
    var mcontext: Activity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)


        mcontext = requireActivity()
    }

    override fun onAttach(context: Activity) {
        super.onAttach(context)
        mcontext = context
    }

    override fun onDetach() {
        super.onDetach()
        mcontext=null
    }
    fun isGpsEnabled() : Boolean{
        var isGPSEnabled = false
        val locationManager = mcontext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled
    }

    open fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    fun isNetworkConnected(): Boolean {

            val cm =
                MyApplication.appInstance.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected


    }

    fun showToast(msg: String?) {

        val toast = Toast.makeText(MyApplication.appInstance.applicationContext, msg, Toast.LENGTH_SHORT)
//        val view = toast.view
//        view!!.background.setColorFilter(
//            ContextCompat.getColor(mcontext!!, R.color.btn_clr_blue_light),
//            PorterDuff.Mode.SRC_IN
//        )
//        val text = view.findViewById<TextView>(android.R.id.message)
//        text.setTextColor(ContextCompat.getColor(mcontext!!, R.color.white))

        toast.show()
    }
    fun dateFormatWithMonthNameFull(date : String) : String {
      /*  val myFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date)
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
    fun showInternetToast() {
        Toast.makeText(MyApplication.appInstance.applicationContext, "No internet connection !", Toast.LENGTH_SHORT).show()
    }
    //Unparseable date: "Thu Mar 02 18:53:50 GMT+02:00 2023"
    fun dateFormatWithHyphanConversion(date : String): String {
       // val myFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date)
        /*val myFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zXXX yyyy").parse(date)
        var myDate = SimpleDateFormat("yyyy-MM-dd").format(myFormat)
        return myDate*/

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
    fun convertDateStringToAnotherFormat(
        dateString: String?,
        format: String,
        requiredFormat: String
    ): String {
        val df = SimpleDateFormat(format, Locale.ENGLISH)
        try {
            val date = df.parse(dateString)
            val dateFormat = SimpleDateFormat(requiredFormat, Locale.ENGLISH)
            return dateFormat.format(date)
        } catch (ex: ParseException) {
            ex.message?.let { Log.d("Parse Exception", it) }
        }
        return ""
    }
    fun  loadImage(url: String, context: Context, imageView: ImageView) {
        var base_url = "https://club.padelbook.gr/public/assets/images/"
        if(url.startsWith("http") || url.startsWith("https")){
            Glide.with(context).asBitmap().load(url).error(R.drawable.ic_user)
                .placeholder(R.drawable.ic_user).into(imageView)
        }
        else{
            Glide.with(context).asBitmap().load(base_url+url).error(R.drawable.ic_user)
                .placeholder(R.drawable.ic_user).into(imageView)
        }

    }

}