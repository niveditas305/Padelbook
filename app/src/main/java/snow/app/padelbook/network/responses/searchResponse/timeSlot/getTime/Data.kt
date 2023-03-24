package snow.app.padelbook.network.responses.searchResponse.timeSlot.getTime

data class Data(
    val date: String,
    val end_time: String,
    val id: Int,
    val court_id: Int,
    val time: String
)