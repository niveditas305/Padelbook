package snow.app.padelbook.network.responses.selectAnswer

data class SelectAnswerResponse(
    val `data`: Data,
    val message: String,
    val userScore: String? = null,
    val status: Boolean
)