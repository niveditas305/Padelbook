package snow.app.padelbook.network.responses.addPlayer

data class AddPlayerData(
    val created_at: String,
    val customer_id: Int,
    val id: Int,
    val match_id: String,
    val pay_type: Any,
    val player_key: Int,
    val schudele_id: Int,
    val team: Int,
    val updated_at: String,
    val user_type: Int
)