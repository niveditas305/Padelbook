package snow.app.padelbook.network.responses.singleMatchDetail

data class Data(
    val booking_time: String,
    val clubName: String,
    val cancel_policy: String,
    val date : String,
    val user_id : String,
    val club_status: String,
    val court_type: String,
    val courtname: String,
    val bookiing_id : String,
    val bookiing_status : String,
    val court_feature : String,
    val match_type : String,
    val player_Lisr: List<PlayerLisr>,
    val price: String,
    val pay_type : String,
    val payment_type : String,
    val start_time : String,
    val time: String
)