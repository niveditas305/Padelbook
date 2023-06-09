package snow.app.padelbook.network.responses.login

data class LoginData(
    val address: String,
    val created_at: String,
    val date_of_birth: String,
    val device_token: String,
    val device_type: String,
    val email: String,
    val gender: String,
    val id: String,
    var image_file: String? = null,
    val is_email_verified: String,
    val is_terms: String,
    val name: String,
    val otp: String,
    val otp_verified: Int,
    val phone: String,
    val provider_id: String,
    val provider_type: String,
    val quiz_type_id: String,
    val status: String,
    val step_type: Int,
    var token: String?=null,
    val score: String,
    val latitude: String,
    val longitude: String,
    val updated_at: String,
    var is_location: String?,
    val check_password: String,
    val language_type: String,
    val user_type: String,
    val city: String,
    val country_code: String,
    // val category : String,
    val facebook_id: String,
    val customer_id: String,
    val first_login: String,
    val otp_verified_forget: String,
    val club_id: String,
    var fatch_contact: Int

)