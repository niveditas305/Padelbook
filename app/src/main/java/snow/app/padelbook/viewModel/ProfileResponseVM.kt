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
import snow.app.padelbook.network.responses.profileresponse.ProfileResponse

class ProfileResponseVM : BaseObservable(), Callback<ProfileResponse> {
    var userProfile : MutableLiveData<ProfileResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        userProfile = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun profileData(context : Context, club_id : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .profileDetail(club_id).enqueue(this)
    }

    override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
        dismissProgress()
        Log.d("OnResponse",response.message())
        userProfile?.value = response.body()
    }

    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
       dismissProgress()
        Log.d("OnFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }

}