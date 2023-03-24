package snow.app.padelbook.network.responses.contactListResponse

data class ContactInfoData(
    val id: Int,
    val image_file: String,
    val customer_id : String,
    val phone_num: String,
    var status: Int,
    var userInvite: Boolean,
    val username: String
)