package snow.app.padelbook.bottomsheetdialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_court_type_bottom_sheet.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.CourtTypeBottomSheetAdapter
import snow.app.padelbook.common.CheckConnectivity
import snow.app.padelbook.common.Utils
import snow.app.padelbook.common.Utils.showInternetToast
import snow.app.padelbook.common.Utils.showToast
import snow.app.padelbook.fragments.SearchFragment
import snow.app.padelbook.fragments.SearchFragment.Companion.filter_type
import snow.app.padelbook.network.responses.searchResponse.searchCourt.CourtNameData
import snow.app.padelbook.utils.SafeClickListener
import snow.app.padelbook.viewModel.SearchCourtVM
import java.util.*
import kotlin.collections.ArrayList

class CourtTypeBottomSheet(val onBottomSheetCloseListener :OnBottomSheetCloseListener,var type : String) : BottomSheetDialogFragment(),
    CourtTypeBottomSheetAdapter.getCourtNameListener {

    private var adapter : CourtTypeBottomSheetAdapter ?= null
    private var searchCourtVM : SearchCourtVM?= null
    private var courtNameList : ArrayList<CourtNameData> = ArrayList()


    interface OnBottomSheetCloseListener{
        fun onBottomSheetClose()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_court_type_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchCourtVM = SearchCourtVM()
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation


        if(selectedTextId == 1){
            selectedText = getString(R.string.padel_courts)
            SearchFragment.type="0"
            selectedTextId == 1
            etSearchCourt.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_blue))
            etSearchCourt.setTextSize(15f)

            tvMatches.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_hint_color))
            tvMatches.setTextSize(12f)
        }
        else{
            selectedText = getString(R.string.menu_four)
            selectedTextId = 2
            SearchFragment.type="1"
            tvMatches.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_blue))
            tvMatches.setTextSize(15f)

            etSearchCourt.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_hint_color))
            etSearchCourt.setTextSize(12f)
        }

      //  callSearchApi()
      //  setSearchData()
        setClick()
        listener()


    }
    fun isNetworkConnected(): Boolean {
        return CheckConnectivity(requireContext()).isNetworkAvailable
    }

    private fun setSearchData() {
        etSearchCourt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        etSearchCourt.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, actionId : Int, p2: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(etSearchCourt.text.toString().isNotEmpty()){
                        if(isNetworkConnected()){
                            searchCourtVM?.searchCourtData(requireContext(),etSearchCourt.text.toString())
                        }
                        else{
                            showInternetToast()
                        }

                        return true
                    }

                }
                return false
            }

        })
    }

    private fun listener() {
        searchCourtVM?.updateTime?.observe(this, Observer { user ->
            if(user != null) {
                if (user.status) {
                    //  showToast(requireContext(),user.message)
                    dialog?.onBackPressed()
                    onBottomSheetCloseListener.onBottomSheetClose()
                } else {
                    showToast(requireContext(), user.message)
                }
            }
            else{
                showToast(requireContext(),getString(R.string.something_went_wrong))
            }
        })
    }


    private fun callSearchApi() {
        // <------------ call search court api ----------------->
        if(isNetworkConnected()){
            searchCourtVM?.searchCourtData(requireContext(),"")

        }
        else{
            showInternetToast()
        }
        // </----------------------------------------------------->
    }

    private fun setAdapter() {
        adapter = CourtTypeBottomSheetAdapter(requireContext(),courtNameList,this)
        searchList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.isFitToContents = false
        behavior.isDraggable = false
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        Log.d("onStart","onStart")

    }


    private fun setClick() {

        imgBack.setSafeOnClickListener {
            dialog?.onBackPressed()
            onBottomSheetCloseListener.onBottomSheetClose()
        }

        etSearchCourt.setOnClickListener {
            selectedText = etSearchCourt.text.toString()
            selectedTextId = 1
            etSearchCourt.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_blue))
            etSearchCourt.setTextSize(15f)

            tvMatches.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_hint_color))
            tvMatches.setTextSize(12f)

            if(isNetworkConnected()){
                searchCourtVM?.updateTimeData(requireContext(),selectedTextId.toString(),"","",type)
                filter_type = "1"
            }
            else{
                showToast(requireContext(),"No internet connection")
            }

        }

        tvMatches.setOnClickListener {
            selectedText = tvMatches.text.toString()
            selectedTextId = 2
            tvMatches.setTextColor(ContextCompat.getColor(requireContext(),R.color.theme_blue))
            tvMatches.setTextSize(15f)

            etSearchCourt.setTextColor(ContextCompat.getColor(requireContext(),R.color.text_hint_color))
            etSearchCourt.setTextSize(12f)
            if(isNetworkConnected()){
                searchCourtVM?.updateTimeData(requireContext(),selectedTextId.toString(),"","",type)
                filter_type = "1"
            }
            else{
                showToast(requireContext(),"No internet connection")
            }
        }

    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun getName(data: CourtNameData) {
        etSearchCourt.setText(data.name)
    }

    companion object{
        var selectedTextId = 1
        var selectedText = ""
    }

}