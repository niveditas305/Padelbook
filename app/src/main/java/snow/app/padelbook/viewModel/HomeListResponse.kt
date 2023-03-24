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

class HomeListResponse : BaseObservable() {
    var homeList : MutableLiveData<HomeDataResponse>?= null
    var contactList : MutableLiveData<ContactListDataResponse>?= null
    var userFailure : MutableLiveData<String>?= null
    var changeLocationnn : MutableLiveData<LocationRsponse> ?= null
    init {
        homeList = MutableLiveData()
        contactList = MutableLiveData()
        userFailure = MutableLiveData()
        changeLocationnn = MutableLiveData()
    }
    fun contactListData(context : Context,contactlist : ContactListData){
//        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).postOrder(contactlist).
        enqueue(object : Callback<ContactListDataResponse> {
            override fun onResponse(
                call: Call<ContactListDataResponse>,
                response: Response<ContactListDataResponse>) {
               // dismissProgress()
               // Log.d("OnResponse",response.message())
                contactList?.value = response.body()
            }

            override fun onFailure(call: Call<ContactListDataResponse>, t: Throwable) {
              //  dismissProgress()
//                Log.d("OnFailure",t.localizedMessage)
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
                    Log.e("OnResponse",response.message())
                    changeLocationnn?.value = response.body()
                }

                override fun onFailure(call: Call<LocationRsponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }
    fun homeData(context : Context,page:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).homeData(page)
            .enqueue(object : Callback<HomeDataResponse>{
                override fun onResponse(
                    call: Call<HomeDataResponse>,
                    response: Response<HomeDataResponse>) {
                    dismissProgress()
                  //  Log.d("OnResponse",response.message())
                    homeList?.value = response.body()
                }

                override fun onFailure(call: Call<HomeDataResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }
}