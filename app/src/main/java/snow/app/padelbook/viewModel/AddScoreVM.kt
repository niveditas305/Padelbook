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
import snow.app.padelbook.network.responses.score.addScore.AddScoreResponse
import snow.app.padelbook.network.responses.score.singleScoreDetailNew.SingleScoreDetailResponseNew

class AddScoreVM : BaseObservable() {
     var singleScoreDetail: MutableLiveData<SingleScoreDetailResponseNew>? = null
     var addScore : MutableLiveData<AddScoreResponse> ?= null
     var userFailure: MutableLiveData<String>? = null

    init {
        singleScoreDetail = MutableLiveData()
        addScore = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun singleScoreDetailData(context : Context, match_id : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).singleScoreDetail(match_id)
            .enqueue(object : Callback<SingleScoreDetailResponseNew>{
                override fun onResponse(
                    call: Call<SingleScoreDetailResponseNew>,
                    response: Response<SingleScoreDetailResponseNew>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    singleScoreDetail?.value = response.body()
                }

                override fun onFailure(call: Call<SingleScoreDetailResponseNew>, t: Throwable) {
                    dismissProgress()
                    Log.d("onFailure",t.localizedMessage)
                }

            })

    }


    fun addScoreData(context : Context,roundOne : String,roundTwo : String,
                     roundThree : String, match_id : String)
    {
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .addScore(roundOne,roundTwo,roundThree,match_id).enqueue(object : Callback<AddScoreResponse>{
                override fun onResponse(
                    call: Call<AddScoreResponse>,
                    response: Response<AddScoreResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    addScore?.value = response.body()
                }

                override fun onFailure(call: Call<AddScoreResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure", t.localizedMessage)
                }
            })
    }
}