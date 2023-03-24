package snow.app.padelbook.network.responses.bookingResponse

data class BookingData(
    val address: String,
    val booking_type: Int,
    val club_details_id: Int,
    val club_name: String,
    val court_feature: String,
    val court_id: Int,
    val date: String,
    val distance: String,
    val end_time: String,
    val match_id: String,
    val match_type : String,
    val participant_list: List<Participant>,
    val schedule_id: Int,
    val start_time: String,
    val status: String,
    val time : String
)