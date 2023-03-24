package snow.app.padelbook.network.responses.notificationList

data class NotificationResponse(
    val `data`: List<NotifyData>,
    val message: String,
    val status: Boolean,
    val unread : Int
)