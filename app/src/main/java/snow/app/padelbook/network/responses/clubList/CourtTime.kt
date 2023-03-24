package snow.app.padelbook.network.responses.clubList

data class CourtTime(
    val court_id: String,
    val day_name: String,
    val schedule_id: String,
    val start_time: String
)