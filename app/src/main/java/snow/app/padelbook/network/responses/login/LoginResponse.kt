package snow.app.padelbook.network.responses.login

data class LoginResponse(
    val `data`: LoginData,
    val message: String,
    val status: Boolean
)