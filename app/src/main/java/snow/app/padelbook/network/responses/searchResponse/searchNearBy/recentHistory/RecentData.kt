package snow.app.padelbook.network.responses.searchResponse.searchNearBy.recentHistory

data class RecentData(
    var address: String,
    val id: Int,
    var latitude: String,
    var longitude: String,
    val radius: Int,
    val user_id: Int
)