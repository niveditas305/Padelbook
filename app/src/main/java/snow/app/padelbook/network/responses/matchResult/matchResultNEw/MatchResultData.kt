package snow.app.padelbook.network.responses.matchResult.matchResultNEw

data class MatchResultData(
    val booking_time: String,
    val clubName: String,
    val club_status: String,
    val court_feature: String,
    val courtname: String,
    val date: String,
    val distance: String,
    val match_type : String,
    val participant_list: List<Participant>,
    val price: String,
    val schedule_id: List<ScheduleId>,
    val team: String,
    val time: String,
    val win_status: String
)