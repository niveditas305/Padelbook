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
import snow.app.padelbook.network.responses.cardList.CardListResponse
import snow.app.padelbook.network.responses.cardlistnew.CardsListResponseNew
import snow.app.padelbook.network.responses.clubpassesistresponse.ClubPassResponse
import snow.app.padelbook.network.responses.createMatch.CreateMatchResponse
import snow.app.padelbook.network.responses.homeList.HomeDataResponse

class CardsVM : BaseObservable() {
    var getCardsRes : MutableLiveData<CardsListResponseNew> ?= null
    var addCardRes : MutableLiveData<CreateMatchResponse> ?= null
    var deleteCardRes : MutableLiveData<CreateMatchResponse> ?= null
    var paymentRes : MutableLiveData<CreateMatchResponse> ?= null
    var clubPassList : MutableLiveData<ClubPassResponse> ?= null

    init {
        getCardsRes = MutableLiveData()
        addCardRes = MutableLiveData()
        deleteCardRes = MutableLiveData()
        paymentRes = MutableLiveData()
        clubPassList = MutableLiveData()
    }

    fun addCardApi(context : Context,card_number : String,
                   exp_month : String,exp_year : String,cvc : String)
    {
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).addCardApi(card_number,
            exp_month,exp_year,cvc).enqueue(object : Callback<CreateMatchResponse> {
            override fun onResponse(
                call: Call<CreateMatchResponse>,
                response: Response<CreateMatchResponse>
            ) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                addCardRes?.value = response.body()
            }

            override fun onFailure(call: Call<CreateMatchResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
            }
        })

    }

    fun getCardsApi(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).cardListApi()
            .enqueue(object : Callback<CardsListResponseNew>{
                override fun onResponse(
                    call: Call<CardsListResponseNew>,
                    response: Response<CardsListResponseNew>) {
                    dismissProgress()
                   // Log.d("OnResponse",response.message())
                    getCardsRes?.value = response.body()
                }

                override fun onFailure(call: Call<CardsListResponseNew>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }

    fun getClubPassList(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).clubPassList()
            .enqueue(object : Callback<ClubPassResponse>{
                override fun onResponse(
                    call: Call<ClubPassResponse>,
                    response: Response<ClubPassResponse>) {
                    dismissProgress()
                   // Log.d("OnResponse",response.message())
                    clubPassList?.value = response.body()
                }

                override fun onFailure(call: Call<ClubPassResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }
    fun deleteCardApi(context : Context,card_id : String )
    {
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).deleteCardApi(card_id).enqueue(object : Callback<CreateMatchResponse> {
            override fun onResponse(
                call: Call<CreateMatchResponse>,
                response: Response<CreateMatchResponse>
            ) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                deleteCardRes?.value = response.body()
            }

            override fun onFailure(call: Call<CreateMatchResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
            }
        })

    }
    fun paymentApi(context : Context,booking_id : String,stripeToken : String,type : String )
    {
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).addPaymentApi(booking_id,stripeToken,type).enqueue(object : Callback<CreateMatchResponse> {
            override fun onResponse(
                call: Call<CreateMatchResponse>,
                response: Response<CreateMatchResponse>
            ) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                paymentRes?.value = response.body()
            }

            override fun onFailure(call: Call<CreateMatchResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
            }
        })

    }

}