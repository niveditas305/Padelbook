package snow.app.padelbook.network.responses.favourite

data class CourtTime(
    val club_id: String,
    val court_id: String,
    val court_type: String,
    val description: String,
    val name: String,
    val start_time: String
)