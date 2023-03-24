package snow.app.padelbook.viewModel

import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snow.app.padelbook.common.Utils.dismissProgress
import snow.app.padelbook.common.Utils.showProgress
import snow.app.padelbook.network.ApiInterface
import snow.app.padelbook.network.WebServiceClient
import snow.app.padelbook.network.responses.forgototpverified.ForgotOtpVerified
import snow.app.padelbook.network.responses.login.LoginResponse
import snow.app.padelbook.network.responses.resendotp.ResendOtpModel

class OtpVerifyVM : BaseObservable(), Callback<LoginResponse> {
    var userOtp : MutableLiveData<LoginResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null
    var resendOtp : MutableLiveData<ResendOtpModel> ?= null
    var resendOtpForgot : MutableLiveData<ResendOtpModel> ?= null
    var verifyForgotOtp : MutableLiveData<ForgotOtpVerified> ?= null
    var resetForgotPassword : MutableLiveData<ForgotOtpVerified> ?= null

    init {
        userOtp = MutableLiveData()
        userFailure = MutableLiveData()
        resendOtp = MutableLiveData()
        resendOtpForgot = MutableLiveData()
        verifyForgotOtp = MutableLiveData()
        resetForgotPassword = MutableLiveData()
    }

    fun otpVerifyData(context : Context,id : String, otp:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).otpVerify(id,otp).enqueue(this)
    }

    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
        dismissProgress()
        Log.d("OnResponse",response.message())
        userOtp?.value = response.body()
    }

    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
        dismissProgress()
        Log.d("OnFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }

    fun resendOtp(context : Context,id: String ){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).resendOtp(id).enqueue(object : Callback<ResendOtpModel>{
            override fun onResponse(call: Call<ResendOtpModel>, response: Response<ResendOtpModel>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                resendOtp?.value = response.body()
            }

            override fun onFailure(call: Call<ResendOtpModel>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value = "Something went wrong"
            }

        })
    }

    fun resendOtpForgot(context : Context,id: String,type: String ){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).resendOtpTwo(id,type).enqueue(object : Callback<ResendOtpModel>{
            override fun onResponse(call: Call<ResendOtpModel>, response: Response<ResendOtpModel>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                resendOtpForgot?.value = response.body()
            }

            override fun onFailure(call: Call<ResendOtpModel>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value = "Something went wrong"
            }

        })
    }
    fun verifyForgetOtp(context : Context,id: String,otp: String,type: String ){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).verifyForgetOtp(id,otp,type)
            .enqueue(object : Callback<ForgotOtpVerified>{
            override fun onResponse(call: Call<ForgotOtpVerified>, response: Response<ForgotOtpVerified>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                verifyForgotOtp?.value = response.body()
            }

            override fun onFailure(call: Call<ForgotOtpVerified>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value =  t.message.toString()
            }

        })
    }
   fun resetPassword(context : Context,user_id: String,password: String ){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).resetPassword(user_id,password)
            .enqueue(object : Callback<ForgotOtpVerified>{
            override fun onResponse(call: Call<ForgotOtpVerified>, response: Response<ForgotOtpVerified>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                resetForgotPassword?.value = response.body()
            }

            override fun onFailure(call: Call<ForgotOtpVerified>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value =  t.message.toString()
            }

        })
    }

}