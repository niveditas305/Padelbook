package snow.app.padelbook.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import snow.app.padelbook.R
import snow.app.padelbook.common.statusBarTransparent
import snow.app.padelbook.network.sendJsonData.ContactListSend
import snow.app.padelbook.session.SessionClass
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashScreen : BaseActivity() {
    companion object{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //open next screen
      //  statusBarTransparent()
        openLoginScreen()

        geneateKeyHash()


    }
    private fun geneateKeyHash() {
        // Add code to print out the key hash
        // Add code to print out the key hash
        try {
            val info = packageManager.getPackageInfo(
                "snow.app.padelbook",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            showToast(this,e.message.toString())
        } catch (e: NoSuchAlgorithmException) {
            showToast(this,e.message.toString())
        }
    }

    fun openLoginScreen() {

        val handler = Handler()
        handler.postDelayed({
            if(SessionClass(this).isLogin == true) {
                startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
                finish()
            }
            else
            {
                startActivity(Intent(this@SplashScreen, WelcomeScreen::class.java))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity()
                }
                else {
                    finish()
                }
            }

        }, 3000)
    }
}