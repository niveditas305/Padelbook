package snow.app.padelbook.network.responses.homeList

data class HomeDataResponse(
    val `data`: List<Data>,
    val matchList: List<Match>,
    val message: String,
    val no_of_pages: String,
    val currentPage: String,
    val user_score: String?=null,
    val status: Boolean
)