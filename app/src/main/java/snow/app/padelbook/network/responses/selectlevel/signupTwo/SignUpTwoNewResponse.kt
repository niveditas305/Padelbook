package snow.app.padelbook.network.responses.selectlevel.signupTwo

import snow.app.padelbook.network.responses.login.LoginData

data class SignUpTwoNewResponse(
    val `data`: LoginData,
    val message: String,
    val status: Boolean
)