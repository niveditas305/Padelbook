package snow.app.padelbook.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.items_results.view.separator
import kotlinx.android.synthetic.main.items_results.view.tvTitle
import kotlinx.android.synthetic.main.toolbar.view.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.ContactAdapter
import snow.app.padelbook.common.Utils
import snow.app.padelbook.common.Utils.showProgress
import snow.app.padelbook.network.responses.contactListResponse.ContactInfoData
import snow.app.padelbook.network.sendJsonData.ContactListData
import snow.app.padelbook.network.sendJsonData.ContactListSend
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.ui.BaseActivity
import snow.app.padelbook.viewModel.AddPlayerVM
import snow.app.padelbook.viewModel.ContactListResponseVM
import java.text.Normalizer


class ContactActivity : BaseActivity(), ContactAdapter.ContactInterface {
    var pastVisiblesItems: Int = 0
    var loginPref: SessionClass? = null
    private var adapter: ContactAdapter? = null
    private var contactVM: ContactListResponseVM? = null
    private var addPlayerVm: AddPlayerVM? = null

    var memberList: ArrayList<ContactInfoData> =
        ArrayList()               // for padelbook contacts only
    var contactList: ArrayList<ContactListSend> =
        ArrayList()              // for sending contact list to backend
    var adapterList: ArrayList<ContactInfoData>? = ArrayList()
    private var PERMISSIONS_REQUEST_READ_CONTACTS = 1
    var playerKey: String? = null
    var matchID: String? = null
    var tempPos = 0
    var phoneContactFromList: String? = null
    var firstVisibleItem = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private val previousTotal = 0
    private var loading = true
    private val visibleThreshold = 1
    private var current_page = 1
    private var tempPoss = 0
    private var mStatus = "1"
    var totalPages: Int = 0


    companion object {
        var callBackToAddMember = ""
        var isFromBooking = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_contact)
        contactList.clear()
        memberList.clear()
        addPlayerVm = AddPlayerVM()
        contactVM = ContactListResponseVM()
        loginPref = SessionClass(this)
/*if(mContacts!=null && mContacts!!.size>0) {
    adapterList.addAll(mContacts!!)

    setAdapter()
}*/
        adapterList?.clear()
        memberList.clear()


        // setMemberAdapter()
        Utils.showProgress(this)

        setToolbar()


