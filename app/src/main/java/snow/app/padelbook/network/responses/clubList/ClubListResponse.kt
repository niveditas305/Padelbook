package snow.app.padelbook.network.responses.clubList

data class ClubListResponse(
    val status: Boolean,
    val `data`: List<ClubData>,
    val message: String,
    val court_id : String,
    val latitude : String,
    val longitude : String,
    val radius : String,
    val address: String,
    val time : String,
    val date : String,
    val sort_type : String,
    val court_feature : String,
    val court_type : String,
    val duration : String,
    val distance : String,
    val gender : String,

)