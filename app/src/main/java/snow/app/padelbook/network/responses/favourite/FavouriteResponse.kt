package snow.app.padelbook.network.responses.favourite

import snow.app.padelbook.network.responses.clubList.ClubData

data class FavouriteResponse(
    val `data`: List<ClubData>,
    val message: String,
    val status: Boolean
)