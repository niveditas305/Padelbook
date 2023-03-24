package snow.app.padelbook.network.responses.likeUnlike

data class LikeUnLikeResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)