package snow.app.padelbook.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.toolbar.view.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.BookingAdapter
import snow.app.padelbook.listener.ClickEvent
import snow.app.padelbook.network.responses.bookingResponse.BookingData
import snow.app.padelbook.viewModel.CreateBookingVM

class BookingActivity : BaseActivity(), ClickEvent {
    private var bookingVM: CreateBookingVM? = null

    private var adapter: BookingAdapter? = null
    var arrayList: ArrayList<BookingData> = ArrayList()
    var pastVisiblesItems: Int = 0
    var firstVisibleItem = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var totalPages: Int = 0
    private val previousTotal = 0
    private var loading = true
    private val visibleThreshold = 1
    private var current_page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        bookingVM = CreateBookingVM()

        // <----------booking list api for all (Confirmed+pending)
        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            arrayList.clear()
            bookingVM?.bookingListData(this, "0", current_page.toString())
        }

        setTabLayout()
        setToolbar()
        listener()

        val mLayoutManager: LinearLayoutManager
        mLayoutManager = LinearLayoutManager(this)
        recycleBooking.setLayoutManager(mLayoutManager)
        recycleBooking.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount()
                    totalItemCount = mLayoutManager.getItemCount()
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            Log.e("...", "Last Item Wow !")
                            // Do pagination.. i.e. fetch new data
                            if (current_page <= totalPages) {
                                current_page = current_page + 1
                                if (isNetworkConnected()) {
                                    bookingVM?.bookingListData(
                                        this@BookingActivity,
                                        "0",
                                        current_page.toString()
                                    )
                                } else {

                                    showInternetToast()
                                }
                            }
                            loading = true
                        }
                    }
                }
            }
        })

    }

    private fun listener() {
        bookingVM?.bookingList?.observe(this, Observer { user ->
            user?.let {
                it.status?.let { status->
                    if (status) {
                        totalPages = user.no_of_pages?.toInt() ?: 0
                        if (it.status) {
                            it.data?.let { list ->
                                //  showToast(this, user.message)
                                //arrayList.clear()
                                arrayList.addAll(list)
                                setAdapter(arrayList)
                                if (list.isEmpty()
                                ) {
                                    llNoResultsFound.visibility = View.VISIBLE
                                    recycleBooking.visibility = View.GONE
                                    if (tabLayout.selectedTabPosition == 0) {
                                        tvEmptyText.text =
                                            getString(R.string.there_are_no_upcoming_bookings)
                                    } else if (tabLayout.selectedTabPosition == 1) {
                                        tvEmptyText.text =
                                            getString(R.string.there_are_no_confirmed_bookings)
                                    } else if (tabLayout.selectedTabPosition == 2) {
                                        tvEmptyText.text =
                                            getString(R.string.there_are_no_pending_bookings)
                                    } else if (tabLayout.selectedTabPosition == 3) {
                                        tvEmptyText.text =
                                            getString(R.string.there_are_no_bookings_in_your_history)
                                    }
                                } else {
                                    llNoResultsFound.visibility = View.GONE
                                    recycleBooking.visibility = View.VISIBLE
                                }

                            } ?: kotlin.run {
                                showToast(this, user.message)
                            }
                        } else {
                            showToast(this, user.message)
                        }


                    } else {
                        showToast(this, user.message)
                    }
                } ?: kotlin.run {
                    showToast(this, getString(R.string.something_went_wrong))

                }

            } ?: kotlin.run {
                showToast(this, getString(R.string.something_went_wrong))

            }


        })
    }

    private fun setToolbar() {
        toolbarId.tvTitle.visibility = View.VISIBLE
        toolbarId.tvTitle.setText(getString(R.string.menu_two))
        toolbarId.separator.visibility = View.VISIBLE
        toolbarId.ivBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }

    private fun setTabLayout() {

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // <----------booking list api for all (Confirmed+pending) ---------->
                        if (!isNetworkConnected()) {
                            showInternetToast()
                        } else {
                            arrayList.clear()
                            bookingVM?.bookingListData(
                                this@BookingActivity,
                                "0",
                                current_page.toString()
                            )
                        }
                    }
                    1 -> {
                        // <----------booking list api for Confirmed only ---------->
                        if (!isNetworkConnected()) {
                            showInternetToast()
                        } else {
                            arrayList.clear()
                            bookingVM?.bookingListData(
                                this@BookingActivity,
                                "1",
                                current_page.toString()
                            )
                        }

                    }
                    2 -> {
                        // <----------booking list api for Pending only ---------->
                        if (!isNetworkConnected()) {
                            showInternetToast()
                        } else {
                            arrayList.clear()
                            bookingVM?.bookingListData(
                                this@BookingActivity,
                                "2",
                                current_page.toString()
                            )
                        }
                    }
                    3 -> {
                        // <----------booking list api for Pending only ---------->
                        if (!isNetworkConnected()) {
                            showInternetToast()
                        } else {
                            arrayList.clear()
                            bookingVM?.bookingListData(
                                this@BookingActivity,
                                "3",
                                current_page.toString()
                            )
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun setAdapter(data: List<BookingData>) {
        Log.e("ddd", Gson().toJson(data))
        adapter = BookingAdapter(this, data as ArrayList<BookingData>, this)
        recycleBooking.adapter = adapter
    }

    override fun clickEvent(matchId: String, playerKey: String, courtFeature: String) {

    }

    override fun clickEventForBooking(position: Int) {
        //  startActivity(Intent(this, AddResultActivity::class.java))
    }

    override fun clickForBookingDetail(matchData: BookingData) {
        startActivity(
            Intent(this, GamesActivity::class.java)
                .putExtra("FromBookingList", "FromBookingList")
                .putExtra("forback", "bookings")
                .putExtra("matchId", matchData.match_id)
        )
        Log.d("MATCH_ID", matchData.match_id)
    }
}