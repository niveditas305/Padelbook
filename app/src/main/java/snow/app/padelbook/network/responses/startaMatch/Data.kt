package snow.app.padelbook.network.responses.startaMatch

import snow.app.padelbook.network.responses.clubList.CourtTime


data class Data(
    val address: String,
    val city: String,
    val club_id: String,
    val club_image: String,
    val club_name: String,
    val latitude : Double,
    val longitude : Double,
    val country: String,
    val court_time: List<CourtTime>,
    val description: String,
    val favourite_status: Boolean,
    val phone: String,
    val postal_code: String,
    val price_per_person: String,
    val rating: Int,
    val state: String,
    val status: Int,
    val street: String,
    val total_rating: Int,
    val user_id: String,
    val website: String
)