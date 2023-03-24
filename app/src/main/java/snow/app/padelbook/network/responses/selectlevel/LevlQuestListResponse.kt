package snow.app.padelbook.network.responses.selectlevel

data class LevlQuestListResponse(
    val `data`: List<SelectLevel>,
    val message: String,
    val status: Boolean
)