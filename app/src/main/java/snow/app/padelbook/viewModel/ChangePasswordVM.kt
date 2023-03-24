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
import snow.app.padelbook.network.responses.ChangePasswordResponse

class ChangePasswordVM : BaseObservable(), Callback<ChangePasswordResponse> {
    var changePass : MutableLiveData<ChangePasswordResponse>?= null
    var userFailure : MutableLiveData<String>?=null

    init {
        changePass = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun changePassword(context : Context,oldPassword : String,NewPassword : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .changePassword(oldPassword,NewPassword).enqueue(this)
    }

    override fun onResponse(call: Call<ChangePasswordResponse>,
        response: Response<ChangePasswordResponse>) {
        dismissProgress()
        Log.d("OnResponse",response.message())
        changePass?.value = response.body()

    }

    override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
        dismissProgress()
        Log.d("OnFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }
}