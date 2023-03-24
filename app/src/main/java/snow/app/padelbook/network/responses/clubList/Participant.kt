package snow.app.padelbook.network.responses.clubList

data class Participant(
    val customer_id: String,
    val id: String,
    val image_file: String,
    val name: String,
    val request_status : String,
    val user_type : String,
    val player_key : String,
    val schedule_id: String,
    val score: String
)