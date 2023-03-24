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
import snow.app.padelbook.network.responses.notificationList.notificationCount.NotificationCountResponse

class NotifyCountReadVM : BaseObservable() {
    var notifyCount : MutableLiveData<NotificationCountResponse> ?= null
    var notifyRead : MutableLiveData<NotificationCountResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        notifyCount = MutableLiveData()
        notifyRead = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun countNotify(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).countNotification()
            .enqueue(object : Callback<NotificationCountResponse>{
                override fun onResponse(
                    call: Call<NotificationCountResponse>,
                    response: Response<NotificationCountResponse>
                ) {
                    dismissProgress()
                    notifyCount?.value = response.body()
                    Log.d("OnResponse",response.message())

                }

                override fun onFailure(call: Call<NotificationCountResponse>, t: Throwable) {
                    dismissProgress()
                }

            })
    }

    fun readNotification(context : Context,notification_id : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .readNotification(notification_id).enqueue(object : Callback<NotificationCountResponse>{
                override fun onResponse(
                    call: Call<NotificationCountResponse>,
                    response: Response<NotificationCountResponse>
                ) {
                    dismissProgress()
                    notifyRead?.value = response.body()
                    Log.d("OnFailure",response.message())
                }

                override fun onFailure(call: Call<NotificationCountResponse>, t: Throwable) {
                    dismissProgress()
                }

            })
    }

}