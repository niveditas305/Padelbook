package snow.app.padelbook.network.responses.matchList

data class Participant(
    val customer_id: String,
    val id: String,
    val image_file: String,
    val name: String,
    val player_key: String,
    val request_status: String,
    val schedule_id: String,
    val score: String,
    val team: String,
    val user_type: String
)