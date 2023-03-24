package snow.app.padelbook.network.responses.matchList

import snow.app.padelbook.network.responses.homeList.Match

data class MatchListResponse(
    val `data`: List<Match>,
    val message: String,
    val status: Boolean
)