package snow.app.padelbook.network.responses.score.singleScoreDetailNew

data class SingleScoreDetailResponseNew(
    val booking_time: String,
    val clubName: String,
    val club_status: String,
    val court_feature: String,
    val courtname: String,
    val `data`: List<Data>,
    val date: String,
    val message: String,
    val price: String,
    val score: List<Score>,
    val status: Boolean,
    val team: String,
    val time: String,
    val win_status: String
)