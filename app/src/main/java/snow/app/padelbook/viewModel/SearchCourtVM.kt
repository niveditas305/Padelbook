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
import snow.app.padelbook.network.responses.searchResponse.searchCourt.SearchCourtResponse
import snow.app.padelbook.network.responses.searchResponse.searchNearBy.recentHistory.RecentHistoryResponse
import snow.app.padelbook.network.responses.searchResponse.searchNearBy.searchAddressAdd.SearchAddressAddedResponse
import snow.app.padelbook.network.responses.searchResponse.timeSlot.getTime.GetTimeResponse
import snow.app.padelbook.network.responses.searchResponse.timeSlot.updateTimeFilter.UpdateTimeFilterResponse

class SearchCourtVM : BaseObservable() {
    var searchCourt : MutableLiveData<SearchCourtResponse> ?= null
    var recentSearch : MutableLiveData<RecentHistoryResponse> ?= null
    var searchAddressAdd : MutableLiveData<SearchAddressAddedResponse> ?= null

    var getTime : MutableLiveData<GetTimeResponse> ?= null
    var updateTime : MutableLiveData<UpdateTimeFilterResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        searchCourt = MutableLiveData()
        recentSearch = MutableLiveData()
        searchAddressAdd = MutableLiveData()
        getTime = MutableLiveData()
        updateTime = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun searchCourtData(context : Context, title : String){
        showProgress(context)

        WebServiceClient(context).client.create(ApiInterface::class.java).searchCourt(title)
            .enqueue(object : Callback<SearchCourtResponse> {
                override fun onResponse(call: Call<SearchCourtResponse>,
                    response: Response<SearchCourtResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    searchCourt?.value = response.body()
                }

                override fun onFailure(call: Call<SearchCourtResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("onFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }

    fun recentSearchData(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).getRecentHistory()
            .enqueue(object : Callback<RecentHistoryResponse>{
                override fun onResponse(
                    call: Call<RecentHistoryResponse>,
                    response: Response<RecentHistoryResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    recentSearch?.value = response.body()
                }

                override fun onFailure(call: Call<RecentHistoryResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }

    fun searchDataAdded(context : Context,address:String,latitude : String,longitude:String,
                        radius : String ,mType : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .searchAddressAdd(address,latitude,longitude,radius,mType).enqueue(object : Callback<SearchAddressAddedResponse>{
                override fun onResponse(
                    call: Call<SearchAddressAddedResponse>,
                    response: Response<SearchAddressAddedResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    searchAddressAdd?.value = response.body()
                }

                override fun onFailure(call: Call<SearchAddressAddedResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }


    fun getTimeData(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).getDataForTimeSlot()
            .enqueue(object : Callback<GetTimeResponse>{
                override fun onResponse(
                    call: Call<GetTimeResponse>,
                    response: Response<GetTimeResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    getTime?.value = response.body()
                }

                override fun onFailure(call: Call<GetTimeResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }

    fun updateTimeData(context : Context,court_id : String,date : String,time:String,type:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .updateTime(court_id,date,time,type)
            .enqueue(object : Callback<UpdateTimeFilterResponse>{
                override fun onResponse(
                    call: Call<UpdateTimeFilterResponse>,
                    response: Response<UpdateTimeFilterResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    updateTime?.value = response.body()
                }

                override fun onFailure(call: Call<UpdateTimeFilterResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }
}