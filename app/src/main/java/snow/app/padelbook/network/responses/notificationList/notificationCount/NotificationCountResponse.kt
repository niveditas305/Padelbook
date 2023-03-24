package snow.app.padelbook.network.responses.notificationList.notificationCount

data class NotificationCountResponse(
    val `data`: String,
    val message: String,
    val status: Boolean,
    val fatch_contact : String
)