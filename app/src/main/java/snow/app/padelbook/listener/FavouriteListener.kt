package snow.app.padelbook.listener

import snow.app.padelbook.network.responses.clubList.ClubData


interface FavouriteListener {
   fun onIconClick(position: Int, clubId: String,)
   fun onFavClick(position: Int, data: ClubData)
}