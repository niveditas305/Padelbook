package snow.app.padelbook.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_club_pass_list.*
import kotlinx.android.synthetic.main.club_pass_detail_layout.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.ClubPassListAdapter
import snow.app.padelbook.network.responses.cardlistnew.CardsListResponseNew
import snow.app.padelbook.network.responses.clubpassesistresponse.Data
import snow.app.padelbook.viewModel.CardsVM
import android.text.Html
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_club_pass.view.*


class ClubPassListActivity : BaseActivity() {
    var clubPassListAdapter: ClubPassListAdapter? = null
    var clubList: ArrayList<Data> = ArrayList()
    private var cardsVM: CardsVM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_pass_list)
        cardsVM = CardsVM()
        initViews()
        initListeners()
    }

    fun initListeners() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun initViews() {
        setAdapter()

        if (isNetworkConnected()) {
            cardsVM?.getClubPassList(this)
        } else {
            showInternetToast()
        }

        observers()
    }

    fun observers() {
        cardsVM?.clubPassList?.observe(this, Observer { user ->
            if (user != null) {
                user?.status?.let {
                    if (user.status == true) {
                        if (user.data != null) {
                            //  showToast(this, user.message)
                            clubList.clear()
                            clubList.addAll(user.data)
                            clubPassListAdapter?.setItems(clubList)
                            if(user.data.size==0){
                                llNoResultsFound.visibility= View.VISIBLE
                                clubPassesRecyclerView.visibility= View.GONE

                            }else{
                                llNoResultsFound.visibility= View.GONE
                                clubPassesRecyclerView.visibility= View.VISIBLE
                            }
                        } else {
                          //  showToast(this, user.message)
                            llNoResultsFound.visibility= View.VISIBLE
                            clubPassesRecyclerView.visibility= View.GONE
                        }

                    } else {
                        llNoResultsFound.visibility= View.VISIBLE
                        clubPassesRecyclerView.visibility= View.GONE
                    }
                } ?: kotlin.run {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
    }


    private fun setAdapter() {
        if (clubPassListAdapter == null) {
            val mLayoutManager = LinearLayoutManager(this)
            clubPassesRecyclerView.layoutManager = mLayoutManager
            clubPassesRecyclerView.setHasFixedSize(false)

            clubPassListAdapter = ClubPassListAdapter(this)
            clubPassesRecyclerView.adapter = clubPassListAdapter
            clubPassListAdapter?.onItemClick = {
                showClubPassDetails(it)
            }
        }
        clubPassListAdapter?.setItems(clubList)
    }

    private fun showClubPassDetails(data: Data) {
        //
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.club_pass_detail_layout)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogData.dismiss()

        val bookingData =
            "<font color=#6c6c6c>" + getString(R.string.bookings_per_week) + ": " + "</font> <font color=#000000>" + data.bookingsWeekly + "</font>"
        dialogData.tvBookings.text =
            HtmlCompat.fromHtml(bookingData, HtmlCompat.FROM_HTML_MODE_LEGACY)
        /*   dialogData.tvBookings.text =
               getString(R.string.bookings_per_week) + ": " + data.bookingsWeekly*/


        val totalBookingData =
            "<font color=#6c6c6c>" + getString(R.string.total_bookings) + ": " + "</font> <font color=#000000>" + data.maximumBookings + "</font>"
        dialogData.tvTotalBookings.text =
            HtmlCompat.fromHtml(totalBookingData, HtmlCompat.FROM_HTML_MODE_LEGACY)
        data.remainingBookings?.let {
            val remainingBookingsData =
                "<font color=#6c6c6c>" + getString(R.string.remaining_bookings) + ": " + "</font> <font color=#000000>" + it + "</font>"
            dialogData.tvRemainingBookings.text =
                HtmlCompat.fromHtml(remainingBookingsData, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }?:kotlin.run {
            val remainingBookingsData =
                "<font color=#6c6c6c>" + getString(R.string.remaining_bookings) + ": " + "</font> <font color=#000000>" + "0" + "</font>"
            dialogData.tvRemainingBookings.text =
                HtmlCompat.fromHtml(remainingBookingsData, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }


        val expiryDateData =
            "<font color=#6c6c6c>" + getString(R.string.exp_date) + ": " + "</font> <font color=#000000>" + data.expiryDate + "</font>"
        dialogData.tvExpiryDate.text =
            HtmlCompat.fromHtml(expiryDateData, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val durationData =
            "<font color=#6c6c6c>" + getString(R.string.match_duration) + ": " + "</font> <font color=#000000>" + data.duration + "</font>"
        dialogData.tvMatchDuration.text =
            HtmlCompat.fromHtml(durationData, HtmlCompat.FROM_HTML_MODE_LEGACY)

        dialogData.tvClubName.text = data.clubName
        data.pricing_name?.let {
            val pricingDetailsData =
                "<font color=#6c6c6c>" + getString(R.string.pricing_rules) + ": " + "</font> <font color=#000000>" + it + "</font>"
            dialogData.tvPriceDetails.text =
                HtmlCompat.fromHtml(pricingDetailsData, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }?:kotlin.run {
            val pricingDetailsData =
                "<font color=#6c6c6c>" + getString(R.string.pricing_rules) + ": " + "</font> <font color=#000000>" + "N/A" + "</font>"
            dialogData.tvPriceDetails.text =
                HtmlCompat.fromHtml(pricingDetailsData, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }


        Glide.with(this)
            .load(data.imageFile)             // image url
            .placeholder(R.drawable.ic_user) // any placeholder to load at start
            .error(R.drawable.ic_user)  // any image in case of error
            .into(dialogData.circularImg)

        dialogData.tvGotIt.setSafeOnClickListener {
            dialogData.dismiss()
            // showDialogCancal(data)
        }
        dialogData.show()
    }

}