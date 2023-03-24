package snow.app.padelbook.network.responses.singleMatchDetail

data class PlayerLisr(
    val customer_id: Int,
    val id: Int,
    val image_file: String,
    val match_id: String,
    val name: String,
    val player_key: String,
    val score: String,
    val request_status : String,
    val team : String,
    val user_type: String
)