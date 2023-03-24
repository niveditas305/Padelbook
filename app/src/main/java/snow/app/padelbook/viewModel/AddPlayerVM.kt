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
import snow.app.padelbook.network.responses.addPlayer.AddPlayersResponse
import snow.app.padelbook.network.responses.contactListResponse.ContactListDataResponse
import snow.app.padelbook.network.responses.createMatch.CreateMatchResponse
import snow.app.padelbook.network.sendJsonData.ContactListData

class AddPlayerVM : BaseObservable() {
    var addPlayerList : MutableLiveData<AddPlayersResponse> ?= null
    var inviteUserList : MutableLiveData<CreateMatchResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        addPlayerList = MutableLiveData()
        inviteUserList = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun addPlayerData(context : Context,customer_id : String,
                      match_id : String,player_key : String)
    {
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).addPlayer(customer_id,
                     match_id,player_key).enqueue(object : Callback<AddPlayersResponse> {
            override fun onResponse(
                call: Call<AddPlayersResponse>,
                response: Response<AddPlayersResponse>
            ) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                addPlayerList?.value = response.body()
            }

            override fun onFailure(call: Call<AddPlayersResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
            }
        })

    }

    fun inviteUserData(context : Context,phone : String )
    {
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).inviteUser(phone ).enqueue(object : Callback<CreateMatchResponse> {
            override fun onResponse(
                call: Call<CreateMatchResponse>,
                response: Response<CreateMatchResponse>
            ) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                inviteUserList?.value = response.body()
            }

            override fun onFailure(call: Call<CreateMatchResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
            }
        })

    }

}