package snow.app.padelbook.network.responses.searchResponse.searchNearBy.recentHistory

data class RecentHistoryResponse(
    val `data`: List<RecentData>,
    val message: String,
    val status: Boolean
)