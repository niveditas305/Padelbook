package snow.app.padelbook.network.responses.homeList

data class Participant(
    val customer_id: Int,
    val id: Int,
    val image_file: String,
    val name: String,
    val schedule_id: Int,
    val team : String,
     val request_status : String,
    val user_type : String,
    val player_key : String,
    val score: String
)