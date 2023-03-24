package snow.app.padelbook.network.responses.cardList


import com.google.gson.annotations.SerializedName

data class CardListResponse(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)