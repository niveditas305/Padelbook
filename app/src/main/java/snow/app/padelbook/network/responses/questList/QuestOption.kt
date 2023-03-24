package snow.app.padelbook.network.responses.questList

data class QuestOption(
    val option_id: String,
    val question_id: String,
    val title: String,
    var isSelected: Boolean = false

)