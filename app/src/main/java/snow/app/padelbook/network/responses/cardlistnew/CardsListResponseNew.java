package snow.app.padelbook.network.responses.cardlistnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardsListResponseNew {
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;

    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("last4")
        @Expose
        public String last4;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("exp_month")
        @Expose
        public String expMonth;
        @SerializedName("exp_year")
        @Expose
        public String expYear;
        @SerializedName("card_image")
        @Expose
        public String cardImage;

    }
}
