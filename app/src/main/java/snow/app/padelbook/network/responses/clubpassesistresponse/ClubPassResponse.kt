package snow.app.padelbook.network.responses.clubpassesistresponse

import com.google.gson.annotations.SerializedName

data class ClubPassResponse (

  @SerializedName("status"  ) var status  : Boolean?        = null,
  @SerializedName("message" ) var message : String?         = null,
  @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()

)