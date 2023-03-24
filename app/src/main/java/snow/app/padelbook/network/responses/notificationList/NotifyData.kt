package snow.app.padelbook.network.responses.notificationList

data class NotifyData(
    val club_id: Int,
    val club_name: String,
    val court_id: String,
    val description: String,
    val id: String,
    val image_file: String,
    val name: String,
    val notification_type: String,
    val schedule_id: String,
    val title: String,
    val user_id: String,
    val score: String,
    val court_name : String,
    val match_date : String,
    val match_time : String,
    val match_id : String,
   // val match_type : String,
    val booking_id : String,
    val user_type :String,
    val pay_type :  String,
    val date : String,
    val time : String,
    var seen : String,
        var matchInPast: Boolean
)