package snow.app.padelbook.network.responses.clubpassesistresponse

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("pricing_name") var pricing_name: String? = null,
    @SerializedName("club_id") var clubId: String? = null,
    @SerializedName("bookings_weekly") var bookingsWeekly: String? = null,
    @SerializedName("expiry_date") var expiryDate: String? = null,
    @SerializedName("maximum_bookings") var maximumBookings: String? = null,
    @SerializedName("remaining_bookings") var remainingBookings: String? = null,
    @SerializedName("pricing_id") var pricingId: String? = null,
    @SerializedName("duration") var duration: String? = null,
    @SerializedName("original_price") var originalPrice: String? = null,
    @SerializedName("discount_fixed_price") var discountFixedPrice: String? = null,
    @SerializedName("total_price") var totalPrice: String? = null,
    @SerializedName("club_pass") var clubPass: String? = null,
    @SerializedName("unlimited_bookings") var unlimitedBookings: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("image_file") var imageFile: String? = null,
    @SerializedName("club_name") var clubName: String? = null,
    @SerializedName("isActive"           ) var isActive          : Int? = null,

    @SerializedName("club_detail") var clubDetail: ClubDetail? = ClubDetail()

)