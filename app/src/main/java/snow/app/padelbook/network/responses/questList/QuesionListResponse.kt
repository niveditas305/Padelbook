package snow.app.padelbook.network.responses.questList

data class QuesionListResponse(
    val `data`: List<QuestionData>,
    val message: String,
    val status: Boolean
)