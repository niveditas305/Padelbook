package snow.app.padelbook.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_icon.view.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.HomeAdapterFirst
import snow.app.padelbook.adapter.HomeAdapterSecond
import snow.app.padelbook.bottomsheetdialog.CourtTypeBottomSheet.Companion.selectedTextId
import snow.app.padelbook.common.IMAGES_URL
import snow.app.padelbook.common.Utils
import snow.app.padelbook.listener.ClickEvent
import snow.app.padelbook.network.responses.bookingResponse.BookingData
import snow.app.padelbook.network.responses.contactListResponse.ContactInfoData
import snow.app.padelbook.network.responses.homeList.Data
import snow.app.padelbook.network.responses.homeList.Match
import snow.app.padelbook.network.sendJsonData.ContactListData
import snow.app.padelbook.network.sendJsonData.ContactListSend
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.ui.GamesActivity
import snow.app.padelbook.ui.SettingActivity
import snow.app.padelbook.viewModel.HomeListResponse
import snow.app.padelbook.viewModel.LocationVm
import androidx.core.widget.NestedScrollView

import android.R.string.no
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_register_screen.view.*
import snow.app.padelbook.common.AppBarStateChangeListener
import android.R.string.no
import android.app.AlertDialog
import android.content.DialogInterface
import android.location.Geocoder
import android.provider.Settings
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.view.*
import snow.app.padelbook.ui.HomeActivity
import snow.app.padelbook.utils.MyApplication
import snow.app.padelbook.utils.MyApplication.Companion.appInstance
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment(), ClickEvent, SwipeRefreshLayout.OnRefreshListener {
    private var loginPref: SessionClass? = null

    private var adapter: HomeAdapterFirst? = null
    private var adapterTwo: HomeAdapterSecond? = null
    private var homeVM: HomeListResponse? = null
    private var locationVM: LocationVm? = null

    private val courtDataList: ArrayList<Data> = ArrayList()
    private val matchList: ArrayList<Match> = ArrayList()

    // private var PERMISSIONS_REQUEST_READ_CONTACTS = 1
    var contactList: ArrayList<ContactListSend> = ArrayList()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var REQUEST_CODE = 101
    var task: Task<Location>? = null
    var pastVisiblesItems: Int = 0
    var firstVisibleItem = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var totalPages: Int = 0
    private val previousTotal = 0
    private var loading = true
    private val visibleThreshold = 1
    private var current_page = 1
    private var tempPoss = 0
    var addressFull = ""
    var mView: View? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        var pImageView: ImageView? = null
        var mContacts: ArrayList<ContactInfoData>? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false)
        firebaseAnalytics = Firebase.analytics
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeVM = HomeListResponse()
        locationVM = LocationVm()
        loginPref = SessionClass(requireContext())
        swiperefresh.setOnRefreshListener(this);
        val linearLayoutManager = LinearLayoutManager(requireContext())
        val linearLayoutManagerFirst =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mView?.recycleSecond?.layoutManager = linearLayoutManager
        mView?.recycleFirst?.layoutManager = linearLayoutManagerFirst
        mView?.recycleFirst?.setNestedScrollingEnabled(false);

        mView?.appBarLayout?.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                Log.d("STATE", state.name)
                mView?.swiperefresh?.isEnabled = state == State.EXPANDED
            }
        })

        mView?.recycleFirst?.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_CANCEL
                || motionEvent.action == MotionEvent.ACTION_UP
            ) {

                mView?.swiperefresh?.setEnabled(false);
                nested.isEnabled = false
            }
            false
        }


        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    val firstPos: Int = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (firstPos > 0) {
                        //  appBarLayout.setExpanded(false)
                        //  swiperefresh.setEnabled(false)
                    } else {
                        //   appBarLayout.setExpanded(true)

                        //  swiperefresh.setEnabled(true)


                        //if (recycleSecond.getScrollState() === 1) if (swiperefresh.isRefreshing()) recycleSecond.stopScroll()
                    }
                } catch (e: Exception) {
                    //   Log.e(TAG, "Scroll Error : " + e.localizedMessage)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    //   appBarLayout.setExpanded(true)
                }
                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount()
                    totalItemCount = linearLayoutManager.getItemCount()
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition()
                    //  Log.e("visibleItemCount",visibleItemCount.toString())
                    //  Log.e("totalItemCount",totalItemCount.toString())
                    //  Log.e("pastVisiblesItems",pastVisiblesItems.toString())
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {

                            if (current_page < totalPages) {
                                tempPoss = matchList.size

                                current_page = current_page + 1
                                if (isNetworkConnected()) {
                                    homeVM?.homeData(requireContext(), current_page.toString())
                                } else {

                                    showInternetToast()
                                }
                            }
                            loading = false
                            Log.e("...", "Last Item Wow !")
                            // Do pagination.. i.e. fetch new data
                            loading = true
                        }
                    }
                }
            }
        }

        mView?.recycleSecond?.addOnScrollListener(scrollListener)
        // recycleFirst.addOnScrollListener(scrollListenerFirst)
        Log.d("LoginPrefResponse", Gson().toJson(loginPref?.loginData))
        pImageView = toolBar.profile_image
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        loginPref?.loginData?.image_file?.let {
            Log.e("profile", loginPref?.loginData?.image_file.toString())

            if (loginPref?.loginData?.image_file!!.startsWith("http")) {
                mView?.toolBar?.profile_image?.let { it1 ->
                    Glide.with(requireActivity())
                        .load(loginPref?.loginData?.image_file)             // image url
                        .placeholder(R.drawable.ic_user) // any placeholder to load at start
                        .error(R.drawable.ic_user)  // any image in case of error
                        .into(it1)
                }
            } else {

                val dd = loginPref?.loginData
                dd!!.image_file =
                    IMAGES_URL + loginPref?.loginData?.image_file

                loginPref!!.loginData = dd
                mView?.toolBar?.profile_image?.let { it1 ->
                    Glide.with(requireActivity())
                        .load(IMAGES_URL + loginPref?.loginData?.image_file)             // image url
                        .placeholder(R.drawable.ic_user) // any placeholder to load at start
                        .error(R.drawable.ic_user)  // any image in case of error
                        .into(it1)
                }
            }

        } ?: kotlin.run {
            mView?.toolBar?.let {
                Glide.with(requireActivity())
                    .load(R.drawable.ic_user)             // image url
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)  // any image in case of error
                    .into(it.profile_image)
            }
        }


        if (mcontext != null) {
            if (isNetworkConnected()) {
                loadDataFromLocation()
            } else {
                showInternetToast()
            }

        }
        listener()
        setClick()
        setAdapterTwo()
        if (isNetworkConnected()) {
            homeVM?.homeData(requireContext(), current_page.toString())
        } else {
            showInternetToast()
        }


    }


    private fun loadContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS
                ),
                2
            )
            //callback onRequestPermissionsResult
        } else {
            val thread = Thread {
                getContactList()
                //  Utils.showProgress(requireContext())
                if (contactList.size > 0) {
                    var contactData: ContactListData? = ContactListData(contactList)
//                Log.d("contactList",Gson().toJson(contactData))
                    if (mcontextNew != null) {

                        homeVM?.contactListData(mcontextNew!!, contactData!!)
                    }
                }
                // println("${Thread.currentThread()} has run.")
            }
            thread.start()


            // setAdapter()

        }


    }

    fun loadDataFromLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            //callback onRequestPermissionsResult
        } else {


            if (SettingActivity.is_location.equals("1")) {
                task = fusedLocationProviderClient.lastLocation
                task!!.addOnSuccessListener(OnSuccessListener<Location?> { location ->
                    if (location != null) {
                        //currentLocation = location
                        //Log.e("ccc loca",""+currentLocation)
                        if (isNetworkConnected()) {
                            locationVM?.automaticLocationUpdate(
                                requireContext(),
                                location.latitude.toString(),
                                location.longitude.toString(), "1",
                                getAddressFromLatLng(location.latitude, location.longitude)

                            )
                        }else {
                            showInternetToast()
                        }


                    }
                })
            } else {
                task = fusedLocationProviderClient.lastLocation
                task!!.addOnSuccessListener(OnSuccessListener<Location?> { location ->
                    if (location != null) {
                        //currentLocation = location
                        //Log.e("ccc loca",""+currentLocation)
                        if (isNetworkConnected()) {
                            locationVM?.automaticLocationUpdate(
                                requireContext(),
                                "",
                                "", "0", ""
                            )
                        }else {
                            showInternetToast()
                        }

                    }
                })

            }

            // setAdapter()
            loadContacts()
        }
    }

    @JvmName("getContactList1")
    @SuppressLint("Range")
    private fun getContactList(): ArrayList<ContactListSend> {
        val cr = mcontextNew!!.contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((if (cur != null) cur.getCount() else 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        var contactModel = ContactListSend()


                        // for the contact person id
                        var contactId =
                            pCur.getString(pCur.getColumnIndex(ContactsContract.Contacts._ID))
                        //  contactModel.id = contactId


                        // for the contact person name
                        contactModel.username = pCur.getString(
                            pCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        )
                        Log.d("person name", contactModel.username.toString())


                        // for the contact mobile number
                        contactModel.phone_num = pCur.getString(
                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        ).replace(" ", "")
                        Log.d("person name", contactModel.phone_num.toString())


                        contactList.add(contactModel)
                    }
                    pCur.close()
                }
            }
        }
        cur?.close()
        Utils.dismissProgress()
        return contactList
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                adapterTwo!!.notifyDataSetChanged()
                if (checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }


                if (SettingActivity.is_location.equals("1")) {
                    task = fusedLocationProviderClient.lastLocation
                    task!!.addOnSuccessListener(OnSuccessListener<Location?> { location ->
                        if (location != null) {
                            //currentLocation = location
                            //Log.e("ccc loca",""+currentLocation)
                            if (isNetworkConnected()) {
                                locationVM?.automaticLocationUpdate(
                                    requireContext(),
                                    location.latitude.toString(),
                                    location.longitude.toString(),
                                    "1",
                                    getAddressFromLatLng(location.latitude, location.longitude)
                                )

                            } else {
                                showInternetToast()
                            }
                        }
                    })
                } else {
                    task = fusedLocationProviderClient.lastLocation
                    task!!.addOnSuccessListener(OnSuccessListener<Location?> { location ->
                        if (location != null) {
                            //currentLocation = location
                            //Log.e("ccc loca",""+currentLocation)
                            if (isNetworkConnected()) {
                                locationVM?.automaticLocationUpdate(
                                    requireContext(),
                                    "",
                                    "", "0", ""
                                )
                            }else {
                                showInternetToast()
                            }

                        }
                    })

                }


                /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                           requireActivity(),
                           Manifest.permission.READ_CONTACTS
                       ) != PackageManager.PERMISSION_GRANTED
                   ) {
                       requestPermissions(
                           arrayOf(Manifest.permission.READ_CONTACTS),
                           PERMISSIONS_REQUEST_READ_CONTACTS
                       )
                       //callback onRequestPermissionsResult
                   } else {
                       val thread = Thread {
                           getContactList()
                           //  Utils.showProgress(requireContext())
                           var contactData: ContactListData? = ContactListData(contactList)
   //                Log.d("contactList",Gson().toJson(contactData))
                           if (mcontextNew != null) {
                               homeVM?.contactListData(mcontextNew!!, contactData!!)
                           }
                           // println("${Thread.currentThread()} has run.")
                       }
                       thread.start()


                   }*/
                loadContacts()

            } else {
                openSettingDialog()
                /* requireActivity().finishAffinity()
                 showToast("Permission must be granted in order to proceed")*/

                if (isNetworkConnected()) {
                    homeVM?.homeData(requireContext(), current_page.toString())
                } else {

                    showInternetToast()
                }

                loadContacts()
            }
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.showProgress(requireContext())
                    loadContacts()
                } else {
                    openCancelSettingDialog()
                    /* showToast("Permission must be granted in order to display contacts information")
                     requireActivity().finishAffinity()*/
                }
            }

        }
        /* if (requestCode == 1) {
             if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 matchList.clear()
                 adapterTwo!!.notifyDataSetChanged()
                 task!!.addOnSuccessListener(OnSuccessListener<Location?> { location ->
                     if (location != null) {
                         //currentLocation = location
                         //Log.e("ccc loca",""+currentLocation)
                         locationVM?.automaticLocationUpdate(
                             requireContext(),
                             location.latitude.toString(),
                             location.longitude.toString()
                         )

                        *//* if (isNetworkConnected()) {
                            matchList.clear()


                            adapterTwo!!.notifyDataSetChanged()
                             homeVM?.homeData(requireContext(), current_page.toString())
                        } else {

                            showInternetToast()
                        }
*//*

                    }
                })

             *//*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.READ_CONTACTS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                    //callback onRequestPermissionsResult
                } else {
                    val thread = Thread {
                        getContactList()
                        //  Utils.showProgress(requireContext())
                        var contactData: ContactListData? = ContactListData(contactList)
//                Log.d("contactList",Gson().toJson(contactData))
                        if (mcontextNew != null) {
                            homeVM?.contactListData(mcontextNew!!, contactData!!)
                        }
                        // println("${Thread.currentThread()} has run.")
                    }
                    thread.start()


                }*//*
            } else {
                requireActivity().finishAffinity()
                 showToast( "Permission must be granted in order to proceed")
            }
        }else  if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.showProgress(requireContext())
                loadContacts()
            } else {

                showToast("Permission must be granted in order to display contacts information")
                requireActivity().finishAffinity()
            }
        }*/
    }

    override fun onResume() {
        super.onResume()
        loginPref?.loginData?.image_file?.let {
            Log.e("profileee", loginPref?.loginData?.image_file.toString())
            if (loginPref?.loginData?.image_file!!.startsWith("http")) {

                mView?.toolBar?.let { it1 ->
                    Glide.with(requireActivity())
                        .load(loginPref?.loginData?.image_file)             // image url
                        .placeholder(R.drawable.ic_user) // any placeholder to load at start
                        .error(R.drawable.ic_user)  // any image in case of error
                        .into(it1?.profile_image)
                }
            } else {
                mView?.toolBar?.let { it1 ->
                    Glide.with(requireActivity())
                        .load(loginPref?.loginData?.image_file)             // image url
                        .placeholder(R.drawable.ic_user) // any placeholder to load at start
                        .error(R.drawable.ic_user)  // any image in case of error
                        .into(it1.profile_image)
                }
            }
        } ?: kotlin.run {
            mView?.toolBar?.let {
                Glide.with(requireActivity())
                    .load(R.drawable.ic_user)             // image url
                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                    .error(R.drawable.ic_user)  // any image in case of error
                    .into(it?.profile_image)
            }
        }
    }

    override fun onAttach(context: Activity) {
        super.onAttach(context)
        mcontext = context
        mcontextNew = requireActivity()
    }

    override fun onDetach() {
        super.onDetach()
        mcontext = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listener() {

        homeVM?.homeList?.observe(requireActivity(), Observer { user ->
            mView?.swiperefresh?.setRefreshing(false)


            if (user != null) {
                if (mView?.swiperefresh != null) {
                    mView?.swiperefresh?.setRefreshing(false)
                }

                if (user.status) {
                    if (!user.no_of_pages.isNullOrEmpty()) {
                        totalPages = user.no_of_pages.toInt()
                    }
                    user.user_score?.let {
                        if (it.isNotEmpty()) {
                            val score = it.split("(")
                            val scoreVaule = score.get(0)
                            val totalScoreValue = (100 / 7) * scoreVaule.toDouble()
                            mView?.arc_progress?.progress = totalScoreValue.toInt()
                        }


                    }

                    // showToast(
                    // user.message)
                    courtDataList.clear()


                    if (user.data.size > 0) {
                        courtDataList.addAll(user.data)
                        setAdapter()
                    }

                    if (user.matchList.size > 0) {

                        mView?.llNoResultsFound?.visibility = View.GONE
                        mView?.recycleSecond?.visibility = View.VISIBLE
/*
                        var sss=0
                        if(matchList.size>0)
                        {
                             sss=matchList.size-1
                        }*/

                        matchList.addAll(user.matchList)



                        adapterTwo!!.notifyDataSetChanged()
                        if (mcontext != null && mView?.recycleSecond != null) {
                            if (matchList.size > 0) {
                                mView?.recycleSecond?.smoothScrollToPosition(tempPoss)
                            }
                        }
                        /*       if(recycleSecond!=null)
                               {
                               recycleSecond.smoothScrollToPosition(tempPoss)
                           }*/

                    } else {
                        mView?.llNoResultsFound?.visibility = View.VISIBLE
                        mView?.recycleSecond?.visibility = View.GONE
                    }

                    if (user.matchList.size == 0) {
                        mView?.llNoResultsFound?.visibility = View.VISIBLE
                        mView?.recycleSecond?.visibility = View.GONE
                    } else {

                        mView?.llNoResultsFound?.visibility = View.GONE
                        mView?.recycleSecond?.visibility = View.VISIBLE
                    }
                } else {
                    /* if (user.data.size > 0) {
                         courtDataList.addAll(user.data)
                         setAdapter()
                     }*/
                    showToast(user.message)
                }
            } else {
                if (mcontext != null) {
                    showToast(mcontext!!.getString(R.string.something_went_wrong))
                }
            }
        })
        homeVM?.contactList?.observe(requireActivity(), Observer { user ->
            if (user != null) {
                mContacts = user.data as ArrayList
                //    showToast(user.message)

            } else {
                showToast(getString(R.string.something_went_wrong))
            }
        })
        locationVM?.changeLocationnn?.observe(requireActivity(), Observer { user ->
            Log.e("ddd", Gson().toJson(user))
            if (user != null) {
                if (mcontext != null && isNetworkConnected()) {
                    current_page = 1
                    matchList.clear()
                    homeVM?.homeData(requireContext(), current_page.toString())
                } else {
                    if (mcontext != null) {
                        showInternetToast()
                    }
                }
                //    showToast(user.message)

            } else {
                showToast(mcontext!!.getString(R.string.something_went_wrong))
            }
        })
    }

    /* private fun setRecycleViewScrolling() {
         appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
             override fun onOffsetChanged(appBarLayout: AppBarLayout?, i: Int) {

                 if (Math.abs(i) == appBarLayout!!.getTotalScrollRange()) {
                     //  Collapsed
                     //    Toast.makeText(mcontext,"Collapsed", Toast.LENGTH_SHORT).show()
                     //     scaleFadeOutAnim(title,filterText,titlePlay)
                     cl_main.setBackgroundColor(ContextCompat.getColor(mcontext!!, R.color.white))
                     headingFirstSec.setBackgroundColor(
                         ContextCompat.getColor(
                             mcontext!!,
                             R.color.white
                         )
                     )
                     titleFour.visibility = View.VISIBLE
                     titleThree.visibility = View.GONE
                     sep_view.visibility = View.VISIBLE
                 } else if (i == 0) {
                     // Expanded
                     cl_main.setBackgroundColor(
                         ContextCompat.getColor(
                             mcontext!!,
                             R.color.light_blue
                         )
                     )
                     headingFirstSec.setBackgroundColor(
                         ContextCompat.getColor(
                             mcontext!!,
                             R.color.light_blue
                         )
                     )
                     //   Toast.makeText(mcontext,"Expanded", Toast.LENGTH_SHORT).show()
                     titleFour.visibility = View.GONE
                     titleThree.visibility = View.VISIBLE
                     sep_view.visibility = View.GONE

                     //scaleAnim(title,filterText,titlePlay)

                 } else {
                     scaleAnim(title, filterText, titlePlay)
                     //   Toast.makeText(mcontext,"InBetween", Toast.LENGTH_SHORT).show()
                 }
             }
         })
     }
 */

    /*  fun scaleAnim(textview: TextView, filterText: TextView, titlePlay: TextView) {
          val fade_in = ScaleAnimation(
              0f,
              1f,
              0f,
              1f,
              Animation.RELATIVE_TO_SELF,
              0.5f,
              Animation.RELATIVE_TO_SELF,
              0.5f
          )
          fade_in.duration = 1000
          fade_in.fillAfter = true
          textview.startAnimation(fade_in)
          // textview.startAnimation(AnimationUtils.loadAnimation(mcontext,R.anim.scale_down))
          filterText.startAnimation(fade_in)
          titlePlay.startAnimation(fade_in)
      }
  */
    private fun setClick() {

        //filter click
        mView?.filterSection?.setSafeOnClickListener {
            val bottomSheetTimeSlot = FilterFragment(selectedTextId)
            bottomSheetTimeSlot.show(childFragmentManager, bottomSheetTimeSlot.tag)
        }
        toolBar.profile_image.setSafeOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }
    }

    private fun setAdapter() {
        if (courtDataList != null && mcontext != null) {
            adapter = HomeAdapterFirst(mcontext!!, courtDataList)
        }
        if (mView?.recycleFirst != null) {
            mView?.recycleFirst?.adapter = adapter
        }
    }

    private fun setAdapterTwo(/*matchListt: ArrayList<Match>*/) {
        if (mcontext != null) {
            adapterTwo = HomeAdapterSecond(mcontext!!, this, "Home", matchList)
        }
        if (mView?.recycleSecond != null) {
            mView?.recycleSecond?.adapter = adapterTwo
        }
    }

    override fun clickEvent(matchId: String, playerKey: String, courtFeature: String) {


        startActivity(
            Intent(requireContext(), GamesActivity::class.java)
                .putExtra("callFrom", "Home")
                .putExtra("matchId", matchId)
                .putExtra("playerKey", playerKey)
                .putExtra("courtFeatureType", courtFeature)
        )
        //firebaseAnalytics.logEvent()
    }

    override fun clickEventForBooking(position: Int) {

    }

    override fun clickForBookingDetail(matchData: BookingData) {

    }

    override fun onRefresh() {
        if (isNetworkConnected()) {
            current_page = 1
            matchList.clear()
            homeVM?.homeData(requireContext(), current_page.toString())
        } else {

            showInternetToast()
        }
    }

    fun AppBarLayout.collapsingPercentage() =
        Math.abs(this.height - this.bottom) / this.totalScrollRange.toFloat()

    fun openSettingDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.enable_permissions_text))
            .setPositiveButton(getString(R.string.go_to_settings),
                DialogInterface.OnClickListener { dialog, which -> // navigate to settings
                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                }).setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which -> // leave?
                    dialog.dismiss()
                }).show()
    }

    fun openCancelSettingDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.enable_permissions_text_cancel))
            .setPositiveButton(getString(R.string.go_to_settings),
                DialogInterface.OnClickListener { dialog, which -> // navigate to settings
                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                }).setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which -> // leave?
                    dialog.dismiss()
                }).show()
    }

    fun getAddressFromLatLng(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var address = geoCoder.getFromLocation(
            lat,
            lng,
            1
        )
        address?.let {

//        Log.d("AddressDetails", address[0].toString())
            addressFull = address[0].getAddressLine(0)
            // set zipCode
            /*   if (address[0].postalCode != null) {
                   //  binding.zipCode.setText(address[0].postalCode)
               }*/
/*
            // set state
            if (address[0].adminArea != null) {
                //binding.state.setText(address[0].adminArea)
            }*/

            /*   // set city
               if (address[0].locality != null) {
                    // binding.cityName.setText(address[0].subAdminArea)
               } else {
                    //   binding.cityName.setText(address.get(0).locality)
               }*/
        }

        return addressFull
    }
}