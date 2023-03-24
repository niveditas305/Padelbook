package snow.app.padelbook.network.responses.selectlevel

data class SignUpStepTwoResponse(
    val `data`: List<SignUpStepTwoData>,
    val message: String,
    val status: Boolean
)