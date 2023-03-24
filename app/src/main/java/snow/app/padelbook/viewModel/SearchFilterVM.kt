package snow.app.padelbook.viewModel

import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snow.app.padelbook.common.Utils.*
import snow.app.padelbook.network.ApiInterface
import snow.app.padelbook.network.WebServiceClient
import snow.app.padelbook.network.responses.filterresponse.FilterDataResponse

class SearchFilterVM : BaseObservable() {
    var searchFilter : MutableLiveData<FilterDataResponse>?= null
    var getSearchFilterData : MutableLiveData<FilterDataResponse>?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        searchFilter = MutableLiveData()
        getSearchFilterData = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun applyFilterData(context : Context, latitude : String, longitude:String, radius : String,
                        sort_type: String, court_feature:String, court_type:String, duration:String,
                        gender : String, level:String,court_id : String, distance : String, type : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).filterData(latitude,longitude,
        radius,sort_type,court_feature,court_type,duration,gender,level,court_id,distance,type).enqueue(object :
            Callback<FilterDataResponse> {
            override fun onResponse(
                call: Call<FilterDataResponse>,
                response: Response<FilterDataResponse>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                searchFilter?.value = response.body()
            }

            override fun onFailure(call: Call<FilterDataResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value = "Something went wrong"
            }

        })

    }

    fun getFilterData(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).getFilterData().enqueue(object :
            Callback<FilterDataResponse>{
            override fun onResponse(
                call: Call<FilterDataResponse>,
                response: Response<FilterDataResponse>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                getSearchFilterData?.value = response.body()
            }

            override fun onFailure(call: Call<FilterDataResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value = "Something went wrong"
            }

        })

    }

}