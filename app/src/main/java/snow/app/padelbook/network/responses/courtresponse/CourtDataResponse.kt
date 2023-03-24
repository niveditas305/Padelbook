package snow.app.padelbook.network.responses.courtresponse

data class CourtDataResponse(
    val club_time: List<ClubTime>,
    val `data`: List<CourtData>,
    val message: String,
    val status: Boolean,
    val isBookingAllowed: Boolean
)