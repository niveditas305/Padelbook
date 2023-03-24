package snow.app.padelbook.network.responses.matchResult

data class MatchResultResponse(
    val `data`: List<MatchResultData>,
    val message: String,
    val status: Boolean
)