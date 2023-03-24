package snow.app.padelbook.network.responses.profileresponse

data class ClubProfile(
    val aminity: List<Aminity>,
    val availability: List<Availability>,
    val club_image: String,
    val description: String? = null,
    val favourite_status: Boolean,
    val latitude: String,
    val longitude: String,
    val phone: String,
    val website: String
)