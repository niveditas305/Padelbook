package snow.app.padelbook.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import snow.app.padelbook.network.responses.ChangePasswordResponse
import snow.app.padelbook.network.responses.clubList.ClubListResponse
import snow.app.padelbook.network.responses.courtresponse.CourtDataResponse
import snow.app.padelbook.network.responses.createBooking.CreateBookingResponse
import snow.app.padelbook.network.responses.favourite.FavouriteResponse
import snow.app.padelbook.network.responses.filterresponse.FilterDataResponse
import snow.app.padelbook.network.responses.likeUnlike.LikeUnLikeResponse
import snow.app.padelbook.network.responses.login.LoginResponse
import snow.app.padelbook.network.responses.logout.LogoutResponse
import snow.app.padelbook.network.responses.profileresponse.ProfileResponse
import snow.app.padelbook.network.responses.questList.QuesionListResponse
import snow.app.padelbook.network.responses.register.RegisterResponse
import snow.app.padelbook.network.responses.searchResponse.searchCourt.SearchCourtResponse
import snow.app.padelbook.network.responses.searchResponse.searchNearBy.recentHistory.RecentHistoryResponse
import snow.app.padelbook.network.responses.searchResponse.searchNearBy.searchAddressAdd.SearchAddressAddedResponse
import snow.app.padelbook.network.responses.searchResponse.timeSlot.getTime.GetTimeResponse
import snow.app.padelbook.network.responses.searchResponse.timeSlot.updateTimeFilter.UpdateTimeFilterResponse
import snow.app.padelbook.network.responses.selectAnswer.SelectAnswerResponse
import snow.app.padelbook.network.responses.selectlevel.LevlQuestListResponse
import snow.app.padelbook.network.responses.selectlevel.signupTwo.SignUpTwoNewResponse
import retrofit2.http.Body
import retrofit2.http.POST
import snow.app.padelbook.network.responses.LocationRsponse
import snow.app.padelbook.network.responses.acceptreject.AcceptRejectResponse
import snow.app.padelbook.network.responses.addPlayer.AddPlayersResponse
import snow.app.padelbook.network.responses.bookingCancelByOwner.BookingCancelByOwnerResponse
import snow.app.padelbook.network.responses.bookingResponse.BookingListResponse
import snow.app.padelbook.network.responses.cardList.CardListResponse
import snow.app.padelbook.network.responses.cardlistnew.CardsListResponseNew
import snow.app.padelbook.network.responses.clubListNew.ClubListNewResponse
import snow.app.padelbook.network.responses.clubpassesistresponse.ClubPassResponse
import snow.app.padelbook.network.responses.contactListResponse.ContactListDataResponse
import snow.app.padelbook.network.responses.createMatch.CreateMatchResponse
import snow.app.padelbook.network.responses.forgototpverified.ForgotOtpVerified
import snow.app.padelbook.network.responses.homeList.HomeDataResponse
import snow.app.padelbook.network.responses.matchList.MatchListResponse
import snow.app.padelbook.network.responses.matchResult.MatchResultResponse
import snow.app.padelbook.network.responses.matchResult.matchResultNEw.MatchResultNew
import snow.app.padelbook.network.responses.notificationList.NotificationResponse
import snow.app.padelbook.network.responses.notificationList.notificationCount.NotificationCountResponse
import snow.app.padelbook.network.responses.resendotp.ResendOtpModel
import snow.app.padelbook.network.responses.score.addScore.AddScoreResponse
import snow.app.padelbook.network.responses.score.singleScoreDetailNew.SingleScoreDetailResponseNew
import snow.app.padelbook.network.responses.singleMatchDetail.SingleMatchDetailResponse
import snow.app.padelbook.network.responses.startaMatch.StartAMatchResponse
import snow.app.padelbook.network.sendJsonData.ContactListData


interface ApiInterface {

    @FormUrlEncoded
    @POST("signup")                                          // Register api
    fun register(
        @Field("email") email :String,
        @Field("password") password : String,
        @Field("password_confirmation") password_confirmation :String,
        @Field("phone") phone : String,
        @Field("country_code") country_code : String,
        @Field("device_type") device_type :String,
        @Field("name") name :String,
        @Field("language_type") language_type :String,
        @Field("device_token") device_token :String ) : Call<LoginResponse>

    @FormUrlEncoded                                              // verifyOtp api
    @POST("verifyOtp")
    fun otpVerify(
        @Field("id") email :String,
        @Field("otp") password : String
    ):Call<LoginResponse>


