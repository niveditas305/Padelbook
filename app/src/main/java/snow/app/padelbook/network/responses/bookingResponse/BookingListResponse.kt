package snow.app.padelbook.network.responses.bookingResponse

data class BookingListResponse(
    val `data`: List<BookingData>?,
    val message: String?,
    val no_of_pages: String?,
    val currentPage: String?,
    val status: Boolean?
)