        val mLayoutManager: LinearLayoutManager
        mLayoutManager = LinearLayoutManager(this)
        recycleContact.setLayoutManager(mLayoutManager)
        recycleContact.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount()
                    totalItemCount = mLayoutManager.getItemCount()
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {

                            if (current_page < totalPages) {
                                if (mStatus.equals("2")) {
                                    tempPoss = memberList.size
                                } else {
                                    tempPoss = adapterList?.size!!

                                }
                                current_page = current_page + 1
                                if (!isNetworkConnected()) {
                                    showInternetToast()
                                } else {
                                    contactVM?.contactListDisplay(
                                        this@ContactActivity,
                                        "",
                                        mStatus,
                                        current_page.toString()
                                    )
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
        })

        listener()
        setClick()
        setToggleButton()
        if (intent?.extras?.get("AddPlayer") == "AddPlayer") {
            playerKey = intent.extras?.get("player_key") as String
            matchID = intent.extras?.get("match_id") as String
            Log.d("PLAYER_KEYCONTACT", playerKey!!)
            butSwitch.isChecked = true
            mStatus = "2"
            isFromBooking = true
            setMemberAdapter()
        } else {
            isFromBooking = false
            setAdapter()
        }

        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            showProgress(this)
            contactVM?.contactListDisplay(this, "", mStatus, current_page.toString())
        }
    }

    fun unaccent(src: String?): String? {
        return Normalizer
            .normalize(src, Normalizer.Form.NFD)
            .replace("[^\\p{ASCII}]", "")
    }

    private fun listener() {
        contactVM?.contactListData?.observe(this, Observer { user ->
            if (user != null) {
                // HomeFragment.mContacts = user.data as ArrayList
                //    showToast(user.message)

                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    contactVM?.contactListDisplay(this, "", mStatus, current_page.toString())
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })


        // <----------- contact list response ------------>
        contactVM?.contactList?.observe(this, Observer { user ->
            Utils.dismissProgress()
            if (user != null) {
                if (user.status) {
                    totalPages = user.no_of_pages.toInt()
                    adapterList?.clear()
                    var loginResponse = loginPref?.loginData
                    loginResponse?.fatch_contact = 1      // update fetch_contact status
                    loginPref?.loginData = loginResponse
                    Log.d("fetch_contact222", loginPref?.loginData?.fatch_contact.toString())


                    adapterList?.addAll(user.data)

                    setAdapter()

                    for (i in user.data.indices) {
                        if (user.data[i].status == 1) {
                            memberList.add(user.data[i])

                        }
                    }

                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
        // </------------------------------------------->


        // <------------------ add player response -------->
        addPlayerVm?.addPlayerList?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    callBackToAddMember = "1"
                    onBackPressed()
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
        // <------------------ invite response -------->
        addPlayerVm?.inviteUserList?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    // getContactList()
                    showToast(this, user.message)
                    adapterList!!.get(tempPos).status = 2
                    recycleContact.adapter!!.notifyDataSetChanged()
                    Sms()

                    /*  val smsIntent = Intent(Intent.ACTION_VIEW)
                      smsIntent.type = "vnd.android-dir/mms-sms"
                      val text = "Let me recommend you this application "
                      val link = "https://play.google.com/store/apps/details?id=$packageName"
                      smsIntent.putExtra("address",phoneContactFromList);
                      smsIntent.putExtra("sms_body", text+" "+link)
                      startActivity(smsIntent)*/
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })

        // <------------- contact list get ------------->
        contactVM?.addContactList?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {

                    totalPages = user.no_of_pages.toInt()

                    if (mStatus.equals("2")) {
                        var sss = 0
                        if (memberList.size > 0) {
                            sss = memberList.size - 1
                        }
                        memberList.addAll(user.data)
                        adapter!!.notifyDataSetChanged()
                        recycleContact.smoothScrollToPosition(tempPoss)

                    } else {
                        var sss = 0
                        if (adapterList?.size!! > 0) {
                            sss = adapterList!!.size - 1
                        }
                        /*  if(recycleContact!=null)
                          {
                              if(sss>0)
                              {
                                  recycleContact.smoothScrollToPosition(sss)
                              }}*/
                        adapterList?.addAll(user.data)
                        adapter!!.notifyDataSetChanged()

                        recycleContact.smoothScrollToPosition(tempPoss)

                        adapterList?.let {
                            if (adapterList?.size == 0) {

                            }
                        }
                        //  setAdapter()

                    }


                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })

    }

    override fun onResume() {
        super.onResume()


    }

    private fun loadContactsFromPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
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
                var contactData: ContactListData? = ContactListData(contactList)
//                Log.d("contactList",Gson().toJson(contactData))
                if (BaseFragment.mcontextNew != null) {
                    contactVM?.contactListData(BaseFragment.mcontextNew!!, contactData!!)
                }
                // println("${Thread.currentThread()} has run.")
            }
            thread.start()


            // setAdapter()

        }


    }


    @JvmName("getContactList1")
    @SuppressLint("Range")
    private fun getContactList(): ArrayList<ContactListSend> {
        val cr = BaseFragment.mcontextNew!!.contentResolver
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

    private fun setToolbar() {
        toolbarID.tvTitle.visibility = View.VISIBLE
        if (intent?.extras?.get("AddPlayer") == "AddPlayer") {
            toolbarID.tvTitle.setText(getString(R.string.add_players))
            isFromBooking = true

        } else {
            toolbarID.tvTitle.setText(getString(R.string.my_contacts))
            isFromBooking = false
        }

        toolbarID.separator.visibility = View.VISIBLE
        toolbarID.ivBack.setOnClickListener {
            onBackPressed()
        }
    }


    private fun setClick() {
        synIcon.setOnClickListener {
            loadContacts()
        }

        val drawable = etSearch.background as GradientDrawable
        drawable.setStroke(1, ContextCompat.getColor(this, R.color.dark_color))

        butSwitch.setSafeOnClickListener {
            if (butSwitch.isChecked) {
                current_page = 1
                mStatus = "2"
                totalPages = 0
                memberList.clear()
                setMemberAdapter()
                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    contactVM?.contactListDisplay(this, "", mStatus, current_page.toString())
                }
                // setMemberAdapter()
            } else {
                mStatus = "1"
                current_page = 1

                totalPages = 0
                adapterList?.clear()
                setAdapter()
                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    contactVM?.contactListDisplay(this, "", mStatus, current_page.toString())
                }
                //setAdapter()
            }
        }
        etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                current_page = 1
                totalPages = 0
                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    adapterList?.clear()
                    memberList.clear()
                    contactVM?.contactListDisplay(
                        this@ContactActivity,
                        etSearch.text.toString().toLowerCase(),
                        mStatus,
                        current_page.toString()
                    )
                }
                return@OnEditorActionListener true
            }
            false
        })
        /* etSearch.addTextChangedListener(object : TextWatcher{
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
             }
             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
             }
             override fun afterTextChanged(s : Editable?) {
                 current_page=1
                 if(!isNetworkConnected()){
                     showInternetToast()
                 }
                 else{
                     adapterList.clear()
                     memberList.clear()
                     contactVM?.contactListDisplay(this@ContactActivity,s.toString().toLowerCase(),mStatus,current_page.toString())
                 }
               //  filter(s.toString().toLowerCase())
             }
         })*/

    }

    private fun setToggleButton() {
        butSwitch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    Log.d("if.....", "memeber")
                    setMemberAdapter()
                } else {
                    Log.d("if.....", "adapter")
                    setAdapter()
                }
            }
        })
    }

    private fun setMemberAdapter() {
        Log.d("MemberList", Gson().toJson(memberList))
        adapter = ContactAdapter(this, memberList, this)
        recycleContact.adapter = adapter
    }


    fun filter(text: String) {
        val temp: ArrayList<ContactInfoData> = ArrayList()
        for (d in adapterList!!) {

            if (unaccent(d.username.toString()).toString().toLowerCase()
                    .contains(unaccent(text).toString().toLowerCase())
            ) {
                temp.add(d)
            }
        }
        adapter?.filterList(temp)
    }

    private fun setAdapter() {

        adapter = ContactAdapter(this, adapterList!!, this)
        recycleContact.adapter = adapter
    }

    private fun loadContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {

            //  getContactList()
            Utils.showProgress(this)
            var contactData: ContactListData? = ContactListData(contactList)

            contactVM?.contactListData(this, contactData!!)
            // setAdapter()

        }
    }

    override fun onContactItemClick(position: Int, contactInfoData: ContactInfoData) {
        if (intent?.extras?.getString("AddPlayer").equals("AddPlayer")) {

            if (contactInfoData.status == 1) {
                // add player api
                if (!isNetworkConnected()) {
                    showInternetToast()
                } else {
                    addPlayerVm?.addPlayerData(
                        this,
                        contactInfoData.customer_id,
                        matchID!!,
                        playerKey!!
                    )
                }
            } else {

                //   tempPos=position
//addPlayerVm?.inviteUserData(this,contactInfoData.phone_num)
            }
        }
        if (contactInfoData.status == 1) {

        } else {
            phoneContactFromList = contactInfoData.phone_num
            tempPos = position
            addPlayerVm?.inviteUserData(this, contactInfoData.phone_num)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utils.showProgress(this)
                    loadContacts()
                } else {
                    openCancelSettingDialog()
                    /* showToast("Permission must be granted in order to display contacts information")
                     requireActivity().finishAffinity()*/
                }
            }

        }

    }

    fun openCancelSettingDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.enable_permissions_text_cancel))
            .setPositiveButton(getString(R.string.go_to_settings),
                DialogInterface.OnClickListener { dialog, which -> // navigate to settings
                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                }).setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which -> // leave?
                    dialog.dismiss()
                }).show()
    }

    fun Sms() {
        /*   val smsIntent = Intent(Intent.ACTION_VIEW)
           smsIntent.type = "vnd.android-dir/mms-sms"

           smsIntent.putExtra("address",phoneContactFromList);
           smsIntent.putExtra("sms_body", text+" "+link)
           startActivity(smsIntent)*/
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneContactFromList))

        val text = getString(R.string.sms_text)
        val link = "https://play.google.com/store/apps/details?id=$packageName"
        intent.putExtra("sms_body", text + " " + link)
        startActivity(intent)
    }
}