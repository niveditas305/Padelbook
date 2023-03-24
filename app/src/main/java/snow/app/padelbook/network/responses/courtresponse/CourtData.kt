package snow.app.padelbook.network.responses.courtresponse

data class CourtData(
    val available_status: String,
    val club_id: String,
    val club_status: String,
    val court_id: String,
    val court_type: String,
    val cancel_policy: String,
    val description: String,
    val name: String,
    val clubName : String,
    val court_feature : String,
    val category_type : List<CategoryData>
)