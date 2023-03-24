package snow.app.padelbook.network.responses.searchResponse.timeSlot.updateTimeFilter

data class Data(
    val club_id: Any,
    val court_id: String,
    val created_at: String,
    val date: String,
    val id: Int,
    val time: String,
    val updated_at: String,
    val user_id: Int
)