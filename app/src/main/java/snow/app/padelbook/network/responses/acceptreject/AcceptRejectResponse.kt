package snow.app.padelbook.network.responses.acceptreject

data class AcceptRejectResponse(
    val `data`: AcceptRejectData,
    val message: String,
    val status: Boolean
)