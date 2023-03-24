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
import snow.app.padelbook.network.responses.courtresponse.CourtDataResponse
import snow.app.padelbook.network.responses.matchList.MatchListResponse

class CourtListVM : BaseObservable() {
    var userCourt : MutableLiveData<CourtDataResponse> ?= null
    var userMatchList : MutableLiveData<MatchListResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        userCourt = MutableLiveData()
        userMatchList = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun courtListData(context : Context,club_id:String,date:String,time:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .courtData(club_id,date,time).enqueue(object : Callback<CourtDataResponse>{
                override fun onResponse(
                    call: Call<CourtDataResponse>,
                    response: Response<CourtDataResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    userCourt?.value = response.body()
                }

                override fun onFailure(call: Call<CourtDataResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }

    fun matchListData(context : Context,club_id : String,date : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).matchListData(club_id,date)
            .enqueue(object : Callback<MatchListResponse>{
                override fun onResponse(
                    call: Call<MatchListResponse>,
                    response: Response<MatchListResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    userMatchList?.value = response.body()
                }

                override fun onFailure(call: Call<MatchListResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }

}