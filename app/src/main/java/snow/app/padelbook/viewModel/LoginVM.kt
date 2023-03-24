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
import snow.app.padelbook.network.responses.login.LoginResponse

class LoginVM : BaseObservable() {
    var userLogin : MutableLiveData<LoginResponse> ?= null
    var userSocialLogin : MutableLiveData<LoginResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        userLogin = MutableLiveData()
        userSocialLogin = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun loginData(context : Context,email: String,password : String,
                  device_type : String,device_token : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).loginData(email,password,
            device_type,device_token).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                userLogin?.value = response.body()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value = "Something went wrong"
            }

        })
    }

    fun socialLoginData(context : Context,facebook_id : String,email : String,device_token : String, device_type : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .socialLogin(facebook_id, email,device_token,device_type).enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    userSocialLogin?.value = response.body()
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })

    }


}