package snow.app.padelbook.network.responses.bookingCancelByOwner

data class Data(
    val bookiing_status: Int,
    val booking_type: Int,
    val category_id: Int,
    val club_id: Int,
    val court_id: Int,
    val created_at: String,
    val created_by: Int,
    val date: String,
    val id: Int,
    val match_id: Int,
    val payment_type: Int,
    val schedule_id: Int,
    val status: Int,
    val time: String,
    val type: Int,
    val updated_at: String
)