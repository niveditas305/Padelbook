package snow.app.padelbook.network.responses.homeList

data class Match(
    val address: String,
    val booking_type: String,
    val club_details_id: String,
    val court_id: String,
    val date: String,
    val distance: String,
    val match_id: String,
    val court_feature : String,
    val participant_list: List<Participant>,
    val schedule_id: String,
    val status : String,
    val club_name : String,
    val time : String,
    val end_time : String,
    val start_time : String
)