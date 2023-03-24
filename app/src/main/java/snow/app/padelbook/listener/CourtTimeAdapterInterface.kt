package snow.app.padelbook.listener

import snow.app.padelbook.network.responses.courtresponse.CategoryData
import snow.app.padelbook.network.responses.courtresponse.CourtData


interface CourtTimeAdapterInterface {

    fun onTimeClick(btnText: String, data: CategoryData, position: Int, courtData: CourtData)
}