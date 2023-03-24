package snow.app.padelbook.network.responses.searchResponse.searchNearBy.searchAddressAdd

data class SearchAddressAddedResponse(
    val `data`: SearchData,
    val message: String,
    val status: Boolean
)