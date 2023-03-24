package snow.app.padelbook.network.responses.cardList


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("brand")
    var brand: String,
    @SerializedName("card_image")
    var cardImage: String,
    @SerializedName("exp_month")
    var expMonth: Int,
    @SerializedName("exp_year")
    var expYear: Int,
    @SerializedName("id")
    var id: String,
    @SerializedName("last4")
    var last4: String
)