package snow.app.padelbook.network.responses.clubListNew

import snow.app.padelbook.network.responses.clubList.ClubData

data class ClubListNewResponse(
    val address: String,
    val court_feature: String,
    val court_id: String,
    val court_type: String,
    val currentPage: String,
    val `data`: List<ClubData>,
    val date: String,
    val distance: String,
    val duration: String,
    val gender: String,
    val latitude: String,
    val longitude: String,
    val matchlist: List<Matchlist>,
    val message: String,
    val no_of_pages: String,
    val radius: String,
    val sort_type: String,
    val status: Boolean,
    val time: String,
    val total_match: String,
    val type: String
)