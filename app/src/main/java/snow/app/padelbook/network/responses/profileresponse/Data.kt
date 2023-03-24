package snow.app.padelbook.network.responses.profileresponse

data class Data(
    val address: String,
    val city: String,
    val club_image: String,
    val club_name: String,
    val club_profile: ClubProfile,
    val country: String,
    val cover_image: List<CoverImage>,
    val description: String,
    val id: String,
    val phone: String,
    val postal_code: String,
    val state: String,
    val status: Int,
    val street: String,
    val user_id: String,
    val website: String,
    val pending_public_match: Boolean

)