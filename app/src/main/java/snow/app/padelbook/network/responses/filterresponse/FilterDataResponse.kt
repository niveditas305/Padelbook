package snow.app.padelbook.network.responses.filterresponse

data class FilterDataResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)