package snow.app.padelbook.network.responses.profileresponse

data class ProfileResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)