package snow.app.padelbook.network.responses.forgototpverified

import com.google.gson.annotations.SerializedName

data class ForgotOtpVerified (

  @SerializedName("status"  ) var status  : Boolean? = null,
  @SerializedName("data"    ) var data    : Data?    = Data(),
  @SerializedName("message" ) var message : String?  = null

)