package snow.app.padelbook.network.responses.searchResponse.searchCourt

data class SearchCourtResponse(
    val `data`: List<CourtNameData>,
    val message: String,
    val status: Boolean
)