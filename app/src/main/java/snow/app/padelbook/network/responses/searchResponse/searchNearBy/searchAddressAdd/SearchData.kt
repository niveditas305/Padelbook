package snow.app.padelbook.network.responses.searchResponse.searchNearBy.searchAddressAdd

data class SearchData(
    val address: String,
    val created_at: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val radius: String,
    val updated_at: String,
    val user_id: Int
)