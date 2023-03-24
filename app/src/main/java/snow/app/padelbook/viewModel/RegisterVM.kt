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
import snow.app.padelbook.network.responses.register.RegisterResponse

class RegisterVM : BaseObservable(), Callback<LoginResponse> {
    var userRegister : MutableLiveData<LoginResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null
    var changelanguage : MutableLiveData<LoginResponse> ?= null
    init {
        userRegister = MutableLiveData()
        userFailure  = MutableLiveData()
        changelanguage = MutableLiveData()
    }

    fun registerData(context : Context,name:String,email:String,password:String,password_confirmation:String,
                     phone:String, country_code:String,device_type:String,device_token:String,language_type:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).register(email,password,password_confirmation,
                       phone,country_code,device_type,name, language_type = language_type,device_token).enqueue(this)

    }
    fun changeLanguageUpdate(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .changeLanguage().enqueue(object : Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>,
                                        response: Response<LoginResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    changelanguage?.value = response.body()
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }


    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
        dismissProgress()
        Log.d("OnResponse",response.message())
        userRegister?.value = response.body()
    }

    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
        dismissProgress()
        Log.d("OnFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }
}