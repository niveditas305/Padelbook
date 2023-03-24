package snow.app.padelbook.viewModel

import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snow.app.padelbook.common.Utils.dismissProgress
import snow.app.padelbook.common.Utils.showProgress
import snow.app.padelbook.network.ApiInterface
import snow.app.padelbook.network.WebServiceClient
import snow.app.padelbook.network.responses.LocationRsponse
import snow.app.padelbook.network.responses.login.LoginResponse
import snow.app.padelbook.network.responses.score.addScore.AddScoreResponse

class ProfileVM : BaseObservable(){
    var userProfile : MutableLiveData<LoginResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    var updateProfile : MutableLiveData<LoginResponse> ?= null
    var updateProfileOnly : MutableLiveData<LoginResponse> ?= null
    var changeLocation : MutableLiveData<LocationRsponse> ?= null
    var changelanguage : MutableLiveData<LoginResponse> ?= null

    init {
        userProfile = MutableLiveData()
        updateProfileOnly = MutableLiveData()
        userFailure = MutableLiveData()
        updateProfile = MutableLiveData()
        changeLocation = MutableLiveData()
        changelanguage = MutableLiveData()
    }

    fun getProfileData(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .getProfile().enqueue(object : Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    userProfile?.value = response.body()
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }

    fun updateProfileData(context : Context,name : RequestBody,gender:RequestBody,password:RequestBody,
                          date_of_birth:RequestBody, address: RequestBody,latitude:RequestBody,
                          longitude:RequestBody, image_file:MultipartBody.Part){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).updateProfile(name,gender,password,
            date_of_birth,address,latitude,longitude,image_file).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                dismissProgress()
                //Log.d("OnResponse",response.message())
                updateProfile?.value = response.body()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
               dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value = "Something went wrong"
            }

        })

    }


    fun updateProfileDataImageOnly(context : Context , image_file:MultipartBody.Part){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).updateProfileImageOnly(image_file).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                dismissProgress()
                //Log.d("OnResponse",response.message())
                updateProfileOnly?.value = response.body()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
               dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value = "Something went wrong"
            }

        })

    }

    fun automaticLocationUpdate(context : Context,lat:String,lng:String,is_location:String,address:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .automaticLocationStatus(lat,lng,is_location,address).enqueue(object : Callback<LocationRsponse>{
                override fun onResponse(call: Call<LocationRsponse>,
                    response: Response<LocationRsponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    changeLocation?.value = response.body()
                }

                override fun onFailure(call: Call<LocationRsponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
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


}