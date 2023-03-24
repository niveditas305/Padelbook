package snow.app.padelbook.network.responses.singleMatchDetail

data class SingleMatchDetailResponse(
    val data: Data?,
    val message: String,
    val status: Boolean,
    val isMatchScoreAdded: Boolean,
    val isCancelAllowed: Boolean,
    val matchInPast: Boolean,
    val playerKeyToShow: Int,
    val isWebsiteMatch: Boolean
)