package snow.app.padelbook.network.responses.register

data class RegisterResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)