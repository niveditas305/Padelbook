package snow.app.padelbook.utils

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MyApplication : Application() {
    companion object {
        lateinit var appInstance: Application
    }

    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        appInstance = this
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    }
}