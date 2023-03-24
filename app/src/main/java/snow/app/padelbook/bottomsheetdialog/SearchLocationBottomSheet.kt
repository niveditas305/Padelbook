package snow.app.padelbook.bottomsheetdialog

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_search_location_bottom_sheet.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.LocationAdapter
import snow.app.padelbook.common.CheckConnectivity
import snow.app.padelbook.common.Utils.showToast
import snow.app.padelbook.fragments.SearchFragment.Companion.filter_type
import snow.app.padelbook.network.responses.searchResponse.searchNearBy.recentHistory.RecentData
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.utils.MyApplication
import snow.app.padelbook.utils.SafeClickListener
import snow.app.padelbook.viewModel.SearchCourtVM
import java.util.*
import kotlin.collections.ArrayList

class SearchLocationBottomSheet(val bottomSheetClose: OnBottomSheetCloseListener,
                                var mType: String) : BottomSheetDialogFragment(),
    LocationAdapter.onLocationClick {

    private var recentSearch : SearchCourtVM ?= null
    private var adapter : LocationAdapter ?= null
    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    var lattitue : String ?= null
    var longitude : String ?= null
    var recentList : ArrayList<RecentData> = ArrayList()
    var loginPref : SessionClass ?= null

    interface OnBottomSheetCloseListener{
        fun onBottomSheetCloseListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_location_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recentSearch = SearchCourtVM()
        loginPref = SessionClass(requireContext())
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation



        if(isNetworkConnected()){
            recentSearch?.recentSearchData(requireContext())
        }
        else{
            showToast(requireContext(),"No internet Connection")
        }

        initAddressPicker()
        setClick()
        listener()
    }

    private fun listener() {
        recentSearch?.recentSearch?.observe(this, androidx.lifecycle.Observer { user ->
           if(user != null) {
               if (user.status) {
                   if(user.data.size > 0) {

                           loginPref?.recentData = user.data[0]
                           Log.d("recentData----", Gson().toJson(loginPref?.recentData))

                           recentList.clear()
                           recentList.addAll(user.data)
                           setAdapter()
                           // showToast(requireContext(),user.message)

                   }
               } else {
                   showToast(requireContext(), user.message)
               }
           }
            else{
                showToast(requireContext(),getString(R.string.something_went_wrong))
            }
        })


        // < ------------------ search address ----------------------->
        recentSearch?.searchAddressAdd?.observe(this, androidx.lifecycle.Observer { user ->
            if(user != null) {
                if (user.status) {
                    showToast(requireContext(), user.message)
                    if (isNetworkConnected()) {
                        recentSearch?.recentSearchData(requireContext())
                        dialog?.onBackPressed()
                        bottomSheetClose.onBottomSheetCloseListener()
                    } else {
                        showToast(requireContext(), "No internet Connection")
                    }



                } else {
                    showToast(requireContext(), user.message)
                }
            }
            else{
                showToast(requireContext(),getString(R.string.something_went_wrong))
            }
        })
    }

    fun isNetworkConnected(): Boolean {
        return CheckConnectivity(MyApplication.appInstance.applicationContext).isNetworkAvailable
    }

    private fun setClick() {
        imgBack.setSafeOnClickListener {
            dialog?.onBackPressed()
            bottomSheetClose.onBottomSheetCloseListener()
        }
        location.setOnClickListener {
            openPlacePicker()
        }
    }

    private fun openPlacePicker() {
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            Activity.RESULT_OK -> {

                        data.let {
                            val place  = Autocomplete.getPlaceFromIntent(data!!)
                            var addressData = place.name
                            location?.setText(place.name)

                            Log.d("Place", "PlaceComponent: ${place}, ${place.id}")
                            Log.d("Place", "latlng"+place.latLng?.latitude+","+place.latLng?.longitude)
                            Log.d("addressData", addressData!!)

                            lattitue = place.latLng!!.latitude.toString()
                            longitude = place.latLng!!.longitude.toString()
                            // get the required fields using geocoder
                            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
                            var address = geoCoder.getFromLocation(place.latLng!!.latitude,place.latLng!!.longitude,1)

                            Log.d("AddressDetails",address[0].toString())

                            if(isNetworkConnected()){
                                   recentSearch?.searchDataAdded(requireContext(),addressData,
                                   lattitue!!,longitude!!,"",mType)
                                   filter_type = "1"
                            }
                            else {
                                showToast(requireContext(),"No internet Connection")
                            }

                        }
                    }

            AutocompleteActivity.RESULT_ERROR -> {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    //  Log.i(TAG, status.statusMessage)
                }
            }
            Activity.RESULT_CANCELED -> {
                // The user canceled the operation.
                Log.i("TAG", "cancel")
            }
        }
    }

    fun initAddressPicker() {
        // initialize the address picker
        Places.initialize(requireContext(), getString(R.string.google_api_key_padelbook))
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.isFitToContents = false
        behavior.isDraggable = false
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        Log.d("onStart","onStart")
    }

    private fun setAdapter() {
        adapter = LocationAdapter(requireContext(),recentList,this)
        recycleLoc.adapter = adapter
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    companion object{
        var selectedAddress = ""
    }

    override fun onClick(recentData: RecentData) {
        loginPref?.recentData = recentData
        Log.d("recentDataTwo----",Gson().toJson(loginPref?.recentData ))
        Log.d("recentDataTwo----",Gson().toJson(recentData))

        location.setText(recentData.address)
        lattitue = recentData.latitude
        longitude = recentData.longitude
        selectedAddress = recentData.address
        if(isNetworkConnected()){
            recentSearch?.searchDataAdded(requireContext(),recentData.address,
                recentData.latitude!!,recentData.longitude!!,"",mType)
            filter_type = "1"
        }
        else{
            showToast(requireContext(),"No internet Connection")
        }


    }

}