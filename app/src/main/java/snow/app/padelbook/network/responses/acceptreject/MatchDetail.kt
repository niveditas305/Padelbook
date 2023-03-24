package snow.app.padelbook.network.responses.acceptreject

data class MatchDetail(
    val add_participant_id: Any,
    val booking_id: Int,
    val booking_type: Int,
    val category_id: Int,
    val club_details_id: Int,
    val court_id: Int,
    val created_at: String,
    val date: String,
    val id: Int,
    val match_type: Int,
    val schedule_id: Int,
    val score: Any,
    val status: Int,
    val type: Int,
    val updated_at: String,
    val user_id: Int
)