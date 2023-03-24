package snow.app.padelbook.ui.cardscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Token
import com.stripe.android.view.CardMultilineWidget
import kotlinx.android.synthetic.main.activity_check_out.*
import snow.app.padelbook.R
import snow.app.padelbook.common.Utils.showToast
import snow.app.padelbook.network.responses.courtresponse.CategoryData
import snow.app.padelbook.network.responses.courtresponse.ClubTime
import snow.app.padelbook.network.responses.courtresponse.CourtData
import snow.app.padelbook.ui.BaseActivity
import snow.app.padelbook.viewModel.CardsVM
import snow.app.padelbook.viewModel.CreateBookingVM

class CheckOutActivity : BaseActivity() {
    lateinit var stripe: Stripe
    private var cardsVM : CardsVM?= null
    var bookingId = ""
    var matchId = ""
    var paymentMethod = ""
    var paymentType = ""
    var isClubPass = ""
    var club_pass_id = ""
    var paid_price = ""
    var courtData = ""
    var selectedDate = ""
    var match_type = ""
    var courtDataModel: CourtData? = null
    var selectedTime = ""
    var selectedAvailable_CateID = ""
    var selectedTimeModel: ClubTime? = null
    var selectedAvailable_CateIDModel: CategoryData? = null
     private var createBookVM: CreateBookingVM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)
        createBookVM = CreateBookingVM()
        cardsVM = CardsVM()
        observer()
        val cardInputWidget = findViewById<CardMultilineWidget>(R.id.cardInputWidget)
