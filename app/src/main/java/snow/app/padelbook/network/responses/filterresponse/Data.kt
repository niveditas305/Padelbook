package snow.app.padelbook.network.responses.filterresponse

data class Data(
    val club_id: String,
    val court_feature: String,
    val court_id: String,
    val court_type: String,
    val created_at: String,
    val date: String,
    val duration: String,
    val gender: String,
    val id: String,
    val distance : Int,
    val latitude: String,
    val level: String,
    val longitude: String,
    val radius: String,
    val sort_type: String,
    val time: String,
    val updated_at: String,
    val user_id: String
)