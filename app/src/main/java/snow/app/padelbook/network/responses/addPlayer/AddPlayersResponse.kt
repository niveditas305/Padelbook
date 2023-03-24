package snow.app.padelbook.network.responses.addPlayer

data class AddPlayersResponse(
    val `data`: AddPlayerData,
    val message: String,
    val status: Boolean
)