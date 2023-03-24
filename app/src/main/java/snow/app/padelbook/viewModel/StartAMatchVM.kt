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
import snow.app.padelbook.network.responses.startaMatch.StartAMatchResponse

class StartAMatchVM : BaseObservable() {
    var matchList: MutableLiveData<StartAMatchResponse>? = null
    var userFailure: MutableLiveData<String>? = null

    init {
        matchList = MutableLiveData()
        userFailure = MutableLiveData()
    }
    fun startAMatchData(context : Context,filter_type : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).startAMatchList(filter_type).enqueue(object :
            Callback<StartAMatchResponse>{
            override fun onResponse(
                call: Call<StartAMatchResponse>,
                response: Response<StartAMatchResponse>)
            {
                dismissProgress()
                Log.d("OnResponse", response.message())
                matchList?.value = response.body()

            }

            override fun onFailure(call: Call<StartAMatchResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
            }

        })

    }
}