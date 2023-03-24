package snow.app.padelbook.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_court.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.CourtAdapter
import snow.app.padelbook.adapter.TimeSlotAdapter
import snow.app.padelbook.listener.CourtTimeAdapterInterface
import snow.app.padelbook.network.responses.courtresponse.CategoryData
import snow.app.padelbook.network.responses.courtresponse.ClubTime
import snow.app.padelbook.network.responses.courtresponse.CourtData
import snow.app.padelbook.ui.GamesActivity
import snow.app.padelbook.viewModel.CourtListVM
import java.util.*
import kotlin.collections.ArrayList

class CourtFragment(var ClubID: String, var CALL_FROM: String, var scheduleID: String?) :
    BaseFragment(),
    TimeSlotAdapter.onClickListener, CourtTimeAdapterInterface {

    private var singleRowCalendar: SingleRowCalendar? = null
    var adapter: CourtAdapter? = null
    var timeSlotAdapter: TimeSlotAdapter? = null
    private var courtDataVM: CourtListVM? = null

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentYear = 0
    var selectedDate: Date? = null

    var clubTimeList: ArrayList<ClubTime> = ArrayList()
    var courtDataList: ArrayList<CourtData> = ArrayList()
    var isBookingAllowed = false
    var isSelectedCurrentDate = false
    var cancelPolicy = ""
    var selectedTime: ClubTime? = null
    var selectAvailableCate_Id: CategoryData? = null
    var clubData: CourtData? = null

//    private var mySelectionManager: CalendarSelectionManager?=null
//    private var myCalendarChangesObserver: CalendarChangesObserver?=null
//    private var myCalendarViewManager: CalendarViewManager?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_court, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //      setCalenderData()
        courtDataVM = CourtListVM()
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        clicks()
        setCustomLayout()
        Log.d("date--", "from search frag" + SearchFragment.selectedDateFromFilter)
        if (SearchFragment.selectedDateFromFilter?.isNotEmpty() == true) {
            isSelectedCurrentDate = false
            /* val list:ArrayList<Date> = ArrayList()
             list.add(SearchFragment.selectedDateFromFilter)*/

            if (SearchFragment.selectedDateFromFilter != null) {
                // < ----------- handle selection of date that we get from getTime api -------->
                for (i in 0..getFutureDatesOfCurrentMonth().size - 1) {
                    Log.d("selectedArea", getFutureDatesOfCurrentMonth()[i].toString())

                    if ((dateFormatWithHyphanConversion(getFutureDatesOfCurrentMonth()[i].toString())).equals(
                            SearchFragment.selectedDateFromFilter
                        )
                    ) {
                        Log.d("savedDate...", getFutureDatesOfCurrentMonth()[i].toString())
                        Log.d("savedDate...", SearchFragment.selectedDateFromFilter.toString())
                        selectedDate = getFutureDatesOfCurrentMonth()[i]
                        singleRowCalendar!!.select(i)
                        break
                    }

                }
            }
        } else {
            isSelectedCurrentDate = true

        }

//        var savedDate = Calendar.getInstance().getTime()
//        singleRowCalendar = main_single_row_calendar.apply {
//            calendarViewManager = myCalendarViewManager!!
//            calendarChangesObserver = myCalendarChangesObserver!!
//            calendarSelectionManager = mySelectionManager!!
//            futureDaysCount = 365
//            includeCurrentDate = true
//            setDates(getFutureDatesOfCurrentMonth())
//            init()
//        }
//

        observer()

    }


    fun observer() {
        courtDataVM?.userCourt?.observe(requireActivity(), androidx.lifecycle.Observer { user ->
            if (user != null) {
                if (user.status) {

                    isBookingAllowed = user.isBookingAllowed
                    if (callFrom == "TimeAdapterClick") {
                        courtDataList.clear()
                        courtDataList.addAll(user.data)

                        //   showToast(user.message)
                        if (courtDataList.isNotEmpty()) {
                            headingAvailable.visibility = View.VISIBLE
                            recycleAvailable.visibility = View.VISIBLE
                            recycleAvailableView.visibility = View.VISIBLE
                            setAdapter()
                        }

                    } else {
                        clubTimeList.clear()
                        clubTimeList.addAll(user.club_time)

                        if (clubTimeList.isNotEmpty()) {
                            recycleTime.visibility = View.VISIBLE
                            tvNoAvailability.visibility = View.GONE
                        } else {
                            recycleTime.visibility = View.GONE
                            tvNoAvailability.visibility = View.VISIBLE
                        }

                        setTimeSlotAdapter()
                        recycleAvailable.visibility = View.GONE
                        headingAvailable.visibility = View.GONE
                        recycleAvailableView.visibility = View.GONE
                        popupSubmit.setText(getString(R.string.book_now))
                        popupSubmit.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.round_corner_grey
                        )
                        popupSubmit.isClickable = false
                        popupSubmit.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }

                } else {
                    showToast(user.message)
                }
            } else {
                showToast(getString(R.string.something_went_wrong))
            }
        })
    }

    private fun setCustomLayout() {
        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date, isSelected: Boolean
            ): Int {

                var tempType: Boolean = isSelected
                var t = ""
                Log.e("isSelected", tempType.toString())

                val cal = Calendar.getInstance()
                // cal.time = date

                Log.d("currentTime.....", date.toString())

                Log.d("CalenderDate", cal.time.toString())


                if (dateFormatWithHyphanConversion(date.toString()).equals(
                        dateFormatWithHyphanConversion(
                            cal.time.toString()
                        )
                    )
                ) {
                    // Log.e("check", "if")
                    if (isSelectedCurrentDate) {
                        tempType = true
                        t = "1"
                        //singleRowCalendar!!.deselect(position)
                    } else {
                        // tempType = false
                    }

                }


                if (tempType) {
                    selectedDate = date

                    if (!isNetworkConnected()) {
                        showInternetToast()
                    } else {
                        callFrom = ""
                        courtDataVM?.courtListData(
                            requireContext(), ClubID,
                            dateFormatWithHyphanConversion(selectedDate.toString()), ""
                        )
                    }
                }

                return if (isSelected) {
                    /* if(t.equals("1"))
                     { R.layout.current_calendar_item}
                     else {*/
                    R.layout.selected_calendar_item
                    //     }
                } else {
                    R.layout.calendar_item
                }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date, position: Int, isSelected: Boolean
            ) {

                holder.itemView.tvDate.text = DateUtils.getDayNumber(date)
                holder.itemView.tvWeek.text = DateUtils.getDay3LettersName(date)
                holder.itemView.tvMonthName.text = DateUtils.getMonth3LettersName(date)
                // bind data to calendar item views
            }
        }

        var myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenWeekMonthYearChanged(
                weekNumber: String,
                monthNumber: String,
                monthName: String,
                year: String,
                date: Date
            ) {
                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
                Log.d("currentDate", "whenWeekMonthYearChanged" + date)
            }

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
                Log.d("currentDate", "whenSelectionChanged" + date)
            }

            override fun whenCalendarScrolled(dx: Int, dy: Int) {
                super.whenCalendarScrolled(dx, dy)
            }

            override fun whenSelectionRestored() {
                super.whenSelectionRestored()
            }

            override fun whenSelectionRefreshed() {
                super.whenSelectionRefreshed()
            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                Log.d("currentDate", "canBeItemSelected" + date)
                // return true if item can be selected

                isSelectedCurrentDate = false
                val cal = Calendar.getInstance()
                cal.time = date
                recycleTime.visibility = View.VISIBLE

                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    callFrom = ""
                    selectedDate = date
                    courtDataVM?.courtListData(
                        requireContext(), ClubID,
                        dateFormatWithHyphanConversion(selectedDate.toString()), ""
                    )
                }
                return true

            }
        }

        singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            futureDaysCount = 365
            includeCurrentDate = true

            setDates(getFutureDatesOfCurrentMonth())
            init()
            //  if (!savedDate.isNullOrEmpty()) {
            // < ----------- handle selection of date that we get from getTime api -------->

            //  }
            //  singleRowCalendar!!.select()


        }


        var date = Calendar.getInstance().getTime()
        Log.d("date", date.toString())

        for (i in 0..getFutureDatesOfCurrentMonth().size - 1) {
            Log.e(
                "selectedArea",
                dateFormatWithHyphanConversion(getFutureDatesOfCurrentMonth()[i].toString())
            )
            Log.e("cu", dateFormatWithHyphanConversion(date.toString()))

            if (dateFormatWithHyphanConversion(getFutureDatesOfCurrentMonth()[i].toString()).equals(
                    dateFormatWithHyphanConversion(date.toString())
                )
            ) {

                singleRowCalendar!!.select(i)
                break
            }

        }
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        currentYear = calendar[Calendar.YEAR]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.YEAR, currentYear)
        //   calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        for (i in 0..365) {
            calendar.add(Calendar.DATE, +1)
            list.add(calendar.time)
        }

        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun setTimeSlotAdapter() {
        val filteredTimelist = clubTimeList.filter {
            it.booking_allow
        }
        timeSlotAdapter = TimeSlotAdapter(requireContext(), this,
            filteredTimelist as ArrayList<ClubTime>, scheduleID)
        recycleTime.adapter = timeSlotAdapter
    }

    fun clicks() {
        popupSubmit.setSafeOnClickListener {
            //if (popupSubmit.text.equals(context?.getString(R.string.court_available))) {
            Log.e("clubData", Gson().toJson(clubData))
            Log.e("selectedDate", Gson().toJson(selectedDate))
            Log.e("selectedTime", Gson().toJson(selectedTime))
            Log.e("selectAvailableCate_Id", Gson().toJson(selectAvailableCate_Id))
            startActivity(
                Intent(requireContext(), GamesActivity::class.java)
                    .putExtra("from", "topay")
                    .putExtra("match_typee", "topay")
                    .putExtra("CourtData", Gson().toJson(clubData))
                    .putExtra("SelectedDate", selectedDate.toString())
                    .putExtra("SelectedTime", Gson().toJson(selectedTime))
                    .putExtra("selectedAvailableData", Gson().toJson(selectAvailableCate_Id))
                    .putExtra("CALL_FROM", CALL_FROM)
            )
            //  requireActivity().finish()
            //  }
        }
    }


    private fun setAdapter() {
        Log.e("ddd", Gson().toJson(courtDataList))
        adapter = CourtAdapter(requireContext(), courtDataList, this)
        recycleAvailable.adapter = adapter
    }

    override fun clickEvent(position: Int, time: String, clubTime: ClubTime) {
        selectedTime = clubTime


        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            callFrom = "TimeAdapterClick"
            courtDataVM?.courtListData(
                requireContext(), ClubID,
                dateFormatWithHyphanConversion(selectedDate.toString()), time
            )
        }

    }


    override fun onResume() {
        super.onResume()
        //   showToast("on reusme called court")
    }

    override fun onTimeClick(
        btnText: String,
        data: CategoryData,
        position: Int,
        courtData: CourtData
    ) {
        selectAvailableCate_Id = data
        clubData = courtData
        if (isBookingAllowed == false) {

            popupSubmit.isEnabled = false
            popupSubmit.isClickable = false

            popupSubmit.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_corner_grey
            )
            popupSubmit.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )

        } else {

            popupSubmit.isEnabled = true
            popupSubmit.isClickable = true
            //  popupSubmit.setText(btnText)
            popupSubmit.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.round_corner_blue)
            popupSubmit.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }


    }

    companion object {
        var callFrom = ""
    }
}