package snow.app.padelbook.network.responses.contactListResponse

data class ContactListDataResponse(
    val `data`: List<ContactInfoData>,
    val message: String,
    val no_of_pages: String,
    val status: Boolean
)