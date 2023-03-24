package snow.app.padelbook.network.responses.matchResult.matchResultNEw

data class MatchResultNew(
    val `data`: List<MatchResultData>,
    val message: String,
    val status: Boolean
)