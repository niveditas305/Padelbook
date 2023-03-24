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
import snow.app.padelbook.network.responses.clubList.ClubListResponse
import snow.app.padelbook.network.responses.clubListNew.ClubListNewResponse

class ClubListVM : BaseObservable(), Callback<ClubListNewResponse> {
    var clubList : MutableLiveData<ClubListNewResponse> ?= null
    var userFailure : MutableLiveData<String> ?=null

    init {
        clubList = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun clubListData(context : Context,filter_type : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).getClubList(filter_type).enqueue(this)
    }

    override fun onResponse(call: Call<ClubListNewResponse>, response: Response<ClubListNewResponse>) {
        dismissProgress()
        Log.d("OnResponse",response.message())
        clubList?.value = response.body()
    }

    override fun onFailure(call: Call<ClubListNewResponse>, t: Throwable) {
        dismissProgress()
        Log.d("OnFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }

}