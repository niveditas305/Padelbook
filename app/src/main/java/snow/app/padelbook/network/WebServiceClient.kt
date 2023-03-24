package snow.app.padelbook.network

import android.content.Context
import android.util.Log
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import snow.app.padelbook.common.BASE_URL
import snow.app.padelbook.session.SessionClass
import java.util.*
import java.util.concurrent.TimeUnit

class WebServiceClient(context : Context) {
     var mContext = context
     private lateinit var interceptor: HttpLoggingInterceptor
     private lateinit var okHttpClient: OkHttpClient
     private val REQUEST_TIMEOUT = 20
     private var retrofit: Retrofit? = null
     var mToken = ""

     val client : Retrofit get() {

          if (SessionClass(mContext).loginData != null) {
               SessionClass(mContext).loginData.token?.let {
                    mToken = it
               }

             //  Log.v("request_auth_token", mToken)
          }

          interceptor = HttpLoggingInterceptor()
          interceptor.level = HttpLoggingInterceptor.Level.BODY
          okHttpClient = OkHttpClient.Builder()
               .addInterceptor(interceptor)
               .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                         .addHeader("Accept", "application/json")
                         .addHeader("Content-Type", "application/json")
                         .addHeader("Authorization", "Bearer " + mToken)
                    val request = requestBuilder.build()
                    chain.proceed(request)
               }
               .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
               /*   .connectionSpecs(Arrays.asList(
                          ConnectionSpec.MODERN_TLS,
                          ConnectionSpec.COMPATIBLE_TLS))*/
               .followRedirects(true)
               .followSslRedirects(true)
               .retryOnConnectionFailure(true)
               .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
               .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
               .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
               .cache(null)
               .build()

          if (retrofit == null) {
               retrofit = Retrofit.Builder()
                   //  .baseUrl("http://103.99.202.37/padelbook/api/")
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
          }
          return retrofit!!

     }


}