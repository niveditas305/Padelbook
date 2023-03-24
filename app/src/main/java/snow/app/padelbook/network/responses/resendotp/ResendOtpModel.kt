package snow.app.padelbook.network.responses.resendotp

import com.google.gson.annotations.SerializedName

data class ResendOtpModel (

  @SerializedName("status"  ) var status  : Boolean? = null,
  @SerializedName("message" ) var message : String?  = null,
  @SerializedName("OTP"     ) var OTP     : String?  = null

)