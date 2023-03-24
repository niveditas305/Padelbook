package snow.app.padelbook.network.responses.createMatch

import snow.app.padelbook.network.responses.createMatch.Data

data class CreateMatchResponse(
    val `data`: Data,
    val message: String,
    val stripe_token: String,
    val status: Boolean
)