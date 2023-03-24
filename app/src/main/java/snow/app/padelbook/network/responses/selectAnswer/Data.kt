package snow.app.padelbook.network.responses.selectAnswer

data class Data(
    val created_at: String,
    val created_by: Int,
    val id: Int,
    val option_id: String,
    val question_id: String,
    val quiz_type_id: String,
    val score: String,
    val status: Int,
    val updated_at: String
)