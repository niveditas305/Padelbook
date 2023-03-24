package snow.app.padelbook.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.toolbar_three.view.*
import kotlinx.android.synthetic.main.toolbar_three.view.arc_progress
import kotlinx.android.synthetic.main.toolbar_three.view.profile_image
import snow.app.padelbook.R
import snow.app.padelbook.adapter.SearchAdapter
import snow.app.padelbook.adapter.SearchMatchesAdapter
import snow.app.padelbook.bottomsheetdialog.CourtTypeBottomSheet
import snow.app.padelbook.bottomsheetdialog.CourtTypeBottomSheet.Companion.selectedText
import snow.app.padelbook.bottomsheetdialog.CourtTypeBottomSheet.Companion.selectedTextId
import snow.app.padelbook.bottomsheetdialog.SearchLocationBottomSheet
import snow.app.padelbook.bottomsheetdialog.TimeSlotBottomSheet
import snow.app.padelbook.databinding.FragmentSearchBinding
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.clubList.ClubData
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.viewModel.ClubListVM
import snow.app.padelbook.viewModel.LikeUnlikeVM
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.CameraUpdateFactory


import com.google.android.gms.maps.SupportMapFragment

import com.google.android.gms.tasks.Task
 import kotlinx.android.synthetic.main.toolbar_three.view.ivBack
import snow.app.padelbook.common.IMAGES_URL
import snow.app.padelbook.listener.ClickEvent
import snow.app.padelbook.network.responses.bookingResponse.BookingData
import snow.app.padelbook.network.responses.clubListNew.Matchlist
import snow.app.padelbook.ui.GamesActivity
import snow.app.padelbook.ui.SettingActivity


