package snow.app.padelbook.network.responses.createBooking

data class CreateBookingResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)