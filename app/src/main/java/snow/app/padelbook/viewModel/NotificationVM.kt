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
import snow.app.padelbook.network.responses.acceptreject.AcceptRejectResponse
import snow.app.padelbook.network.responses.notificationList.NotificationResponse

class NotificationVM : BaseObservable(){
    var notificationList : MutableLiveData<NotificationResponse> ?= null
    var acceptRejectList : MutableLiveData<AcceptRejectResponse> ?= null
    var acceptRejectScore : MutableLiveData<AcceptRejectResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        notificationList = MutableLiveData()
        acceptRejectList = MutableLiveData()
        acceptRejectScore = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun notificationListData(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .notificationList().enqueue(object : Callback<NotificationResponse> {
                override fun onResponse(
                    call: Call<NotificationResponse>,
                    response: Response<NotificationResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    notificationList?.value = response.body()
                }

                override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }

    fun acceptRejectResponse(context : Context,booking_id : String,match_id:String,
                             status:String, payment_type : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .acceptRejectRequest(booking_id, match_id,status,payment_type).enqueue(object : Callback<AcceptRejectResponse>{
                override fun onResponse(
                    call: Call<AcceptRejectResponse>,
                    response: Response<AcceptRejectResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    acceptRejectList?.value = response.body()
                }

                override fun onFailure(call: Call<AcceptRejectResponse>, t: Throwable) {
                   dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })

    }

    fun acceptRejectScore(context : Context,match_id:String, status:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .acceptRejectScore(match_id,status).enqueue(object : Callback<AcceptRejectResponse>{
                override fun onResponse(
                    call: Call<AcceptRejectResponse>,
                    response: Response<AcceptRejectResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    acceptRejectScore?.value = response.body()
                }

                override fun onFailure(call: Call<AcceptRejectResponse>, t: Throwable) {
                   dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })

    }



}