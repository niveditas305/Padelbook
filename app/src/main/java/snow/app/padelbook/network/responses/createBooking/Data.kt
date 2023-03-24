package snow.app.padelbook.network.responses.createBooking

data class Data(
    val category_id: String,
    val club_id: String,
    val court_id: String,
    val created_at: String,
    val created_by: Int,
    val date: String,
    val id : String,
    val match_id : String,
    val pay_type : String,
    val payment_type: String,
    val schedule_id: String,
    val status: Int,
    val time: String,
    val updated_at: String
)