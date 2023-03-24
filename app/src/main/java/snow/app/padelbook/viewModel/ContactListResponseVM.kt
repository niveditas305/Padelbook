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
import snow.app.padelbook.network.responses.contactListResponse.ContactListDataResponse
import snow.app.padelbook.network.sendJsonData.ContactListData
import snow.app.padelbook.network.sendJsonData.ContactListSend

class ContactListResponseVM : BaseObservable() {
    var contactList: MutableLiveData<ContactListDataResponse>? = null
    var addContactList : MutableLiveData<ContactListDataResponse> ?= null
    var userFailure: MutableLiveData<String>? = null
    var contactListData : MutableLiveData<ContactListDataResponse>?= null
    init {
        contactList = MutableLiveData()
        contactListData = MutableLiveData()
        addContactList = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun contactListData(context : Context,contactlist : ContactListData){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).postOrder(contactlist).
        enqueue(object : Callback<ContactListDataResponse> {
            override fun onResponse(
                call: Call<ContactListDataResponse>,
                response: Response<ContactListDataResponse>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                contactListData?.value = response.body()
            }

            override fun onFailure(call: Call<ContactListDataResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
            }

        })
    }

    fun contactListDisplay(context : Context,title: String, status: String,page: String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).contactListDisplay(title,status,page)
            .enqueue(object : Callback<ContactListDataResponse>{
                override fun onResponse(
                    call: Call<ContactListDataResponse>,
                    response: Response<ContactListDataResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    addContactList?.value = response.body()
                }

                override fun onFailure(call: Call<ContactListDataResponse>, t: Throwable) {
                    dismissProgress()
                }

            })
    }
}