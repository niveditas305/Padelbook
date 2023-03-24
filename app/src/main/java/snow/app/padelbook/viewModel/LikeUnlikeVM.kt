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
import snow.app.padelbook.network.responses.likeUnlike.LikeUnLikeResponse

class LikeUnlikeVM : BaseObservable(), Callback<LikeUnLikeResponse> {
    var likeUnlike : MutableLiveData<LikeUnLikeResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        likeUnlike = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun likeUnlikeData(context : Context, clubId : String ){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .likeUnlike(clubId).enqueue(this)
    }

    override fun onResponse(call: Call<LikeUnLikeResponse>,
        response: Response<LikeUnLikeResponse>) {
        dismissProgress()
        Log.d("onResponse",response.message())
        likeUnlike?.value = response.body()
    }

    override fun onFailure(call: Call<LikeUnLikeResponse>, t: Throwable) {
        dismissProgress()
        Log.d("onFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }

}