package snow.app.padelbook.network.responses.clubpassesistresponse

import com.google.gson.annotations.SerializedName

data class ClubDetail (

  @SerializedName("id"                  ) var id                : Int?    = null,
  @SerializedName("user_id"             ) var userId            : String? = null,
  @SerializedName("club_name"           ) var clubName          : String? = null,
  @SerializedName("website"             ) var website           : String? = null,
  @SerializedName("country_code"        ) var countryCode       : String? = null,
  @SerializedName("country_code_symbol" ) var countryCodeSymbol : String? = null,
  @SerializedName("phone"               ) var phone             : String? = null,
  @SerializedName("description"         ) var description       : String? = null,
  @SerializedName("address"             ) var address           : String? = null,
  @SerializedName("country"             ) var country           : String? = null,
  @SerializedName("street"              ) var street            : String? = null,
  @SerializedName("city"                ) var city              : String? = null,
  @SerializedName("state"               ) var state             : String? = null,
  @SerializedName("latitude"            ) var latitude          : String? = null,
  @SerializedName("longitude"           ) var longitude         : String? = null,
  @SerializedName("postal_code"         ) var postalCode        : String? = null,
  @SerializedName("profile_image"       ) var profileImage      : String? = null,
  @SerializedName("aminities_id"        ) var aminitiesId       : String? = null,
  @SerializedName("status"              ) var status            : String? = null,
  @SerializedName("type"                ) var type              : String? = null,
  @SerializedName("created_at"          ) var createdAt         : String? = null,
  @SerializedName("updated_at"          ) var updatedAt         : String? = null

)