package snow.app.padelbook.fragments

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_match.*
import kotlinx.android.synthetic.main.fragment_match.main_single_row_calendar
import java.util.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.HomeAdapterSecond
import snow.app.padelbook.listener.ClickEvent
import snow.app.padelbook.network.responses.bookingResponse.BookingData
import snow.app.padelbook.network.responses.homeList.Match
import snow.app.padelbook.ui.GamesActivity
import snow.app.padelbook.ui.HomeActivity
import snow.app.padelbook.ui.HomeActivity.Companion.lastSelectedTab
import snow.app.padelbook.utils.SafeClickListener
import snow.app.padelbook.viewModel.CourtListVM
import kotlin.collections.ArrayList


class MatchFragment(var clubID : String) : BaseFragment(), ClickEvent {
    private var adapterTwo: HomeAdapterSecond? = null
    private var matchListVM : CourtListVM? = null

    private var singleRowCalendar: SingleRowCalendar? = null
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentYear = 0
    var isSelectedCurrentDate = true
    private var mContext: Context? = null
    var selectedDate: Date? = null

    var matchList : ArrayList<Match> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        matchListVM = CourtListVM()

        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        setClick()
        setCalenderCustom()
        listener()
        setAdapter()
    }

    private fun listener() {
        matchListVM?.userMatchList?.observe(requireActivity(), androidx.lifecycle.Observer { user ->
            if(user != null){
                if(user.status){
                    matchList.clear()
                    if(user.data.isNotEmpty()){
                        matchList.addAll(user.data)
                        adapterTwo?.notifyDataSetChanged()
                        if(matchList.size == 0){
                            llNoResultsFound.visibility = View.VISIBLE
                            recycleProfile.visibility = View.GONE
                        }else{
                            llNoResultsFound.visibility = View.GONE
                            recycleProfile.visibility = View.VISIBLE
                        }
                    }else{
                        matchList.clear()
                        adapterTwo?.notifyDataSetChanged()
                        llNoResultsFound.visibility = View.VISIBLE
                        recycleProfile.visibility = View.GONE
                    }

                }
                else{
                    showToast(user.message)
                }
            }
            else{
                showToast(requireContext().getString(R.string.something_went_wrong))
            }

        })
    }


    private fun setCalenderCustom() {
     /*   val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                var tempType: Boolean = isSelected
                var t = ""
                Log.e("isSelected", tempType.toString())

                val cal = Calendar.getInstance()
                // cal.time = date

                if (dateFormatWithHyphanConversion(date.toString()).equals(dateFormatWithHyphanConversion(cal.time.toString()))) {
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

                    Log.d("SELECTEDDATE",selectedDate.toString())

                    if (!isNetworkConnected()) {
                        showInternetToast()
                    } else {
                        matchListVM?.matchListData(requireContext(),clubID,dateFormatWithHyphanConversion(selectedDate.toString()))
                    }
                }

                return if (tempType) {
                    if (t.equals("1")) {
                        R.layout.current_calendar_item
                    } else {
                        R.layout.selected_calendar_item
                    }
                } else
                    R.layout.calendar_item

            }
*/
        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date, isSelected: Boolean
            ): Int {

                var tempType:Boolean=isSelected
                var t=""
                Log.e("isSelected",tempType.toString())

                val cal = Calendar.getInstance()
                // cal.time = date

                Log.d("currentTime.....", date.toString())

                Log.d("CalenderDate", cal.time.toString())

                Log.e("SELECTEDDATE","---"+date)
                if (dateFormatWithHyphanConversion(date.toString()).equals(
                        dateFormatWithHyphanConversion(
                            cal.time.toString()
                        )
                    )
                ) {
                    // Log.e("check", "if")
                    if(isSelectedCurrentDate) {
                        tempType = true
                        t="1"
                        //singleRowCalendar!!.deselect(position)
                    }
                    else{
                        // tempType = false
                    }

                }else{
                    Log.e("SELECTEDDATE","---"+date)
                }


                if (tempType) {
                    selectedDate = date
                    if (!isNetworkConnected()) {
                        showInternetToast()
                    } else {
                        matchListVM?.matchListData(requireContext(),clubID,dateFormatWithHyphanConversion(selectedDate.toString()))
                    }
                }

                return if (isSelected) {
                    /* if(t.equals("1"))
                     { R.layout.current_calendar_item}
                     else {*/
                    R.layout.selected_calendar_item
                    //     }
                }
                else {
                    R.layout.calendar_item
                }
            }
            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
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
            }

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)

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
                // return true if item can be selected
                val cal = Calendar.getInstance()
                cal.time = date
                selectedDate = date
                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    matchListVM?.matchListData(requireContext(),clubID,dateFormatWithHyphanConversion(selectedDate.toString()))
                }
                return true

            }
        }
        singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            futureDaysCount = 365

            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        var date =  Calendar.getInstance().getTime()
        Log.d("date",date.toString())

        for (i in 0..getFutureDatesOfCurrentMonth().size - 1) {
            Log.e("selectedArea", dateFormatWithHyphanConversion(getFutureDatesOfCurrentMonth()[i].toString()))
            Log.e("cu", dateFormatWithHyphanConversion(date.toString()))

            if (dateFormatWithHyphanConversion(getFutureDatesOfCurrentMonth()[i].toString()).equals(dateFormatWithHyphanConversion(date.toString()))) {

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
      //  calendar.set(Calendar.MONTH, currentMonth)
        //  calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        for (i in 0..365) {
            calendar.add(Calendar.DATE, +1)
            list.add(calendar.time)
        }

        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun setClick() {
        bottomText.setSafeOnClickListener {
            lastSelectedTab = 2
            startActivity(Intent(requireContext(), HomeActivity::class.java))
        }
    }

    private fun setAdapter() {
        adapterTwo = HomeAdapterSecond(requireContext(), this,"",matchList)
        recycleProfile.adapter = adapterTwo
    }

    override fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun clickEvent(matchId: String, playerKey: String, courtFeature: String) {
        startActivity(
            Intent(requireContext(), GamesActivity::class.java)
                .putExtra("callFrom", "Home")
                .putExtra("matchId", matchId)
                .putExtra("playerKey", playerKey)

                .putExtra("courtFeatureType", courtFeature)
        )
    }

    override fun clickEventForBooking(position: Int) {
    }

    override fun clickForBookingDetail(matchData: BookingData) {
    }

}