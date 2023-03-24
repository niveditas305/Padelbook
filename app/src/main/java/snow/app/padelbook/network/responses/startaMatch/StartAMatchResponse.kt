package snow.app.padelbook.network.responses.startaMatch

data class StartAMatchResponse(
    val court_id: String,
    val `data`: List<Data>,
    val message: String,
    val status: Boolean,
    val address: String,
    val date: String,
    val time: String,
    val latitude: String,
    val longitude: String
    )