package snow.app.padelbook.network.responses.bookingResponse

data class Participant(
    val customer_id: Int,
    val id: String,
    val image_file: String,
    val name: String,
    val player_key: String,
    val schedule_id: String,
    val score: String,
    val team: String,
    val user_type: String
)