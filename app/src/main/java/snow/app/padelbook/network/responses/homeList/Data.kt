package snow.app.padelbook.network.responses.homeList

data class Data(
    val address: String,
    val city: String,
    val club_image: String,
    val club_name: String,
    val country: String,
    val description: String,
    val id: String,
    val is_favourite: Int,
    val phone: String,
    val postal_code: String,
    val state: String,
    val status: Int,
    val street: Any,
    val user_id: Int,
    val website: String
)