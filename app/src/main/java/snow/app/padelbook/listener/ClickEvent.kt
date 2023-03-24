package snow.app.padelbook.listener

import snow.app.padelbook.network.responses.bookingResponse.BookingData
import snow.app.padelbook.network.responses.homeList.Match

interface ClickEvent {
    fun clickEvent(matchId: String, playerKey: String, courtFeature: String)
    fun clickEventForBooking(position : Int)
    fun clickForBookingDetail(matchData: BookingData)
}