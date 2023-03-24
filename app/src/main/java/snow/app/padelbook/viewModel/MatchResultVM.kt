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
import snow.app.padelbook.network.responses.matchResult.MatchResultResponse
import snow.app.padelbook.network.responses.matchResult.matchResultNEw.MatchResultNew

class MatchResultVM : BaseObservable() {
    var matchResult : MutableLiveData<MatchResultNew>? = null
    var userFailure : MutableLiveData<String>? = null

    init {
        matchResult = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun matchResult(context : Context,result : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).matchResultData(result)
            .enqueue(object : Callback<MatchResultNew>{
                override fun onResponse(
                    call: Call<MatchResultNew>,
                    response: Response<MatchResultNew>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    matchResult?.value = response.body()
                }
                override fun onFailure(call: Call<MatchResultNew>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }
}