class SearchFragment : BaseFragment(), OnMapReadyCallback, FavouriteListener, ClickEvent,
    CourtTypeBottomSheet.OnBottomSheetCloseListener,
    SearchLocationBottomSheet.OnBottomSheetCloseListener,
    TimeSlotBottomSheet.OnBottomSheetCloseListener {

    private var mapf: SupportMapFragment? = null
    lateinit var binding: FragmentSearchBinding
    private var clubListVM: ClubListVM? = null
    private var likeunlikeVm: LikeUnlikeVM? = null
    private var adapter: SearchAdapter? = null

    var googleMap: GoogleMap? = null


    var loginPref: SessionClass? = null
    val list: ArrayList<String> = ArrayList()
    val arrayClub: ArrayList<ClubData> = ArrayList()
    val matchData: ArrayList<Matchlist> = ArrayList()
    val PERMISSION_ID = 42
    var latitude: String? = null
    var longitude: String? = null
    var courtId: Int? = null


    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private var adapterMatches: SearchMatchesAdapter? = null


    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var REQUEST_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Log.d("onCreateView", "onCreateView")

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        initMap(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation();

        return binding.root
    }

    private fun getCurrentLocationNew() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        // getCurrentLocation()
        locationRequest = LocationRequest().apply {
            // Sets the desired interval for
            // active location updates.
            // This interval is inexact.
            interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates.
            // This interval is exact, and your application will never
            // receive updates more frequently than this value
            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location
            // updates are delivered. Updates may be
            // delivered sooner than this interval
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult?.lastLocation?.let {
                    currentLocation = it
                    Log.d("locatiomnn", " " + it)
                    //  if(!lattitue.equals("")) {
                    //  }
                    // use latitude and longitude as per your need
                } ?: {
                    Log.d("dd", "Location information isn't available.")
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        // fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    override fun onAttach(context: Activity) {
        super.onAttach(context)
        Log.d("onAttach", "onAttach")
        mcontext = context
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //  binding.mapFragment.onSaveInstanceState(outState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.clear()
        loginPref = SessionClass(requireContext())


        clubListVM = ClubListVM()
        likeunlikeVm = LikeUnlikeVM()
        //getCurrentLocationNew()
        loginPref?.loginData?.image_file?.let {


            if (loginPref?.loginData?.image_file!!.startsWith("http")) {
                Glide.with(requireActivity())
                    .load(loginPref?.loginData?.image_file)             // image url
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)  // any image in case of error
                    .into(binding.toolbar.profile_image)
            } else {
                Glide.with(requireActivity())
                    .load(IMAGES_URL + loginPref?.loginData?.image_file)             // image url
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)  // any image in case of error
                    .into(binding.toolbar.profile_image)
            }

        } ?: kotlin.run {
            Glide.with(requireActivity())
                .load(R.drawable.ic_user)             // image url
                .placeholder(R.drawable.ic_user) // any placeholder to load at start
                .error(R.drawable.ic_user)  // any image in case of error
                .into(binding.toolbar.profile_image)
        }


        filter_type = "0"

        //<----------call api get Club Listing ---------------->

        //</------------------------------------------>

        setToolbar()
        setClick()
        observer()

    }

    override fun onResume() {
        // binding.mapFragment.onResume()
        super.onResume()
        if (isNetworkConnected()) {
            clubListVM?.clubListData(mcontext!!, filter_type)
        } else {
            showInternetToast()
        }
    }

    fun dateFormatWithMonthName(date: String): String {
        val myFormat = SimpleDateFormat("yyyy-MM-dd").parse(date)
        var myDate = SimpleDateFormat("EEEE, dd/MM").format(myFormat)
        return myDate
    }


    private fun observer() {

        //<---------------- get List Api response ------------------->
        clubListVM?.clubList?.observe(requireActivity(), Observer { user ->

            if (user != null) {
                if (user.status) {
                    if (user.data != null) {

                        if (user.court_id != null) {
                            selectedTextId = user.court_id.toInt()
                            courtId = user.court_id.toInt()
                            if (user.court_id == "1") {
                                if (mcontext != null) {
                                    binding.spinnerfirst.setText(mcontext!!.getString(R.string.padel_courts))
                                    arrayClub.clear()
                                    type="0"
                                    arrayClub.addAll(user.data)
                                    setRecyclePadelCourtAdapter()

                                    if (arrayClub.size == 0){
                                        // showToast(getString(R.string.no_matches_found))
                                        binding.recycleSearch.visibility =View.GONE
                                        binding.llNoResultsFound.visibility =View.VISIBLE
                                        binding.tvEmptyText.text =getString(R.string.no_court_data_text)
                                    }else{
                                        binding.recycleSearch.visibility =View.VISIBLE
                                        binding.llNoResultsFound.visibility =View.GONE
                                    }
                                }

                            } else {
                                matchData.clear()
                                matchData.addAll(user.matchlist)
                                if (binding.spinnerfirst != null) {
                                    type="1"
                                    binding.spinnerfirst.setText(getString(R.string.menu_four))
                                }
                                if (matchData.size == 0){
                                   // showToast(getString(R.string.no_matches_found))
                                    binding.recycleSearch.visibility =View.GONE
                                    binding.llNoResultsFound.visibility =View.VISIBLE
                                    binding.tvEmptyText.text = getString(R.string._no_match_text)
                                }else{
                                    binding.recycleSearch.visibility =View.VISIBLE
                                    binding.llNoResultsFound.visibility =View.GONE
                                }
                                setRecycleMatchesAdapter()

                            }

                            if (user.address != null && user.address.isNotEmpty()) {
                                loginPref?.recentData?.address = user.address
                                loginPref?.recentData?.longitude = user.longitude
                                loginPref?.recentData?.latitude = user.latitude
                                binding.spinnerDataTwo.setText(user.address)
                            } else {
                                binding.spinnerDataTwo.setText(mcontext?.getString(R.string.nearby))
                            }

                            if (user.date != null && user.time != null && user.date.isNotEmpty() && user.time.isNotEmpty()) {
                                binding.spinnerDataThree.setText(dateFormatWithMonthName(user.date) + " | " + user.time)
                                selectedDateFromFilter = user.date
                            } else {
                                binding.spinnerDataThree.setText(
                                    dateFormatWithMonthNameFull(
                                        Calendar.getInstance().getTime().toString()
                                    )
                                )
                                //  binding.spinnerDataThree.setText(selectedDateTime)

                                Log.e("get current t", "--")

                            }

                        }



                        if (googleMap != null) {
                            if (user.court_id.equals("1")) {
                                for (i in 0 until arrayClub.size) {
                                    createMarker(
                                        arrayClub.get(i).latitude.toDouble(),
                                        arrayClub[i].longitude.toDouble(),
                                        arrayClub[i].club_name,
                                        arrayClub[i].club_name
                                    )
                                }
                            } else {
                                for (i in 0 until matchData.size) {
                                    createMarker(
                                        matchData.get(i).latitude.toDouble(),
                                        matchData[i].longitude.toDouble(),
                                        matchData[i].club_name,
                                        matchData[i].club_name
                                    )
                                }
                            }

                        }
                        //setRecyclePadelCourtAdapter()
                    }
                    //  showToast(user.message)

                } else {
                    showToast(user.message)
                }
            } else {
                showToast(mcontext!!.getString(R.string.something_went_wrong))
            }
        })
        // </---------------------------------------------------->


        // <------------------------ fav/Unfav api response ----->
        likeunlikeVm?.likeUnlike?.observe(requireActivity(), Observer { user ->
            if (user != null) {
                if (user.status) {
                    showToast(user.message)
                    if (isNetworkConnected()) {
                        clubListVM?.clubListData(mcontext!!, filter_type)
                    } else {
                        showInternetToast()
                    }
                } else {
                    showToast(user.message)
                }
            } else {
                showToast("Something went wrong")
            }
        })
        // <---------------------------------------------------->


    }

    private fun setRecyclePadelCourtAdapter() {
        if (mcontext != null) {
            adapter = SearchAdapter(mcontext!!, arrayClub, this)
            binding.recycleSearch.adapter = adapter
        }
    }


    private fun setRecycleMatchesAdapter() {
        if (mcontext != null) {
            adapterMatches = SearchMatchesAdapter(mcontext!!, this, "", matchData)
            binding.recycleSearch.adapter = adapterMatches
        }
    }


    fun initMap(savedInstanceState: Bundle?) {
        mapf = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapf!!.getMapAsync(this)

        /*    binding.mapFragment.onCreate(savedInstanceState)
            binding.mapFragment.onResume()
            binding.mapFragment.getMapAsync(this)




           binding.mapFragment.setOnTouchListener(OnTouchListener { v, event ->
               val action = event.action
               when (action) {
                   MotionEvent.ACTION_DOWN -> {
                       // Disallow ScrollView to intercept touch events.
                       binding.nested.requestDisallowInterceptTouchEvent(true)
                       binding.nested.arrowScroll(0)
                       // Disable touch on transparent view
                       false
                   }
                   MotionEvent.ACTION_UP -> {
                       // Allow ScrollView to intercept touch events.
                       binding.nested.requestDisallowInterceptTouchEvent(false)
                       binding.nested.arrowScroll(0)
                       true
                   }
                   MotionEvent.ACTION_MOVE -> {
                       binding.nested.requestDisallowInterceptTouchEvent(true)
                       binding.nested.arrowScroll(0)
                       false
                   }
                   else -> true
               }
           })*/
    }


    private fun setClick() {
        mapf!!.requireView().visibility = View.GONE
        binding.spinnerfirst.setSafeOnClickListener {
            val bottomSheet = CourtTypeBottomSheet(this,"0" )
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }
     /*   binding.toolbar.profile_image.setSafeOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }*/

        binding.filterImg.setSafeOnClickListener {
            val bottomSheet = FilterFragment(selectedTextId)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
            bottomSheet.onSubmitClick = {
                 bottomSheet.dismiss()
                if (isNetworkConnected()) {
                    clubListVM?.clubListData(mcontext!!, filter_type)
                } else {
                    showInternetToast()
                }
            }

        }

        binding.spinnerDataTwo.setSafeOnClickListener {
            val bottomSheetLocation = SearchLocationBottomSheet(this, "0")
            bottomSheetLocation.show(childFragmentManager, bottomSheetLocation.tag)
        }

        binding.spinnerDataThree.setSafeOnClickListener {
            val bottomSheetTimeSlot = TimeSlotBottomSheet(selectedTextId, this, "0")
            bottomSheetTimeSlot.show(childFragmentManager, bottomSheetTimeSlot.tag)
        }

        binding.mapImage.setSafeOnClickListener {
            binding.ivList.visibility = View.VISIBLE
            binding.mapImage.visibility = View.GONE
            binding.recycleSearch.visibility = View.GONE
            mapf!!.requireView().visibility = View.VISIBLE


            if (googleMap != null && currentLocation != null) {
                val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            }
        }

        binding.ivList.setSafeOnClickListener {
            binding.ivList.visibility = View.GONE
            binding.mapImage.visibility = View.VISIBLE
            binding.recycleSearch.visibility = View.VISIBLE
            mapf!!.requireView().visibility = View.GONE
        }

    }

    private fun setToolbar() {
        binding.toolbar.ivBack.visibility = View.GONE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.setText(getString(R.string.search))
        binding.toolbar.separator.visibility = View.VISIBLE
        binding.toolbar.arc_progress.visibility = View.GONE
        binding.toolbar.profile_image.visibility = View.GONE
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener(OnSuccessListener<Location?> { location ->
            if (location != null) {
                currentLocation = location
                Log.e("ccc loca", "" + currentLocation)

            }
        })
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        googleMap?.isMyLocationEnabled = true
        googleMap?.getUiSettings()!!.isZoomGesturesEnabled = true
        googleMap?.getUiSettings()!!.setZoomControlsEnabled(true);

        if (courtId == 1) {
            if (googleMap != null && arrayClub != null) {
                for (i in 0 until arrayClub.size) {
                    createMarker(
                        arrayClub.get(i).latitude.toDouble(),
                        arrayClub[i].longitude.toDouble(),
                        arrayClub[i].club_name,
                        arrayClub[i].club_name
                    )
                }
            }
        } else {
            if (googleMap != null && matchData != null) {
                for (i in 0 until matchData.size) {
                    createMarker(
                        matchData.get(i).latitude.toDouble(),
                        matchData[i].longitude.toDouble(),
                        matchData[i].club_name,
                        matchData[i].club_name
                    )
                }
            }
        }
    }

    protected fun createMarker(
        latitude: Double,
        longitude: Double,
        title: String?,
        snippet: String?
    ): Marker? {
        return googleMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))

                .title(title)

            //.snippet(snippet)
            // .icon(BitmapDescriptorFactory.fromResource(iconResID))
        )
    }

    override fun onIconClick(position: Int, clubId: String) {
        if (isNetworkConnected()) {
            likeunlikeVm?.likeUnlikeData(mcontext!!, clubId)
        } else {
            showInternetToast()
        }
    }

    override fun onFavClick(position: Int, data: ClubData) {

    }


    override fun onBottomSheetClose() {
        spinnerfirst.setText(selectedText)
        //<----------call api get Club Listing ---------------->
        if (isNetworkConnected()) {
            clubListVM?.clubListData(mcontext!!, filter_type)
        } else {
            showInternetToast()
        }
        //</------------------------------------------>
    }

    override fun onBottomSheetCloseListener() {
        //   spinnerDataTwo.setText(selectedAddress)
        //<----------call api get Club Listing ---------------->
        if (isNetworkConnected()) {
            clubListVM?.clubListData(mcontext!!, filter_type)
        } else {
            showInternetToast()
        }
        //</------------------------------------------>
    }

    override fun timeSlotSheet() {
        //   spinnerDataThree.setText(selectedDateTime)
        //<----------call api get Club Listing ---------------->
        if (isNetworkConnected()) {
            clubListVM?.clubListData(mcontext!!, filter_type)
        } else {
            showInternetToast()
        }
        //</------------------------------------------>

    }


    companion object {
        var filter_type = "0"
        var  type  = "0"
        var selectedDateFromFilter: String? = null
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