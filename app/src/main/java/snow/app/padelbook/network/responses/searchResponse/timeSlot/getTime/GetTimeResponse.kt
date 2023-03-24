package snow.app.padelbook.network.responses.searchResponse.timeSlot.getTime

data class GetTimeResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)