package snow.app.padelbook.viewModel

import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import snow.app.padelbook.common.Utils.*
import snow.app.padelbook.network.ApiInterface
import snow.app.padelbook.network.WebServiceClient
import snow.app.padelbook.network.responses.bookingCancelByOwner.BookingCancelByOwnerResponse
import snow.app.padelbook.network.responses.bookingResponse.BookingListResponse
import snow.app.padelbook.network.responses.createBooking.CreateBookingResponse
import snow.app.padelbook.network.responses.createMatch.CreateMatchResponse
import snow.app.padelbook.network.responses.singleMatchDetail.SingleMatchDetailResponse

class CreateBookingVM : BaseObservable() {
    var createBooking : MutableLiveData<CreateBookingResponse> ?= null
    var singleMatchDetail : MutableLiveData<SingleMatchDetailResponse> ?= null
    var createMatch : MutableLiveData<CreateMatchResponse> ?= null
    var bookingList : MutableLiveData<BookingListResponse> ?= null
    var bookingDetail : MutableLiveData<SingleMatchDetailResponse> ?= null
    var bookingCancelOwner : MutableLiveData<BookingCancelByOwnerResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        createBooking = MutableLiveData()
        singleMatchDetail = MutableLiveData()
        createMatch = MutableLiveData()
        bookingList = MutableLiveData()
        bookingDetail = MutableLiveData()
        bookingCancelOwner = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun createBookingData(context : Context, club_id : String,court_id:String,date:String,time:String,
                          category_id : String,payment_type : String,schedule_id:String,
                          pay_type : String, match_type : String, selected_duration : String, price : String,
                          paid_price : String, stripeToken : String, type : String, club_pass : String, club_pass_id : String){
        // create booking for host when call from court fragment
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).createBooking(club_id,court_id,
        date,time,category_id,payment_type,schedule_id,pay_type,match_type,selected_duration,price,paid_price,
        stripeToken,type,club_pass,club_pass_id).enqueue(object : Callback<CreateBookingResponse>{
            override fun onResponse(call: Call<CreateBookingResponse>,
                response: Response<CreateBookingResponse>) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                createBooking?.value = response.body()
            }
            override fun onFailure(call: Call<CreateBookingResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnFailure",t.localizedMessage)
                userFailure?.value = "Something went wrong"
            }

        })
    }

    fun singleMatchDetailData(context : Context, matchId : String){

        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).singleMatchData(matchId)
            .enqueue(object : Callback<SingleMatchDetailResponse>{
                override fun onResponse(
                    call: Call<SingleMatchDetailResponse>,
                    response: Response<SingleMatchDetailResponse> ) {
                    dismissProgress()
                    singleMatchDetail?.value = response.body()
                    Log.d("OnResponse",response.message())

                }

                override fun onFailure(call: Call<SingleMatchDetailResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }

    fun createMatchData(context : Context,pay_type : String,player_key : String,match_id : String){
        // create match booking for users
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).createMatchBooked(pay_type,
            player_key,match_id).enqueue(object : Callback<CreateMatchResponse>{
            override fun onResponse(
                call: Call<CreateMatchResponse>,
                response: Response<CreateMatchResponse> ) {
                dismissProgress()
                Log.d("OnResponse",response.message())
                createMatch?.value = response.body()
            }

            override fun onFailure(call: Call<CreateMatchResponse>, t: Throwable) {
                dismissProgress()
                Log.d("OnResponse",t.localizedMessage)
            }

        })

    }

    fun bookingListData(context : Context, status : String, page : String){
        // booking list for all, pending, confirmed
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .allBookingList(status,page).enqueue(object : Callback<BookingListResponse>{
                override fun onResponse(
                    call: Call<BookingListResponse>,
                    response: Response<BookingListResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    bookingList?.value = response.body()
                }

                override fun onFailure(call: Call<BookingListResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }

    fun bookingCancelByOwnerData(context : Context, booking_id : String){
        // booking Cancel By Owner
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .bookingCancelByOwner(booking_id).enqueue(object : Callback<BookingCancelByOwnerResponse>{
                override fun onResponse(
                    call: Call<BookingCancelByOwnerResponse>,
                    response: Response<BookingCancelByOwnerResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    bookingCancelOwner?.value = response.body()
                }

                override fun onFailure(call: Call<BookingCancelByOwnerResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }

    fun bookingCancelByUserData(context : Context, booking_id : String){
        // booking Cancel By Owner
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .bookingCancelByUser(booking_id).enqueue(object : Callback<BookingCancelByOwnerResponse>{
                override fun onResponse(
                    call: Call<BookingCancelByOwnerResponse>,
                    response: Response<BookingCancelByOwnerResponse>
                ) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    bookingCancelOwner?.value = response.body()
                }

                override fun onFailure(call: Call<BookingCancelByOwnerResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }

            })
    }

}