cardInputWidget.setShouldShowPostalCode(false)
        setClick()

        if (intent.hasExtra("bookingid")) {
            bookingId = intent!!.getStringExtra("bookingid").toString()
            matchId = intent!!.getStringExtra("matchId").toString()
            paymentMethod = intent!!.getStringExtra("paymentMethod").toString()
            paymentType = intent!!.getStringExtra("paymentType").toString()
            courtData = intent!!.getStringExtra("courtData").toString()
            selectedDate = intent!!.getStringExtra("selectedDate").toString()
            paid_price = intent!!.getStringExtra("paid_price").toString()
            isClubPass = intent!!.getStringExtra("isClubPass").toString()
            club_pass_id = intent!!.getStringExtra("club_pass_id").toString()
            match_type = intent!!.getStringExtra("match_type").toString()
            selectedTime = intent!!.getStringExtra("selectedTime").toString()
            selectedAvailable_CateID =
                intent!!.getStringExtra("selectedAvailable_CateID").toString()
            courtDataModel = Gson().fromJson(courtData, CourtData::class.java)
            selectedAvailable_CateIDModel =
                Gson().fromJson(selectedAvailable_CateID, CategoryData::class.java)
            selectedTimeModel = Gson().fromJson(selectedTime, ClubTime::class.java)
        }


      //  sendStripeTokenAndCallApi("CallCheckOutApi")

    }

    private fun observer() {


        createBookVM?.createBooking?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    showToast(this,user.message)
                    val returnIntent = Intent()
                    returnIntent.putExtra("matchId",user.data.match_id)
                    setResult(RESULT_OK, returnIntent)
                    finish()
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })

        cardsVM?.addCardRes?.observe(this, Observer { user ->
            if(user != null) {
                if (user.status) {
                  //  cardsVM?.paymentApi(this@CheckOutActivity,intent.getStringExtra("bookingid").toString(),user.stripe_token,"0")
                    createBookVM?.createBookingData(
                        this,
                        courtDataModel!!.club_id,
                        courtDataModel!!.court_id.toString(),
                        dateFormatWithHyphanConversion(selectedDate.toString()),
                        selectedTimeModel?.start_time.toString(),
                        selectedAvailable_CateIDModel?.category_id.toString(),
                        paymentType,
                        selectedTimeModel?.schedule_id.toString(),
                        paymentMethod,
                        match_type.toString(),
                        selectedAvailable_CateIDModel?.time.toString(),
                        selectedAvailable_CateIDModel?.price.toString(), paid_price,
                        user.stripe_token, "0", isClubPass, club_pass_id
                    )



                    showToast(this,user.message)

                } else {
                    showToast(this, user.message)
                }
            }
            else{
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
        cardsVM?.paymentRes?.observe(this, Observer { user ->
            if(user != null) {
                if (user.status) {

                        showToast(this,user.message)
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                } else {
                    showToast(this, user.message)
                }
            }
            else{
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
    }
    private fun setClick() {
        ivBack.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()

        }

        tvAddCard.setOnClickListener {
            if(cardInputWidget.validateAllFields())
            {
if(radioBtn.isChecked)
{

    cardsVM?.addCardApi(this,cardInputWidget.cardNumberEditText.text.toString(),cardInputWidget.expiryDateEditText.text.toString().split("/")[0],cardInputWidget.expiryDateEditText.text.toString().split("/")[1],cardInputWidget.cvcEditText.text.toString())

}
            else
{
    stripe = Stripe(
        this,
        "pk_test_51KTJwOErnoExGfQDkYblk6bPxeIhwVqwSxMkoCAAo0EAEt7KvqrlxGgSoYQXGvro6k2eCWgzF39S6OyZzF6XCEl000seRb40uI",
        "acct_1KTJwOErnoExGfQD"
    )

    cardInputWidget.cardParams?.let {
        stripe.createCardToken(it,"","acct_1KTJwOErnoExGfQD", callback = object : ApiResultCallback<Token> {
            override fun onError(e: Exception) {
                showToast(this@CheckOutActivity, e.toString())
                Log.e("StripeExample", "Error while creating card token", e)
            }

            override fun onSuccess(result: Token) {
                Log.e("params- res-", "--" + result.id)
                Log.e("params- res-", "--" + result)
cardsVM?.paymentApi(this@CheckOutActivity,intent.getStringExtra("bookingid").toString(),result.id,"1")

            }
        })
    }
}}
            else
            {
                showToast(this,"Please enter valid card")
            }


        }
    }


    private fun sendStripeTokenAndCallApi(stype: String) {

        val cardInputWidget = findViewById<CardMultilineWidget>(R.id.cardInputWidget)
        val params = cardInputWidget.paymentMethodCreateParams

            stripe = Stripe(
                this,
                "sk_test_51KTJwOErnoExGfQDVqBJUYj6TtlbJ5f3Gf3DgGnJCvoQT2bgWEVeREFMMkJo62EzLJAQfaNLfbKBlHeyTxVErfo400bgvOCXHq"
            )

        cardInputWidget.cardParams?.let {
            stripe.createCardToken(it,"","", callback = object : ApiResultCallback<Token> {
                override fun onError(e: Exception) {
                    showToast(this@CheckOutActivity, e.toString())
                    Log.e("StripeExample", "Error while creating card token", e)
                }

                override fun onSuccess(result: Token) {
                    Log.e("params- res-", "--" + result.id)
                    Log.e("params- res-", "--" + result)
                    if(stype == "CallExtendApi"){
    //                        orderExtendViewModel?.orderExtendData(this@CheckoutPayment,result.id,productExtendAmount!!,
    //                            extendDetails?.data!!.shippingChargeAmount,extendDetails?.data!!.insuranceAmount,totalExtendAmount!!,
    //                            extendedAmount!!,extendDetails?.data!!._id,extendedDate!!)
                    } else{
    //                        checkoutViewModel?.checkOutPaymentData(this@CheckoutPayment,result.id,productPrice!!,shippingCharges!!,
    //                            insuranceAmountAdd!!,totalAmount!!,AddressIDForCart!!)
                    }

                }
            })
        }
    /*        stripe.createCardToken(params!!, callback = object : ApiResultCallback<Token> {
                override fun onError(e: Exception) {
                    showToast(this@CheckOutActivity, e.toString())
                    Log.e("StripeExample", "Error while creating card token", e)
                }
                override fun onSuccess(result: Token) {
                    Log.e("params- res-", "--" + result.id)
                    Log.e("params- res-", "--" + result)
                    if(stype == "CallExtendApi"){
//                        orderExtendViewModel?.orderExtendData(this@CheckoutPayment,result.id,productExtendAmount!!,
//                            extendDetails?.data!!.shippingChargeAmount,extendDetails?.data!!.insuranceAmount,totalExtendAmount!!,
//                            extendedAmount!!,extendDetails?.data!!._id,extendedDate!!)
                    }
                    else{
//                        checkoutViewModel?.checkOutPaymentData(this@CheckoutPayment,result.id,productPrice!!,shippingCharges!!,
//                            insuranceAmountAdd!!,totalAmount!!,AddressIDForCart!!)
                    }

                }
            })*/


    }

}