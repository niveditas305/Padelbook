package snow.app.padelbook.network.responses.acceptreject

data class AcceptRejectData(
    val booking_id: String,
    val created_at: String,
    val created_by: String,
    val id: String,
    val match_detail: MatchDetail,
    val match_id: String,
    val status: String,
    val updated_at: String,
    val user_id: String
)