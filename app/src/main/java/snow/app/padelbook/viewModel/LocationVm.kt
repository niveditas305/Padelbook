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
import snow.app.padelbook.network.responses.LocationRsponse
import snow.app.padelbook.network.responses.contactListResponse.ContactListDataResponse
import snow.app.padelbook.network.responses.homeList.HomeDataResponse
import snow.app.padelbook.network.responses.login.LoginResponse
import snow.app.padelbook.network.responses.score.addScore.AddScoreResponse
import snow.app.padelbook.network.sendJsonData.ContactListData

class LocationVm : BaseObservable() {


    var changeLocationnn : MutableLiveData<String> ?= null
    init {


        changeLocationnn = MutableLiveData<String>()
    }



    fun automaticLocationUpdate(context : Context,lat:String,lng:String,is_location:String,address:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .automaticLocationStatus(lat,lng,is_location,address).enqueue(object : Callback<LocationRsponse>{
                override fun onResponse(call: Call<LocationRsponse>,
                                        response: Response<LocationRsponse>) {
                    dismissProgress()
                    Log.e("sssssss","ssssss")
                    changeLocationnn?.value = ""
                   // Log.e("OnResponse",response.message())
                }

                override fun onFailure(call: Call<LocationRsponse>, t: Throwable) {
                    dismissProgress()
                    Log.e("OnFailure",t.localizedMessage)
                   // changeLocationnn?.value = "Something went wrong"
                }

            })
    }

}