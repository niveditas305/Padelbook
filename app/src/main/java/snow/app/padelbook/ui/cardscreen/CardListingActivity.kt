package snow.app.padelbook.ui.cardscreen

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener
import kotlinx.android.synthetic.main.activity_card_listing.*
import kotlinx.android.synthetic.main.popup_game_summary.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.CardListAdapter
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.cardlistnew.CardsListResponseNew
import snow.app.padelbook.network.responses.clubList.ClubData
import snow.app.padelbook.ui.BaseActivity
import snow.app.padelbook.ui.GamesActivity
import snow.app.padelbook.viewModel.CardsVM
import android.app.Activity
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import snow.app.padelbook.network.responses.courtresponse.CategoryData
import snow.app.padelbook.network.responses.courtresponse.ClubTime
import snow.app.padelbook.network.responses.courtresponse.CourtData
import snow.app.padelbook.utils.SwipeHelper
import snow.app.padelbook.viewModel.CreateBookingVM


class CardListingActivity : BaseActivity(), RecyclerTouchListener.RecyclerTouchListenerHelper,
    FavouriteListener {
    private var mList: MutableList<CardsListResponseNew.Datum>? = null
    private var adapter: CardListAdapter? = null

    //  private lateinit var onTouchListener: RecyclerTouchListener
    private var touchListener: OnActivityTouchListener? = null
    private var cardsVM: CardsVM? = null
    var bookingId = ""
    var matchId = ""
    var paymentMethod = ""
    var paymentType = ""
    var isClubPass = ""
    var stripeToken = ""
    var club_pass_id = ""
    var paid_price = ""
    var courtData = ""
    var selectedDate = ""
    var match_type = 0
    var courtDataModel: CourtData? = null
    var selectedTime = ""
    var selectedAvailable_CateID = ""
    var selectedTimeModel: ClubTime? = null
    var selectedAvailable_CateIDModel: CategoryData? = null
    private var createBookVM: CreateBookingVM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_listing)
        createBookVM = CreateBookingVM()

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
            club_pass_id = intent!!.getStringExtra("club_pass_id") ?: ""
            Log.e("courtData str","--"+courtData)
            Log.e("match type","--"+intent!!.getStringExtra("match_type").toString())
            match_type = intent!!.getIntExtra("match_type",0)
            selectedTime = intent!!.getStringExtra("selectedTime").toString()
            selectedAvailable_CateID =
                intent!!.getStringExtra("selectedAvailable_CateID").toString()
            courtDataModel = Gson().fromJson(courtData, CourtData::class.java)

            selectedAvailable_CateIDModel =
                Gson().fromJson(selectedAvailable_CateID, CategoryData::class.java)
            selectedTimeModel = Gson().fromJson(selectedTime, ClubTime::class.java)
        }
        //  setAdapter(user.data)
        cardsVM = CardsVM()
        observer()
        object : SwipeHelper(this, recycleCards, false) {

            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                underlayButtons?.add(SwipeHelper.UnderlayButton(
                    "Remove",
                    null,
                    Color.parseColor("#FF0F0F"), Color.parseColor("#ffffff"),
                    UnderlayButtonClickListener { pos: Int ->
                        showCardRemovePopup(pos)

                    }

                ))


                /*  // Archive Button
                  underlayButtons?.add(SwipeHelper.UnderlayButton(
                      "Archive",null,
                      Color.parseColor("#000000"), Color.parseColor("#ffffff"),
                      UnderlayButtonClickListener { pos: Int ->

                          showCardRemovePopup(pos)
                      }
                  ))*/


            }
        }
        /*     onTouchListener = RecyclerTouchListener(this,recycleCards)
            // onTouchListener.setIndependentViews(R.id.remove)
          //   onTouchListener.setIndependentViews(R.id.remove)

             onTouchListener.setSwipeOptionViews(R.id.remove,R.id.add, R.id.edit)*//*
        onTouchListener.setSwipeable(R.id.rowFG,R.id.rowBG,
                RecyclerTouchListener.OnSwipeOptionsClickListener { viewID, position ->

                    Log.e("1",viewID.toString())
                    Log.e("22","--> "+position)
                    when(viewID){
                        R.id.remove ->{
                            showCardRemovePopup(position)
                       //     showCardRemovePopup()
                        }
                    }
                })
*//*

        .setSwipeable(R.id.rowFG,R.id.rowBG, RecyclerTouchListener.OnSwipeOptionsClickListener { viewID, position ->

        })
*/
        /*           .setClickable(object : RecyclerTouchListener.OnRowClickListener{
                       override fun onRowClicked(position: Int) {
                           //showCardRemovePopup(position)
                       }

                       override fun onIndependentViewClicked(independentViewID: Int, position: Int) {
                           Log.e("1",independentViewID.toString())
                           Log.e("22","--> "+position)
                           when(independentViewID){
                               R.id.remove ->{
                                  // showCardRemovePopup(position)
                                   //     showCardRemovePopup()
                               }
                           }
                       }

                   })*/
    }

    private fun observer() {

        createBookVM?.createBooking?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {

                    val returnIntent = Intent()
                    returnIntent.putExtra("matchId",user.data.match_id)
                    setResult(RESULT_OK, returnIntent)
                    finish()
                    // finish()

                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })

        cardsVM?.getCardsRes?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    if (user.data != null) {
                        //  showToast(this, user.message)
                        mList = user.data
                        recycleCards.visibility = View.VISIBLE
                        setAdapter(user.data)
                    } else {
                        recycleCards.visibility = View.GONE
                        showToast(this, user.message)
                    }
                } else {
                    recycleCards.visibility = View.GONE
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
        cardsVM?.deleteCardRes?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    cardsVM?.getCardsApi(this)
                    showToast(this, user.message)

                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
        cardsVM?.paymentRes?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()
                    // finish()

                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
    }

    private fun showCardRemovePopup(positionn: Int) {
        val alertdialog = AlertDialog.Builder(this)
        alertdialog.setTitle("Are you sure, you want to remove this card?")
        alertdialog.setCancelable(false)

        alertdialog.setPositiveButton("Yes") { dialog, which ->
            cardsVM?.deleteCardApi(this, mList!!.get(positionn).id.toString())
            dialog?.dismiss()
        }

        alertdialog.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(alertdialog: DialogInterface?, which: Int) {
                alertdialog?.dismiss()
            }
        })
        alertdialog.show()
    }


    private fun setAdapter(data: MutableList<CardsListResponseNew.Datum>) {
        adapter = CardListAdapter(this, data, this)
        recycleCards.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        cardsVM?.getCardsApi(this)
//        recycleCards.addOnItemTouchListener(onTouchListener)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (touchListener != null) touchListener!!.getTouchCoordinates(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun onPause() {
        super.onPause()
        //  recycleCards.removeOnItemTouchListener(onTouchListener)
    }

    private fun setClick() {
        ivBack.setOnClickListener {
            val returnIntent = Intent()
            setResult(RESULT_CANCELED, returnIntent)
            finish()
        }
        tvAddCard.setOnClickListener {
            if (intent.getStringExtra("type").equals("match")) {
                /* startActivityForResult(
                     Intent(
                         this,
                         CheckOutActivity::class.java
                     ).putExtra("bookingid", bookingId)
                         .putExtra("type", intent.getStringExtra("type")),
                     0
                 )
 */

                startActivityForResult(
                    Intent(this, CheckOutActivity::class.java)
                        .putExtra("bookingid", bookingId).putExtra("matchId", matchId)
                        .putExtra("type", "booking")
                        .putExtra("paymentMethod", paymentMethod)
                        .putExtra("paymentType", paymentType)
                        .putExtra("courtData", Gson().toJson(courtDataModel))
                        .putExtra("selectedTime", Gson().toJson(selectedTimeModel))
                        .putExtra("match_type", match_type)
                        .putExtra("paid_price", paid_price)
                        .putExtra("isClubPass", isClubPass)
                        .putExtra("club_pass_id", club_pass_id)
                        .putExtra("selectedDate", selectedDate)
                        .putExtra(
                            "selectedAvailable_CateID",
                            Gson().toJson(selectedAvailable_CateIDModel)
                        ), 0
                )

            } else {
                /*   startActivityForResult(
                       Intent(
                           this,
                           CheckOutActivity::class.java
                       ).putExtra("bookingid", bookingId)
                           .putExtra("type", intent.getStringExtra("type")),
                       1
                   )*/

                startActivityForResult(
                    Intent(this, CheckOutActivity::class.java)
                        .putExtra("bookingid", bookingId).putExtra("matchId", matchId)
                        .putExtra("type", "booking")
                        .putExtra("paymentMethod", paymentMethod)
                        .putExtra("paymentType", paymentType)
                        .putExtra("courtData", Gson().toJson(courtDataModel))
                        .putExtra("selectedTime", Gson().toJson(selectedTimeModel))
                        .putExtra("match_type", match_type)
                        .putExtra("paid_price", paid_price)
                        .putExtra("isClubPass", isClubPass)
                        .putExtra("club_pass_id", club_pass_id)
                        .putExtra("selectedDate", selectedDate)
                        .putExtra(
                            "selectedAvailable_CateID",
                            Gson().toJson(selectedAvailable_CateIDModel)
                        ), 0
                )
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("resss", resultCode.toString())
        if (resultCode == Activity.RESULT_CANCELED) {
            //   val returnIntent = Intent()
            //    setResult(RESULT_CANCELED, returnIntent)
            // finish()
        } else {
            val returnIntent = Intent()
            returnIntent.putExtra("matchId",data?.getStringExtra("matchId"))
            setResult(RESULT_OK, returnIntent)
            finish()

        }
    }

    override fun setOnActivityTouchListener(listener: OnActivityTouchListener?) {
        touchListener = listener
    }

    override fun onIconClick(position: Int, clubId: String) {
        /*    cardsVM?.paymentApi(
                this,
                bookingId,
                clubId,
                "0"
            ) //price select needs to send in the api , paid_price , club_pass ==0 ,1*/

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
            clubId, "0", isClubPass, club_pass_id
        )

    }

    override fun onFavClick(position: Int, data: ClubData) {

    }
}