package snow.app.padelbook.network.responses.courtresponse

data class ClubTime(
    val court_id: String,
    val day_name: String,
    val end_time: String,
    val schedule_id: String,
    val booking_allow: Boolean,
    val start_time: String
)