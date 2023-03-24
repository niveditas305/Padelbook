package snow.app.padelbook.network.responses.clubListNew

data class Matchlist(
    val address: String,
    val booking_type: String,
    val club_details_id: String,
    val club_name: String,
    val court_feature: String,
    val court_id: String,
    val date: String,
    val distance: String,
    val latitude: Double,
    val longitude: Double,
    val end_time: String,
    val match_id: String,
    val participant_list: List<Participant>,
    val schedule_id: Int,
    val start_time: String,
    val status: String,
    val time: String
)