    @FormUrlEncoded                                              // verifyOtp api
    @POST("verifyForgetOtp")
    fun verifyForgetOtp(
        @Field("id") id :String,
        @Field("otp") otp : String,
        @Field("type") type : String
    ):Call<ForgotOtpVerified>

    @FormUrlEncoded                                              // verifyOtp api
    @POST("resetPassword")
    fun resetPassword(
        @Field("user_id") user_id :String,
        @Field("password") password : String
    ):Call<ForgotOtpVerified>

    @FormUrlEncoded
    @POST("login")                                         // Login api
    fun loginData(
        @Field("email") email :String,
        @Field("password") password : String,
        @Field("device_type") device_type :String,
        @Field("device_token") device_token :String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("resendOtp")                                         // Login api
    fun resendOtp(
        @Field("id") id  :String
     ):Call<ResendOtpModel>

    @FormUrlEncoded
    @POST("resendOtp")
    fun resendOtpTwo(
         @Field("id") id : String,
        @Field("type") type :String  //type=1 mobile , type=2 email
    ):Call<ResendOtpModel>

    @FormUrlEncoded
    @POST("loginWithFacebook")
    fun socialLogin(
        @Field("facebook_id") facebook_id : String,
        @Field("email") email : String,
        @Field("device_token") device_token : String,
        @Field("device_type") device_type : String
    ) : Call<LoginResponse>


    @GET("favouriteList")                                 // favourite List
    fun favouriteList() : Call<FavouriteResponse>

    @GET("quizLists")                                    //  level list
    fun selectLevelList() : Call<LevlQuestListResponse>

    @FormUrlEncoded
    @POST("signupStep2")                                // Select level Api
    fun selectLevel(
        @Field("quiz_type_id") quiz_type_id : String
    ) : Call<SignUpTwoNewResponse>

    @GET("logout")                                     // logout API
    fun logoutData() : Call<LogoutResponse>

    @GET("userQuizAttempts")                           // QuestionList Api
    fun QuestList(
        @Query("quiz_type_id") quiz_type_id : String
    ) : Call<QuesionListResponse>

    @FormUrlEncoded
    @POST("searchClubList")                            // club list api
    fun getClubList(
        @Field("filter_type") filter_type : String

    ) : Call<ClubListNewResponse>

    @FormUrlEncoded
    @POST("clubLikeDislike")                         // likeUnlike api
    fun likeUnlike (
        @Field("club_id") club_id : String
    ) : Call<LikeUnLikeResponse>

    @FormUrlEncoded
    @POST("changePassword")                         // change password api
    fun changePassword(
        @Field("old_password") old_password : String,
        @Field("new_password") new_password : String
    ) : Call<ChangePasswordResponse>

    @GET("getProfile")                              // Get Profile data api
    fun getProfile() : Call<LoginResponse>

    @Multipart
    @POST("updateProfile")                          // Update Profile data api
    fun updateProfile(
        @Part("name") name : RequestBody,
        @Part("gender") gender : RequestBody,
        @Part("password") password : RequestBody,
        @Part("date_of_birth") date_of_birth : RequestBody,
        @Part("address") address : RequestBody,
        @Part("latitude") latitude : RequestBody,
        @Part("longitude") longitude : RequestBody,
        @Part image_file : MultipartBody.Part ) : Call<LoginResponse>

    @Multipart
    @POST("updateProfile")                          // Update Profile data api
    fun updateProfileImageOnly(
        @Part image_file : MultipartBody.Part ) : Call<LoginResponse>

    @FormUrlEncoded
    @POST("changeLocation")                          // change location status api
    fun automaticLocationStatus(  @Field("latitude") latitude : String,
                                  @Field("longitude") longitude : String,
                                  @Field("is_location") is_location : String,
                                  @Field("address") address : String,
    ) : Call<LocationRsponse>

    @FormUrlEncoded
    @POST("selectAnswer")                          // (quiz process) select answer
    fun selectAnswerData(
        @Field("option_id") option_id : String,
        @Field("question_id") question_id : String,
        @Field("quiz_type_id") quiz_type_id : String
    ) : Call<SelectAnswerResponse>

    @FormUrlEncoded                                    // profile response
    @POST("singleClubDetail")
    fun profileDetail(
        @Field("club_id") club_id : String
    ) : Call<ProfileResponse>

    @FormUrlEncoded                                    // court list api
    @POST("allCourtList")
    fun courtData(
        @Field("club_id") club_id : String,
        @Field("date") date : String,
        @Field("time") time : String
    ) : Call<CourtDataResponse>

    @GET("changeLanguage")                       // change language
    fun changeLanguage() : Call<LoginResponse>

    @FormUrlEncoded                                    // get court list for filter search
    @POST("getAllCourtList")
    fun searchCourt(
        @Field("title") title : String
     ) : Call<SearchCourtResponse>


    @GET("recentHistory")                        //  recent history
    fun getRecentHistory() : Call<RecentHistoryResponse>

    @FormUrlEncoded
    @POST("searchAddress")                             // search address added
    fun searchAddressAdd(
        @Field("address") address : String,
        @Field("latitude") latitude : String,
        @Field("longitude") longitude : String,
        @Field("radius") radius : String,
        @Field("type") type : String
    ) : Call<SearchAddressAddedResponse>

    @GET("getSaveDateDetail")                           // get time
    fun getDataForTimeSlot() : Call<GetTimeResponse>

    @FormUrlEncoded                                           // update time filter
    @POST("saveFilter")
    fun updateTime(
        @Field("court_id") court_id : String,
        @Field("date") date : String,
        @Field("time") time : String,
        @Field("type") type : String,
    ):Call<UpdateTimeFilterResponse>

    @FormUrlEncoded                                                 // 4th filter
    @POST("searchFilter")
    fun filterData(
        @Field("latitude") latitude : String,
        @Field("longitude") longitude : String,
        @Field("radius") radius : String,
        @Field("sort_type") sort_type : String,
        @Field("court_feature") court_feature : String,
        @Field("court_type") court_type : String,
        @Field("duration") duration : String,
        @Field("gender") gender : String,
        @Field("level") level : String,
        @Field("court_id") court_id : String,
        @Field("distance") distance : String,
        @Field("type") type : String,
    ) : Call<FilterDataResponse>

    @GET("searchFilterDetail")                               // filter get detail data
    fun getFilterData() : Call<FilterDataResponse>


    @FormUrlEncoded
    @POST("createBooking")
    fun createBooking(
        @Field("club_id") club_id : String,
        @Field("court_id") court_id : String,
        @Field("date") date : String,
        @Field("time") time : String,
        @Field("category_id") category_id : String,
        @Field("payment_type") payment_type : String,
        @Field("schedule_id") schedule_id : String,
        @Field("pay_type") pay_type : String,
        @Field("match_type") match_type : String,
        @Field("selected_duration") selected_duration : String,
        @Field("price") price : String,
        @Field("paid_price") paid_price : String,
        @Field("stripeToken") stripeToken : String,
        @Field("type") type : String,
        @Field("club_pass") club_pass : String,
        @Field("club_pass_id") club_pass_id : String,
    ) : Call<CreateBookingResponse>

    @POST("addContact")                                          // add contact list
    fun postOrder(@Body jsonArray : ContactListData)
    : Call<ContactListDataResponse>

    @FormUrlEncoded
    @POST("homeList")                                            // home data
    fun homeData( @Field("page") page : String) : Call<HomeDataResponse>

    @GET("cardList")                                            // home data
    fun cardListApi() : Call<CardsListResponseNew>

    @GET("userClubPasses")                                            // home data
    fun clubPassList() : Call<ClubPassResponse>

    @FormUrlEncoded
    @POST("singleMatchDetail")                                  // single match detail
    fun singleMatchData(
        @Field("match_id") match_id : String
    ) : Call<SingleMatchDetailResponse>

    @FormUrlEncoded                                                  // create match booking
    @POST("createMatchBooking")
    fun createMatchBooked(
        @Field("payment_type") pay_type : String,
        @Field("player_key") player_key : String,
        @Field("match_id") match_id : String
    ) : Call<CreateMatchResponse>

    @FormUrlEncoded                                                  // create match booking
    @POST("userInvite")
    fun inviteUser(
        @Field("phone") phone : String
    ) : Call<CreateMatchResponse>

    @FormUrlEncoded                                                  // create match booking
    @POST("addCard")
    fun addCardApi(
        @Field("card_number") card_number : String,
        @Field("exp_month") exp_month : String,
        @Field("exp_year") exp_year : String,
        @Field("cvc") cvc : String
    ) : Call<CreateMatchResponse>

    @FormUrlEncoded                                                  // create match booking
    @POST("cardDelete")
    fun deleteCardApi(
        @Field("card_id") card_id : String
    ) : Call<CreateMatchResponse>

    @FormUrlEncoded                                                  // create match booking
    @POST("addPayment")
    fun addPaymentApi(
        @Field("booking_id") booking_id : String,
        @Field("stripeToken") stripeToken : String,
        @Field("type") type : String
    ) : Call<CreateMatchResponse>

    @FormUrlEncoded
    @POST("matchResult")                                        // match Results api
    fun matchResultData(
        @Field("result") result : String
    ) : Call<MatchResultNew>

    @FormUrlEncoded
    @POST("bookingList")                                        // booking list api
    fun allBookingList(
        @Field("status") status : String,
        @Field("page") page : String
    ) : Call<BookingListResponse>

//    @FormUrlEncoded                                                // booking detail for owner
//    @POST("bookingDetail")
//    fun bookingDetailData(
//        @Field("booking_id") booking_id : String
//    ) : Call<SingleMatchDetailResponse>

    @FormUrlEncoded
    @POST("bookingCancelByOwner")                             // booking cancel by owner
    fun bookingCancelByOwner(
        @Field("booking_id") booking_id : String
    ) : Call<BookingCancelByOwnerResponse>

    @FormUrlEncoded
    @POST("bookingCancelByUser")
    fun bookingCancelByUser(
        @Field("booking_id") booking_id : String
    ) : Call<BookingCancelByOwnerResponse>

    @FormUrlEncoded
    @POST("singleScoreDetail")                                 // single score detail api
    fun singleScoreDetail(
        @Field("match_id") match_id : String
    ) : Call<SingleScoreDetailResponseNew>

//    @FormUrlEncoded
//    @POST("addScore")                                          // add score api
//    fun addScore(
//        @Field("round1[0]") roundOne : ArrayList<String>,
//        @Field("round2[0]") roundTwo : ArrayList<String>,
//        @Field("round3[0]") roundThree : ArrayList<String>,
//        @Field("round1[1]") roundFour : ArrayList<String>,
//        @Field("round2[1]") roundFive : ArrayList<String>,
//        @Field("round3[1]") roundSix : ArrayList<String>,
//        @Field("match_id") match_id : String
//    ) : Call<AddScoreResponse>

    @FormUrlEncoded
    @POST("addScore")                                          // add score api
    fun addScore(
        @Field("round1") roundOne : String,
        @Field("round2") roundTwo : String,
        @Field("round3") roundThree : String,
        @Field("match_id") match_id : String
    ) : Call<AddScoreResponse>

    @GET("notificationList")                                    // notification list
    fun notificationList() : Call<NotificationResponse>

    @FormUrlEncoded
    @POST("acceptReject")                                       // accept-reject request
    fun acceptRejectRequest(
        @Field("booking_id") booking_id : String,
        @Field("match_id") match_id : String,
        @Field("status") status : String,
        @Field("payment_type") payment_type: String
    ) : Call<AcceptRejectResponse>

    @FormUrlEncoded                                                   // add player api
    @POST("addPlayer")
    fun addPlayer(
        @Field("customer_id") customer_id : String,
        @Field("match_id") match_id : String,
        @Field("player_key") player_key : String
    ) : Call<AddPlayersResponse>


    @FormUrlEncoded
    @POST("singleClubMatchList")                              // match list tab data (court,match,profile tab screens)
    fun matchListData(
        @Field("club_id") club_id : String,
        @Field("date") date : String
    ) : Call<MatchListResponse>

    @FormUrlEncoded
    @POST("acceptRejectScore")                            // accept/reject score
    fun acceptRejectScore(
        @Field("match_id") match_id : String,
        @Field("status") status : String
    )  : Call<AcceptRejectResponse>

    @FormUrlEncoded
    @POST("matchSearchList")                                  // start a match api
    fun startAMatchList(
        @Field("filter_type") filter_type : String
    ) : Call<StartAMatchResponse>

    @GET("countNotification")                                // count notification
    fun countNotification() : Call<NotificationCountResponse>

    @FormUrlEncoded                                                // read Notification
    @POST("readNotification")
    fun readNotification(
        @Field("notification_id")notification_id : String)
    : Call<NotificationCountResponse>

    @FormUrlEncoded
    @POST("contactList")
    fun contactListDisplay(
        @Field("title")title : String,
    @Field("status")status : String,
    @Field("page")page : String


    ) : Call<ContactListDataResponse>          // contact list display



}