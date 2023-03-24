package snow.app.padelbook.network.responses.matchResult

data class MatchResultData(
    val booking_time: String,
    val clubName: String,
    val courtname: String,
    val date: String,
    val distance: String,
    val court_feature : String,
    val participant_list: List<Participant>,
    val price: String,
    val schedule_id: List<ScheduleId>,
    val time: String
)