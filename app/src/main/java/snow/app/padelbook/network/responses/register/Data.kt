package snow.app.padelbook.network.responses.register

data class Data(
    val created_at: String,
    val device_token: String,
    val device_type: String,
    val email: String,
    val id : String,
    val is_terms: String,
    val otp: String,
    val otp_verified : String,
    val phone: String,
    val step_type: String,
    val updated_at: String,
    val user_type: String,
    val latitude: String,
    val longitude: String,
    val is_location : String,
    val check_password : String,
    val language_type : String

)

