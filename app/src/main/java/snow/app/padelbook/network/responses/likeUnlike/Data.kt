package snow.app.padelbook.network.responses.likeUnlike

data class Data(
    val club_id: String,
    val created_at: String,
    val id: Int,
    val favourite_status: Boolean,
    val updated_at: String,
    val user_id: Int
)