package snow.app.padelbook.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_create_game.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.CreateGameAdapter
import snow.app.padelbook.bottomsheetdialog.CourtTypeBottomSheet.Companion.selectedTextId
import snow.app.padelbook.bottomsheetdialog.SearchLocationBottomSheet
import snow.app.padelbook.bottomsheetdialog.SearchLocationBottomSheet.Companion.selectedAddress
import snow.app.padelbook.bottomsheetdialog.TimeSlotBottomSheet
import snow.app.padelbook.bottomsheetdialog.TimeSlotBottomSheet.Companion.selectedDateTime
import snow.app.padelbook.fragments.BaseFragment
import snow.app.padelbook.fragments.FilterFragment
import snow.app.padelbook.fragments.SearchFragment.Companion.filter_type
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.clubList.ClubData
import snow.app.padelbook.network.responses.startaMatch.Data
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.viewModel.LikeUnlikeVM
import snow.app.padelbook.viewModel.StartAMatchVM
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_create_game.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class CreateGameActivity : BaseFragment(), SearchLocationBottomSheet.OnBottomSheetCloseListener,
    TimeSlotBottomSheet.OnBottomSheetCloseListener, FavouriteListener, OnMapReadyCallback {
    private var adapter: CreateGameAdapter? = null
    private var likeunlikeVm: LikeUnlikeVM? = null

    var createGameVM: StartAMatchVM? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var REQUEST_CODE = 101
    var mView: View? = null
    var googleMap: GoogleMap? = null
    private var mContext: Context? = null
    var arrayList: ArrayList<Data> = ArrayList()
    private var mapf: SupportMapFragment? = null
    private var currentLocation: Location? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_create_game, container, false)
        return mView
    }


    override fun onAttach(context: Activity) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createGameVM = StartAMatchVM()
        likeunlikeVm = LikeUnlikeVM()

        filter_type = "0"


        // < --------create game activity data ------>
        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            createGameVM?.startAMatchData(requireContext(), filter_type)
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())



        setToolbar()
        clicks()
        //   setDataOnFilters()
        fetchLocation()
        initMap(savedInstanceState)
        listener()
        mapf!!.requireView().visibility = View.GONE

     }

    private fun listener() {
        createGameVM?.matchList?.observe(requireActivity(), Observer { user ->
            if (user != null) {
                if (user.status) {
                    if (user.data != null) {
                        arrayList.clear()
                        arrayList.addAll(user.data)
                        setRecycleAdapter()

                        if (user.address != null && user.address.isNotEmpty()) {
                            mView?.spinnerDataTwo?.setText(user.address)
                        } else {
                            mView?.spinnerDataTwo?.setText(mcontext?.getString(R.string.nearby))
                        }

                        if (user.date != null && user.time != null && user.date.isNotEmpty() && user.time.isNotEmpty()) {
                            mView?.spinnerDataThree?.setText(dateFormatWithMonthName(user.date) + " | " + user.time)
                        } else {
                            Log.e("get current t", "--")
                            mView?.spinnerDataThree?.setText(
                                dateFormatWithMonthNameFull(
                                    Calendar.getInstance().getTime().toString()
                                )
                            )

                        }


                        if (googleMap != null && arrayList != null) {
                            for (i in 0 until arrayList.size) {
                                createMarker(
                                    arrayList.get(i).latitude.toDouble(),
                                    arrayList[i].longitude.toDouble(),
                                    arrayList[i].club_name,
                                    arrayList[i].club_name
                                )
                            }
                        }
                    }

                } else {
                    showToast(user.message)
                }

            } else {
                showToast(mcontext?.getString(R.string.something_went_wrong))
            }
        })


        // <------------------------ fav/Unfav api response ----->
        likeunlikeVm?.likeUnlike?.observe(requireActivity(), Observer { user ->
            if (user.status) {
                showToast(user.message)
                if (isNetworkConnected()) {
                    createGameVM?.startAMatchData(requireContext(), filter_type)
                } else {
                    showInternetToast()
                }
            } else {
                showToast(user.message)
            }
        })
        // <---------------------------------------------------->

    }


    private fun setDataOnFilters() {
        if (SessionClass(requireContext()).recentData != null) {
            if (SessionClass(requireContext()).recentData.address != null) {
                mView?.spinnerDataTwo?.setText(SessionClass(requireContext()).recentData.address)
            } else {
                mView?.spinnerDataTwo?.setText(requireContext().getString(R.string.nearby))

            }
        }
    }


    private fun setRecycleAdapter() {
        adapter = CreateGameAdapter(mContext!!, arrayList, this)
        if (  mView?.recycleCreateGame != null) {
            mView?.recycleCreateGame?.adapter = adapter
        }
    }


    fun clicks() {

        mView?.spinnerDataTwo?.setSafeOnClickListener {
            val bottomSheetLocation = SearchLocationBottomSheet(this, "1")
            bottomSheetLocation.show(childFragmentManager, bottomSheetLocation.tag)
        }

        mView?.spinnerDataThree?.setSafeOnClickListener {
            val bottomSheetTimeSlot = TimeSlotBottomSheet(selectedTextId!!, this, "1")
            bottomSheetTimeSlot.show(childFragmentManager, bottomSheetTimeSlot.tag)
        }

        mView?.filterImg?.setSafeOnClickListener {
            val bottomSheet = FilterFragment(selectedTextId)
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        mView?.ivList?.setSafeOnClickListener {
            mView?.recycleCreateGame?.visibility = View.VISIBLE
            mView?.ivList?.visibility = View.GONE
            mapf!!.requireView().visibility = View.GONE
            mView?.mapImage?.visibility = View.VISIBLE
        }

        mapImage.setSafeOnClickListener {
            mView?.recycleCreateGame?.visibility = View.GONE

            mView?.ivList?.visibility = View.VISIBLE
            mView?.mapImage?.visibility = View.GONE
            mapf!!.requireView().visibility = View.VISIBLE

            if (googleMap != null && currentLocation != null) {
                val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            }
        }

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

    private fun setToolbar() {
        mView?.toolbar_game?.tvTitle?.visibility = View.VISIBLE
        mView?.toolbar_game?.tvTitle?.setText(getString(R.string.start_a_match))
        mView?.toolbar_game?.separator?.visibility = View.VISIBLE
        mView?.toolbar_game?.ivBack?.visibility = View.GONE
//        toolbar.ivBack.setSafeOnClickListener {
//            onBackPressed()
//        }
    }


    fun initMap(savedInstanceState: Bundle?) {

        mapf = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapf!!.getMapAsync(this)
    }


    override fun onBottomSheetCloseListener() {
        // spinnerDataTwo.setText(selectedAddress)
        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            createGameVM?.startAMatchData(requireContext(), filter_type)
        }
    }


    override fun timeSlotSheet() {
        //  spinnerDataThree.setText(selectedDateTime)
        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            createGameVM?.startAMatchData(requireContext(), filter_type)
        }
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
        googleMap?.getUiSettings()!!.setZoomControlsEnabled(true);
        googleMap?.getUiSettings()!!.setAllGesturesEnabled(true);
        if (googleMap != null && arrayList != null) {
            for (i in 0 until arrayList.size) {
                createMarker(
                    arrayList.get(i).latitude.toDouble(),
                    arrayList[i].longitude.toDouble(),
                    arrayList[i].club_name,
                    arrayList[i].club_name
                )
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

    fun dateFormatWithMonthName(date: String): String {
        val myFormat = SimpleDateFormat("yyyy-MM-dd").parse(date)
        var myDate = SimpleDateFormat("EEEE, dd/MM").format(myFormat)
        return myDate
    }
}