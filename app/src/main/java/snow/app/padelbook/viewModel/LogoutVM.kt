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
import snow.app.padelbook.network.responses.logout.LogoutResponse

class LogoutVM : BaseObservable(), Callback<LogoutResponse> {
    var userLogout : MutableLiveData<LogoutResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        userLogout = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun logoutData(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).logoutData().enqueue(this)
    }

    override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
        dismissProgress()
        Log.d("OnResponse",response.message())
        userLogout?.value = response.body()
    }

    override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
       dismissProgress()
        Log.d("OnFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }

}