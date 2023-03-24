package snow.app.padelbook.network.responses.favourite

data class Data(
    val aminities_id: String,
    val city: String,
    val club_name: String,
    val country: String,
    val court_time: List<CourtTime>,
    val description: String,
    val id: String,
    val club_image: String,
    val phone: String,
    val postal_code: String,
    val price_per_person: String,
    val state: String,
    val status: String,
    val street: String,
    val time: String,
    val total_rating: String,
    val is_favourite: Int,
    val address: String,
    val user_id: String,
    val website: String
)