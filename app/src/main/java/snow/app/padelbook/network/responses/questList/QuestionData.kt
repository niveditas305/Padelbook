package snow.app.padelbook.network.responses.questList

data class QuestionData(
    val option: List<QuestOption>,
    val question : String,
    val question_id: String,
    val quiz_type_id: String,
    val select: String
)