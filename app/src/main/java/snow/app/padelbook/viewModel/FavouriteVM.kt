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
import snow.app.padelbook.network.responses.favourite.FavouriteResponse

class FavouriteVM : BaseObservable(), Callback<FavouriteResponse> {
    var favList : MutableLiveData<FavouriteResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        favList = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun favouriteData(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).favouriteList().enqueue(this)

    }

    override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
        dismissProgress()
        Log.d("OnResponse",response.message())
        favList?.value = response.body()
    }

    override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {
        dismissProgress()
        Log.d("OnFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }
}