package snow.app.padelbook.bottomsheetdialog

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import kotlinx.android.synthetic.main.calendar_item.view.*
 import snow.app.padelbook.R
import snow.app.padelbook.adapter.TimeSlotBottomSheetAdapter
import snow.app.padelbook.common.CheckConnectivity
import snow.app.padelbook.common.Utils.showInternetToast
import snow.app.padelbook.common.Utils.showToast
import snow.app.padelbook.utils.SafeClickListener
import snow.app.padelbook.viewModel.SearchCourtVM
import java.text.SimpleDateFormat
import java.util.*
import com.aigestudio.wheelpicker.WheelAdapter


import com.aigestudio.wheelpicker.WheelPicker

import com.aigestudio.wheelpicker.WheelItem
import kotlinx.android.synthetic.main.fragment_time_slot_bottom_sheet.*
import snow.app.padelbook.fragments.SearchFragment
import snow.app.padelbook.fragments.SearchFragment.Companion.filter_type
import kotlinx.android.synthetic.main.fragment_time_slot_bottom_sheet.view.*


class TimeSlotBottomSheet(
    var selectedTextId: Int, val onBottomSheetCloseListener: OnBottomSheetCloseListener,
    var type: String
) : BottomSheetDialogFragment(),
    TimeSlotBottomSheetAdapter.TimeSlotInterface {

    private var mySelectionManager: CalendarSelectionManager? = null
    private var myCalendarChangesObserver: CalendarChangesObserver? = null
    private var myCalendarViewManager: CalendarViewManager? = null
    private var adapter: TimeSlotBottomSheetAdapter? = null
    private var singleRowCalendar: SingleRowCalendar? = null
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentYear = 0
    var isSelectedCurrentDate = true
    var selectedDate: Date? = null

    var timeList: ArrayList<String> = ArrayList()

    var getTimeVM: SearchCourtVM? = null
    var selectedTime = ""
    var AddFiveHoursTime = ""
    var savedDate = ""
    var isSelectedSavedDate = true
    var currentHour = ""
    var currentMin = ""
    var selectedHour = 0
    var mView: View? = null

    private val TIME_PICKER_INTERVAL = 30
    var layoutManager: LinearLayoutManager? = null

    interface OnBottomSheetCloseListener {
        fun timeSlotSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_time_slot_bottom_sheet, container, false)
        return mView
    }

    fun isNetworkConnected(): Boolean {
        return CheckConnectivity(requireContext()).isNetworkAvailable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation



        getTimeVM = SearchCourtVM()

        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]
        mView?.timePicker?.setIs24HourView(true)
        getCurrentTimeFromPicker()
        // <-----------------get time api ----------------->
        if (isNetworkConnected()) {
            getTimeVM?.getTimeData(requireContext())
        } else {
            showToast(requireContext(), "No Internet Connection")
        }

        //setAdapter()

        setClick()
        setCalenderCustom()

        //  setCalenderData()

        listener()


    }

    private fun getCurrentTimeFromPicker() {
        mView?.timePicker?.let { setTimePickerInterval(it) }
        if ( mView?.timePicker
                ?.currentMinute == 1) {
            currentHour =  mView?.timePicker?.currentHour.toString() + ":" + 30
            currentMin = mView?.timePicker?.currentHour?.let { addHoursToJavaUtilDate(it, 5).toString() } + ":" + 30

            Log.d("currentHour", currentHour)
            Log.d("currentMin", currentMin)
            var date = Calendar.getInstance().getTime()
            Log.d("currentDate", date.toString())

        } else {
            currentHour =
                mView?.timePicker?.currentHour.toString() + " : " +  mView?.timePicker?.currentMinute.toString()
            currentMin = mView?.timePicker?.let {
                addHoursToJavaUtilDate(
                    it.currentHour,
                    5
                ).toString()
            } + " : " +  mView?.timePicker?.currentMinute.toString()
        }


    }

    private fun setTimePicker(startTime: String, endTime: String) {
        if (startTime != null && endTime != null && selectedDate != null) {
            //  tvDateTime.setText(dateFormatWithMonthNameFull(selectedDate.toString())+ " | " +startTime+" - "+endTime)


            selectedDateTime =
                dateFormatWithMonthNameFull(selectedDate.toString()) + " | " + startTime + " - " + endTime
            var fullDate =
                dateFormatWithMonthNameFull(selectedDate.toString()) + " | " + startTime + " - " + endTime
            if (fullDate.contains("Mon")) {
                mView?.tvDateTime?.setText(fullDate.replace("Monday", getString(R.string.monday)))
            } else

                if (fullDate.contains("Tues")) {
                    mView?.tvDateTime?.setText(fullDate.replace("Tuesday", getString(R.string.tuesday)))
                } else
                    if (fullDate.contains("Wed")) {
                        mView?.tvDateTime?.setText(
                            fullDate.replace(
                                "Wednesday",
                                getString(R.string.wednes)
                            )
                        )
                    } else

                        if (fullDate.contains("Thurs")) {
                            mView?.tvDateTime?.setText(
                                fullDate.replace(
                                    "Thursday",
                                    getString(R.string.thurs)
                                )
                            )
                        } else

                            if (fullDate.contains("Frid")) {
                                mView?.tvDateTime?.setText(
                                    fullDate.replace(
                                        "Friday",
                                        getString(R.string.friday)
                                    )
                                )
                            } else
                                if (fullDate.contains("Satur")) {
                                    mView?.tvDateTime?.setText(
                                        fullDate.replace(
                                            "Saturday",
                                            getString(R.string.satu)
                                        )
                                    )
                                } else

                                    if (fullDate.contains("Sund")) {
                                        mView?.tvDateTime?.setText(
                                            fullDate.replace(
                                                "Sunday",
                                                getString(R.string.sunday)
                                            )
                                        )
                                    }


        } else {
            // tvDateTime.setText(dateFormatWithMonthNameFull(selectedDate.toString()) + " | " + startTime + " - " + endTime)

            //   tvDateTime.setText(selectedDateTime)

            var fullDate = selectedDateTime
            if (fullDate.contains("Mon")) {
                mView?.tvDateTime?.setText(fullDate.replace("Monday", getString(R.string.monday)))
            } else

                if (fullDate.contains("Tues")) {
                    mView?.tvDateTime?.setText(fullDate.replace("Tuesday", getString(R.string.tuesday)))
                } else
                    if (fullDate.contains("Wed")) {
                        mView?.tvDateTime?.setText(
                            fullDate.replace(
                                "Wednesday",
                                getString(R.string.wednes)
                            )
                        )
                    } else

                        if (fullDate.contains("Thurs")) {
                            mView?.tvDateTime?.setText(
                                fullDate.replace(
                                    "Thursday",
                                    getString(R.string.thurs)
                                )
                            )
                        } else

                            if (fullDate.contains("Frid")) {
                                mView?.tvDateTime?.setText(
                                    fullDate.replace(
                                        "Friday",
                                        getString(R.string.friday)
                                    )
                                )
                            } else
                                if (fullDate.contains("Satur")) {
                                    mView?.tvDateTime?.setText(
                                        fullDate.replace(
                                            "Saturday",
                                            getString(R.string.satu)
                                        )
                                    )
                                } else

                                    if (fullDate.contains("Sund")) {
                                        mView?.tvDateTime?.setText(
                                            fullDate.replace(
                                                "Sunday",
                                                getString(R.string.sunday)
                                            )
                                        )
                                    }


        }

        mView?.timePicker?.setIs24HourView(true)
        mView?.timePicker?.setOnTimeChangedListener(object :
            android.widget.TimePicker.OnTimeChangedListener {
            override fun onTimeChanged(p0: android.widget.TimePicker?, hour: Int, min: Int) {
                //setTimePickerInterval(timePicker)

                Log.d("time...", p0.toString())
                Log.d("time...", hour.toString())
                Log.d("time...", min.toString())

                selectedHour = hour

                updateDateTime(hour, min)

            }
        })

    }

    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val minutePicker = timePicker.findViewById(
                Resources.getSystem().getIdentifier(
                    "minute", "id", "android"
                )
            ) as NumberPicker

            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / TIME_PICKER_INTERVAL - 1

            val displayedValues: MutableList<String> = ArrayList()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.displayedValues = displayedValues.toTypedArray()
            minutePicker.setOnValueChangedListener { picker, oldVal, newVal ->
                Log.e("picker", "--" + picker)
                Log.e("oldVal", "--" + oldVal)
                Log.e("newVal", "--" + newVal)
                updateDateTime(selectedHour, newVal)

            }

        } catch (e: Exception) {
            Log.e("TAG", "Exception: $e")
        }
    }

    private fun updateDateTime(hour: Int, min: Int) {
        if (min == 1) {
            selectedTime = hour.toString() + ":" + 30
            AddFiveHoursTime =
                addHoursToJavaUtilDate(hour, 5).toString() + ":" + 30
            //tvDateTime.setText()


            var fullDate =
                dateFormatWithMonthNameFull(selectedDate.toString()) + " | " + selectedTime + " - " + AddFiveHoursTime
            if (fullDate.contains("Mon")) {
                mView?.tvDateTime?.setText(fullDate.replace("Monday", getString(R.string.monday)))
            } else

                if (fullDate.contains("Tues")) {
                    mView?.tvDateTime?.setText(fullDate.replace("Tuesday", getString(R.string.tuesday)))
                } else
                    if (fullDate.contains("Wed")) {
                        mView?.tvDateTime?.setText(
                            fullDate.replace(
                                "Wednesday",
                                getString(R.string.wednes)
                            )
                        )
                    } else

                        if (fullDate.contains("Thurs")) {
                            mView?.tvDateTime?.setText(
                                fullDate.replace(
                                    "Thursday",
                                    getString(R.string.thurs)
                                )
                            )
                        } else

                            if (fullDate.contains("Frid")) {
                                mView?.tvDateTime?.setText(
                                    fullDate.replace(
                                        "Friday",
                                        getString(R.string.friday)
                                    )
                                )
                            } else
                                if (fullDate.contains("Satur")) {
                                    mView?.tvDateTime?.setText(
                                        fullDate.replace(
                                            "Saturday",
                                            getString(R.string.satu)
                                        )
                                    )
                                } else

                                    if (fullDate.contains("Sund")) {
                                        mView?.tvDateTime?.setText(
                                            fullDate.replace(
                                                "Sunday",
                                                getString(R.string.sunday)
                                            )
                                        )
                                    }

            selectedDateTime =  mView?.tvDateTime?.text.toString()
        } else {
            selectedTime = hour.toString() + ":" + min.toString() + "0"
            AddFiveHoursTime =
                addHoursToJavaUtilDate(hour, 5).toString() + ":" + min.toString() + "0"


            var fullDate =
                dateFormatWithMonthNameFull(selectedDate.toString()) + " | " + selectedTime + " - " + AddFiveHoursTime
            if (fullDate.contains("Mon")) {
                mView?.tvDateTime?.setText(fullDate.replace("Monday", getString(R.string.monday)))
            } else

                if (fullDate.contains("Tues")) {
                    mView?.tvDateTime?.setText(fullDate.replace("Tuesday", getString(R.string.tuesday)))
                } else
                    if (fullDate.contains("Wed")) {
                        mView?.tvDateTime?.setText(
                            fullDate.replace(
                                "Wednesday",
                                getString(R.string.wednes)
                            )
                        )
                    } else

                        if (fullDate.contains("Thurs")) {
                            mView?.tvDateTime?.setText(
                                fullDate.replace(
                                    "Thursday",
                                    getString(R.string.thurs)
                                )
                            )
                        } else

                            if (fullDate.contains("Frid")) {
                                mView?.tvDateTime?.setText(
                                    fullDate.replace(
                                        "Friday",
                                        getString(R.string.friday)
                                    )
                                )
                            } else
                                if (fullDate.contains("Satur")) {
                                    mView?.tvDateTime?.setText(
                                        fullDate.replace(
                                            "Saturday",
                                            getString(R.string.satu)
                                        )
                                    )
                                } else

                                    if (fullDate.contains("Sund")) {
                                        mView?.tvDateTime?.setText(
                                            fullDate.replace(
                                                "Sunday",
                                                getString(R.string.sunday)
                                            )
                                        )
                                    }


            //  tvDateTime.setText(dateFormatWithMonthNameFull(selectedDate.toString()) + " | " + selectedTime + " - " + AddFiveHoursTime)
            selectedDateTime =  mView?.tvDateTime?.text.toString()
        }


    }

    fun addHoursToJavaUtilDate(hours: Int, add_hours: Int): Int {
        var sum = hours + add_hours
        if (sum >= 24) {
            sum -= 24
            return sum
        } else {
            return sum
        }
    }

    private fun listener() {

        getTimeVM?.getTime?.observe(requireActivity(), androidx.lifecycle.Observer { user ->
            var startTime = -1
            var endTime = -1
            if (user != null) {
                if (user.status) {
                    //   showToast(requireContext(), user.message)

                    // <------- for setting start time (min and hour) to Time picker
                    if (user.data.time != null) {
                        val separated: List<String> = user.data.time.split(":")
                        startTime = separated[0].toInt()
                        endTime = separated[1].toInt()
                    } else {
                        val separated: List<String> = currentHour.split(":")
                        startTime = separated[0].toInt()
                        endTime = separated[1].toInt()
                    }
                    // <----------------------------------------------- >

                    //<--------------for getting start time and end time ---->
                    if (user.data.time != null && user.data.end_time != null) {
                        currentHour = user.data.time
                        currentMin = user.data.end_time
                    }


                    // <--------------- if date null then set the cureent date --->
                    if (user.data.date != null) {

                        savedDate = user.data.date
                        Log.d("startTimeDate", savedDate)
                        selectedDate = convertHyphanWithFullName(savedDate)

                        selectedDateTime = dateFormatWithMonthNameFull(selectedDate.toString())

                    } else {
                        var date = Calendar.getInstance().getTime()
                        Log.d("date", date.toString())
                        savedDate = dateFormatWithHyphanConversion(date.toString())
                        selectedDate = date

                        selectedDateTime = dateFormatWithMonthNameFull(date.toString())
                    }
                    // <----------------------------------------------------->
                    setTimePicker(currentHour, currentMin)

                    singleRowCalendar =  mView?.horizontalCalenderTime?.apply {
                        calendarViewManager = myCalendarViewManager!!
                        calendarChangesObserver = myCalendarChangesObserver!!
                        calendarSelectionManager = mySelectionManager!!
                        futureDaysCount = 365
                        includeCurrentDate = true
                        setDates(getFutureDatesOfCurrentMonth())
                        init()
                    }


                    if (!savedDate.isNullOrEmpty()) {
                        // < ----------- handle selection of date that we get from getTime api -------->
                        for (i in 0..getFutureDatesOfCurrentMonth().size - 1) {
                            Log.d("selectedArea", getFutureDatesOfCurrentMonth()[i].toString())

                            if (dateFormatWithHyphanConversion(getFutureDatesOfCurrentMonth()[i].toString()).equals(
                                    savedDate
                                )
                            ) {
                                Log.d("savedDate...", getFutureDatesOfCurrentMonth()[i].toString())
                                Log.d("savedDate...", savedDate)
                                selectedDate = getFutureDatesOfCurrentMonth()[i]
                                singleRowCalendar!!.select(i)
                                break
                            }

                        }
                    }
                    if (startTime != -1) {
                        timePicker.setHour(startTime).toString()
                        timePicker.setMinute(endTime).toString()
                    }

                    // setAdapter()
                } else {
                    showToast(requireContext(), user.message)
                }
            } else {
                showToast(requireContext(), getString(R.string.something_went_wrong))
            }
        })

        getTimeVM?.updateTime?.observe(requireActivity(), androidx.lifecycle.Observer { user ->
            if (user.status) {
                showToast(requireContext(), user.message)
                dialog?.onBackPressed()
                onBottomSheetCloseListener.timeSlotSheet()
            } else {
                showToast(requireContext(), user.message)
            }
        })
    }


    private fun setCalenderCustom() {
        myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date, isSelected: Boolean
            ): Int {

//                var tempType : Boolean = isSelected
//                var t = ""
//                Log.e("isSelected",tempType.toString())
//                val cal = Calendar.getInstance()
//                // cal.time = date
//
//                Log.d("currentTime.....", date.toString())
//                Log.d("CalenderDate", cal.time.toString())
//
//                if (dateFormatWithHyphanConversion(date.toString()).equals(
//                        dateFormatWithHyphanConversion(cal.time.toString() )))
//                        {
//                   //  Log.e("checkTwo", "if")
//                    if(isSelectedCurrentDate) {
//                        tempType = true
//                        t = "1"
//
//                    }
//                    else{
//                        // tempType = false
//                    }

                //              }


//                if (tempType) {
//                    // set the current date
                //   selectedDate = date
//
//
//                }

                return if (isSelected) {
//                    if(t.equals("1")) {
//                        R.layout.current_calendar_item
//                    }
//                    else if(t.equals("2")) {
//                        R.layout.selected_calendar_item
//                    }
//                    else {
                    R.layout.selected_calendar_item
                    //       }

                } else {
                    R.layout.calendar_item
                }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder, date: Date,
                position: Int, isSelected: Boolean
            ) {

                holder.itemView.tvDate.text = DateUtils.getDayNumber(date)
                holder.itemView.tvWeek.text = DateUtils.getDay3LettersName(date)
                holder.itemView.tvMonthName.text = DateUtils.getMonth3LettersName(date)
                // bind data to calendar item views

            }
        }

        myCalendarChangesObserver = object : CalendarChangesObserver {
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

        mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // return true if item can be selected
                /* val cal = Calendar.getInstance()
                 cal.time = date*/
                selectedDate = selectedDate(date)

                //tvDateTime.setText(dateFormatWithMonthNameFull(date.toString())+ " | " +selectedTime+" - "+AddFiveHoursTime)
                selectedDateTime =
                    dateFormatWithMonthNameFull(date.toString()) + " | " + selectedTime + " - " + AddFiveHoursTime
                savedDate = ""
                var fullDate =
                    dateFormatWithMonthNameFull(date.toString()) + " | " + selectedTime + " - " + AddFiveHoursTime
                if (fullDate.contains("Mon")) {
                    tvDateTime.setText(fullDate.replace("Monday", getString(R.string.monday)))
                } else

                    if (fullDate.contains("Tues")) {
                        tvDateTime.setText(fullDate.replace("Tuesday", getString(R.string.tuesday)))
                    } else
                        if (fullDate.contains("Wed")) {
                            tvDateTime.setText(
                                fullDate.replace(
                                    "Wednesday",
                                    getString(R.string.wednes)
                                )
                            )
                        } else

                            if (fullDate.contains("Thurs")) {
                                tvDateTime.setText(
                                    fullDate.replace(
                                        "Thursday",
                                        getString(R.string.thurs)
                                    )
                                )
                            } else

                                if (fullDate.contains("Frid")) {
                                    tvDateTime.setText(
                                        fullDate.replace(
                                            "Friday",
                                            getString(R.string.friday)
                                        )
                                    )
                                } else
                                    if (fullDate.contains("Satur")) {
                                        tvDateTime.setText(
                                            fullDate.replace(
                                                "Saturday",
                                                getString(R.string.satu)
                                            )
                                        )
                                    } else

                                        if (fullDate.contains("Sund")) {
                                            tvDateTime.setText(
                                                fullDate.replace(
                                                    "Sunday",
                                                    getString(R.string.sunday)
                                                )
                                            )
                                        }


                return true
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
        list.add(calendar.time)
        for (i in 0..365) {
            calendar.add(Calendar.DATE, +1)
            list.add(calendar.time)
        }

        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun setClick() {
        imgBack.setSafeOnClickListener {
            dialog?.onBackPressed()
            onBottomSheetCloseListener.timeSlotSheet()
        }
        popupSubmit.setOnClickListener {
            if (!isNetworkConnected()) {
                showInternetToast()
            } else {

                getTimeVM?.updateTimeData(
                    requireContext(), selectedTextId.toString(),
                    dateFormatWithHyphanConversion(selectedDate.toString()), selectedTime, type
                )
                filter_type = "1"
            }
        }
    }

    fun dateFormatWithHyphanConversion(date: String): String {
        val myFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(date)
        var myDate = SimpleDateFormat("yyyy-MM-dd").format(myFormat)
        return myDate
    }

    fun convertHyphanWithFullName(date: String): Date {
        val myFormat = SimpleDateFormat("yyyy-MM-dd").parse(date)
        var myDate = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").format(myFormat)
        return SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(myDate)
    }

    fun dateFormatWithMonthNameFull(date: String): String {
        val myFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy").parse(date)
        var myDate = SimpleDateFormat("EEEE, dd/MM").format(myFormat)
        return myDate
    }

    fun selectedDate(date: Date): Date {
        val myDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        val myFormat = SimpleDateFormat("yyyy-MM-dd").parse(myDate)

        return myFormat
    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.isFitToContents = false
        behavior.isDraggable = false
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        Log.d("onStart", "onStart")
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun timeSLotClick(time: String, position: Int, itemView: View) {
        //  selectedTime = time
        //   scrollToCenter(itemView)
    }


    companion object {
        var selectedDateTime = ""
    }
}