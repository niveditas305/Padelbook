package snow.app.padelbook.network.responses.courtresponse

data class CategoryData(
    val category_id: Int,
    val is_club_pass: Int,
    val price: String,
    val time: String,
    var club_pass   : ClubPass? = ClubPass()


)