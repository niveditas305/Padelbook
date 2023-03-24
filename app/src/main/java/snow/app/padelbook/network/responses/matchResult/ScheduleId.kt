package snow.app.padelbook.network.responses.matchResult

data class ScheduleId(
    val id: Int,
    val round1: String,
    val round2: String,
    val round3: String,
    val status: String,
    val team: String
)