package snow.app.padelbook.network.responses.selectlevel.signupTwo

data class Data(
    val address: String,
    val created_at: String,
    val date_of_birth: String,
    val device_token: String,
    val device_type: Int,
    val email: String,
    val gender: String,
    val id: String,
    val image_file: String,
    val is_email_verified:String,
    val is_terms: String,
    val name: String,
    val otp: String,
    val otp_verified: String,
    val phone: String,
    val provider_id: String,
    val provider_type: String,
    val quiz_type_id: String,
    val score: String,
    val status: Int,
    val token: String,
    val step_type: Int,
    val updated_at: String,
    val user_type: Int
)