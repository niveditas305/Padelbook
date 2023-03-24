package snow.app.padelbook.ui

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.toolbar.view.*
import snow.app.padelbook.R
import snow.app.padelbook.viewModel.ProfileVM
import java.util.*
import androidx.core.app.ActivityCompat

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context


import android.os.Build

import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import snow.app.padelbook.common.Utils.*
import java.io.ByteArrayOutputStream
import java.io.File
import android.util.DisplayMetrics
import android.widget.EditText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_games.*
import kotlinx.android.synthetic.main.toolbar.view.ivBack
import kotlinx.android.synthetic.main.toolbar_icon.view.*
import snow.app.padelbook.BuildConfig
import snow.app.padelbook.common.BASE_URL_ONE
import snow.app.padelbook.common.IMAGES_URL
import snow.app.padelbook.common.PRIVACY_POLICY_URL
import snow.app.padelbook.common.TERMS_AND_CONDITIONS

import snow.app.padelbook.common.Utils.context
import snow.app.padelbook.fragments.HomeFragment
import snow.app.padelbook.network.responses.login.LoginData
import snow.app.padelbook.session.SessionClass
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class SettingActivity : BaseActivity() {

    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
    var lattitue: String? = null
    var longitude: String? = null

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_CODE = 2
    private val PERMISSION_REQUEST_CODE_LOCATION = 3
    val GALLERY_REQUESTCODE = 3
    val CAMERA_REQUESTCODE = 4

    private var profileVM: ProfileVM? = null
    var profileData: LoginData? = null

    var profileImage = ""
    var loginPref: SessionClass? = null

    companion object {
        var is_location = "1"
    }

    var addressFull = ""
    var gender = 0
    var dob = ""
    var dobApi = ""
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var REQUEST_CODE = 101
    var task: Task<Location>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        profileVM = ProfileVM()
        loginPref = SessionClass(this)

        if (!isNetworkConnected()) {
            showToast(this, getString(R.string.no_internet_connection))
        } else {
            // call api get profile data
            profileVM?.getProfileData(this)
        }

        if (SessionClass(this).loginData.language_type == "0") {
            changeLanguage("en")
        } else {
            changeLanguage("el")
        }
        languageSelector.lifecycleOwner = this
        setSpinner()
        initAddressPicker()
        setToolbar()

        setClick()
        listener()
    }

    private fun listener() {
        // get profile data api response
        profileVM?.userProfile?.observe(this, androidx.lifecycle.Observer { user ->
            if (user != null) {
                if (user.status) {
                    //   showToast(this, user.message)
                    if (user.data != null) {
                        profileData = user.data
                        if (user.data.image_file != null) {
                            Glide.with(this).load(user.data.image_file)
                                .error(R.drawable.ic_user)
                                .placeholder(R.drawable.ic_user).into(dp)

                            profileImage = user.data.image_file.toString()
                        }
                        if (user.data.name != null) {
                            etName.setText(user.data.name)
                        }
                        if (user.data.email != null) {
                            etEmail.setText(user.data.email)
                        }
                        if (user.data.phone != null) {
                            if (user.data.country_code != null) {
                                etMobile.setText(user.data.country_code + user.data.phone)
                            }
                        }
                        if (user.data.check_password != null) {
                            etPass.setText(user.data.check_password)
                        }
                        if (user.data.gender != null) {

                            Log.e("user.data.gender", user.data.gender)
                            /*  if (user.data.gender.equals("Άνδρας") || user.data.gender.equals("Man")) {
                                  selectGender.setText(getString(R.string.man))
                                  gender = 1
                              } else {
                                  selectGender.setText(getString(R.string.quest_secndoptionn))
                                  gender = 2
                              }*/

                            if (user.data.gender.equals("1") /*|| user.data.gender.equals("Man")*/) {
                                selectGender.setText(getString(R.string.man))
                                gender = 1
                            } else {
                                selectGender.setText(getString(R.string.quest_secndoptionn))
                                gender = 2
                            }

                        }

                        if (user.data.score != null) {
                            etLevel.setText(user.data.score)
                        }
                        if (user.data.address != null) {
                            selectAddress.setText(user.data.address)
                        }
                        if (user.data.latitude != null) {
                            lattitue = user.data.latitude
                        }
                        if (user.data.longitude != null) {
                            longitude = user.data.longitude
                        }
                        if (user.data.is_location != null) {
                            if (user.data.is_location == "0") {
                                butSwitch.isChecked = false
                                is_location = "0"
                            } else {
                                butSwitch.isChecked = true
                                is_location = "1"
                            }
                        }

                        if (user.data.language_type != null) {
                            if (user.data.language_type == "0") {
                                changeLanguage("en")

                                if (user.data.date_of_birth != null) {
                                    dobApi = user.data.date_of_birth
                                    dateBirth.setText(
                                        dateFormatWithMonthNameFull(
                                            user.data.date_of_birth,
                                            "en"
                                        )
                                    )
                                }
                                languageSelector.text = getString(R.string.english)
                                val list: ArrayList<String> = ArrayList()
                                list.add(getString(R.string.greek))
                                languageSelector.setItems(list)
                            } else {
                                changeLanguage("el")
                                if (user.data.date_of_birth != null) {
                                    dobApi = user.data.date_of_birth
                                    dateBirth.setText(
                                        dateFormatWithMonthNameFull(
                                            user.data.date_of_birth,
                                            "el"
                                        )
                                    )
                                }
                                languageSelector.text = getString(R.string.greek)
                                val list: ArrayList<String> = ArrayList()
                                list.add(getString(R.string.english))
                                languageSelector.setItems(list)
                            }

                        }


                    } else {
                        showToast(this, user.message)
                    }

                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })


        var saveToken = loginPref?.loginData?.token
        //  update profile response
        profileVM?.updateProfile?.observe(this, androidx.lifecycle.Observer { user ->
            if (user != null) {
                if (user.status) {

                    Log.d("ProfileResponse", Gson().toJson(user.data))
                    user.data.token = saveToken!!
                    loginPref?.loginData = user.data
                    Log.e("ProfileResponse", loginPref?.loginData?.image_file.toString() + "--")


                    /*        if (loginPref?.loginData?.image_file!!.startsWith("http")) {
                                Glide.with(this)
                                    .load(loginPref?.loginData?.image_file)             // image url
                                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                    .error(R.drawable.ic_user)  // any image in case of error
                                    .into(HomeFragment.pImageView!!)
                            } else {
                                Glide.with(this)
                                    .load(IMAGES_URL + loginPref?.loginData?.image_file)             // image url
                                    .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                    .error(R.drawable.ic_user)  // any image in case of error
                                    .into(HomeFragment.pImageView!!)
                            }*/

                    showToast(this, user.message)
                    //   startActivity(Intent(this, HomeActivity::class.java))
                    //   finishAffinity()
                    //   onBackPressed()
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })
        profileVM?.updateProfileOnly?.observe(this, androidx.lifecycle.Observer { user ->
            if (user != null) {
                if (user.status) {
                    Log.d("ProfileResponse", Gson().toJson(user.data))
                    user.data.token = saveToken!!
                    loginPref?.loginData = user.data
                    Log.e("ProfileResponse", loginPref?.loginData?.image_file.toString() + "--")


                    /*      if (loginPref?.loginData?.image_file!!.startsWith("http")) {
                              Glide.with(this)
                                  .load(loginPref?.loginData?.image_file)             // image url
                                  .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                  .error(R.drawable.ic_user)  // any image in case of error
                                  .into(HomeFragment.pImageView!!)
                          } else {
                              Glide.with(this)
                                  .load(IMAGES_URL + loginPref?.loginData?.image_file)             // image url
                                  .placeholder(R.drawable.ic_user) // any placeholder to load at start
                                  .error(R.drawable.ic_user)  // any image in case of error
                                  .into(HomeFragment.pImageView!!)
                          }*/

                    showToast(this, user.message)
                } else {
                    showToast(this, user.message)
                }
            } else {
                showToast(this, getString(R.string.something_went_wrong))
            }
        })


        // change location response
        profileVM?.changeLocation?.observe(this, androidx.lifecycle.Observer { user ->
            if (user.status) {
                showToast(this, user.message)
            } else {
                showToast(this, user.message)
            }
        })


        // change language
        profileVM?.changelanguage?.observe(this, androidx.lifecycle.Observer { user ->
            if (user.status) {
                if (user.data.language_type == "0") {
                    SessionClass(this).loginData = user.data
                    changeLanguage("en")                // for english lang
                    startActivity(Intent(this, HomeActivity::class.java))
                    showToast(this, user.message)

                } else {
                    SessionClass(this).loginData = user.data
                    changeLanguage("el")                 // for greek lang
                    startActivity(Intent(this, HomeActivity::class.java))
                    showToast(this, user.message)
                }
            } else {
                showToast(this, user.message)
            }
        })
    }


    fun initAddressPicker() {
        // initialize the address picker
        Places.initialize(this, getString(R.string.new_google_maps_key))
        tvAppVersion.text =
            getString(R.string.app_version) + " " + getString(R.string.version) + " " + BuildConfig.VERSION_NAME
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )

    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE_LOCATION
        )

    }

    private fun checkPermission(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(applicationContext, CAMERA)
        val result2 = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissionLocation(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE ->

                if (grantResults.size > 0) {
                    //  val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val externalStorageAccepted =
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && externalStorageAccepted) {
                        showToast(
                            this,
                            getString(R.string.permission_granted_now_access_files_and_camera)
                        )
                        //    chooseImage(this)

                        pickImage()
                    } else {
                        openSettingDialogCamera()
                    }
                }

            PERMISSION_REQUEST_CODE_LOCATION ->
                if (grantResults.size > 0) {
                    val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (locationAccepted) {

                        if (ActivityCompat.checkSelfPermission(
                                this,
                                ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this,
                                ACCESS_COARSE_LOCATION
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
                        task = fusedLocationProviderClient.lastLocation
                        task!!.addOnSuccessListener(OnSuccessListener<Location?> { location ->
                            if (location != null) {

                                profileVM?.automaticLocationUpdate(
                                    this,
                                    location.latitude.toString(),
                                    location.longitude.toString(),
                                    "1",
                                    getAddressFromLatLng(location.latitude, location.longitude)
                                )
                            }
                        })
                    } else {
                        openSettingDialog()
                    }
                }
        }
    }

    fun openSettingDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.enable_permissions_text))
            .setPositiveButton(getString(R.string.go_to_settings),
                DialogInterface.OnClickListener { dialog, which -> // navigate to settings
                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                }).setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which -> // leave?
                    dialog.dismiss()
                }).show()
    }

    fun openSettingDialogCamera() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.permissions_denied_you_dont_have_access_to_files_camera))
            .setPositiveButton(getString(R.string.go_to_settings),
                DialogInterface.OnClickListener { dialog, which -> // navigate to settings
                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                }).setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which -> // leave?
                    dialog.dismiss()
                }).show()
    }

    private fun showMessageOKCancel(s: String, onClickListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@SettingActivity)
            .setMessage("message")
            .setPositiveButton("OK", onClickListener)
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
            .show()
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            getString(R.string.take_photo),
            getString(R.string.choose_from_gallery),
            getString(R.string.exit)
        ) // create a menuOption Array
        // create a dialog for showing the optionsMenu
        val builder = AlertDialog.Builder(context)
        // set the items in builder
        builder.setItems(
            optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == getString(R.string.take_photo)) {
                // Open the camera and get the photo
                openCamera()
            } else if (optionsMenu[i] == getString(R.string.choose_from_gallery)) {
                // choose from  external storage
                openGallery()
            } else if (optionsMenu[i] == getString(R.string.exit)) {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }

    fun pickImage() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()

    }

    private fun openGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhoto.type = "image/*"
        pickPhoto.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(pickPhoto, GALLERY_REQUESTCODE)
    }

    private fun openCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, CAMERA_REQUESTCODE)
    }

    private fun setClick() {

        butSwitch.setOnClickListener {

            if (butSwitch.isChecked) {
                if (checkPermissionLocation()) {
                    task = fusedLocationProviderClient.lastLocation
                    task!!.addOnSuccessListener(OnSuccessListener<Location?> { location ->
                        if (location != null) {

                            profileVM?.automaticLocationUpdate(
                                this,
                                location.latitude.toString(),
                                location.longitude.toString(),
                                "1",
                                getAddressFromLatLng(location.latitude, location.longitude)
                            )
                        }
                    })

                } else {
                    requestLocationPermission()
                }


            } else {
                profileVM?.automaticLocationUpdate(
                    this,
                    "",
                    "", "0", ""
                )
            }
            /*   if (!isNetworkConnected()) {
                   showInternetToast()
               } else {
                   profileVM?.automaticLocationUpdate(this,"","")
               }*/
        }
        dp.setOnClickListener {
            if (checkPermission()) {
                //  chooseImage(this)

                pickImage()

            } else {
                requestPermission()
            }
        }
        // image picker from gallery and camera functionality
        cameraImage.setOnClickListener {
            if (checkPermission()) {
                //chooseImage(this)
                pickImage()

            } else {
                requestPermission()
            }
        }

        // open date of birth picker
        dateBirth.setOnClickListener {
            openDatePickerDialog(this, dob, dateBirth)
        }

        selectAddress.setOnClickListener {
            // open location picker
            openPlacePicker()
        }

        privacyPolicy.setOnClickListener {
            val i = Intent("android.intent.action.VIEW")
            i.data = Uri.parse(/*BASE_URL_ONE + "club/login/privacy_policy"*/PRIVACY_POLICY_URL)
            startActivity(i)
        }

        termsOfUse.setOnClickListener {
            val i = Intent("android.intent.action.VIEW")
            i.data = Uri.parse(/*BASE_URL_ONE + "club/login/terms_and_conditions"*/TERMS_AND_CONDITIONS)
            startActivity(i)
        }

        tvChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        tvSave.setOnClickListener {
            if (!isNetworkConnected()) {
                showInternetToast()
            } else {
                if (etName.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_name))
                } else if (selectGender.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_select_gender))
                } /*else if (dateBirth.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_enter_dob))
                }*/ else if (selectAddress.text.toString().isEmpty()) {
                    showToast(this, getString(R.string.please_select_address))
                } else if (profileImage == "") {
                    showToast(this, getString(R.string.please_select_profile_photo))
                } else {
                    updateDataProfile(profileImage)

                }
            }
        }
    }

    fun openDatePickerDialog(context: Context, date: String, etText: EditText) {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        /* Log.e("date from picker","--"+date)
         if (date.isNotEmpty()) {
             val dateFromApi =  dateFormatConversionPutPattern(date,*//*"dd MMMM yyyy"*//*"dd-MM-yyyy","dd/MM/yyyy")
            val splitDate = dateFromApi.split("/")
            day = splitDate[0].toInt()
            month = splitDate[1].toInt() - 1
            year = splitDate[2].toInt()
        }*/

        val dpd = DatePickerDialog(
            context, R.style.DialogThemeDatePicker, { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in rotextbox

                var datee = dayOfMonth.toString() + " " + (monthOfYear + 1) + " " + year



                profileData?.let {
                    profileData?.language_type?.let {
                        if (profileData?.language_type.equals("0")) {
                            dob = dateFormatConversionLocale(datee, "en")
                            etText.setText(dob)
                        } else {
                            dob = dateFormatConversionLocale(datee, "el")
                            etText.setText(dob)
                        }
                    }
                }

                dobApi = dateToApi(dateFormatConversion(datee))
            }, year, month, day
        )
        val futureSixMonths = Calendar.getInstance()
        futureSixMonths.add(Calendar.YEAR, -13)
        dpd.datePicker.maxDate = futureSixMonths.timeInMillis
        //  dpd.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        dpd.show()
    }

    private fun openPlacePicker() {
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    fun dateFormatWithMonthNameFull(date: String, locale: String): String {
        val myFormat = SimpleDateFormat("dd-MM-yyyy").parse(date)
        var myDate = SimpleDateFormat("dd MMMM yyyy", Locale(locale)).format(myFormat)
        return myDate
    }

    fun updateDataProfile(mImagePth: String) {

        Log.e("mImagePth", "--" + mImagePth)
        //    val dob =  dateToApi(dateBirth.text.toString())

        var mImageParts: MultipartBody.Part? = null

        if (mImagePth != null && !mImagePth.isEmpty() && !mImagePth.toString().startsWith("http")) {

            val file = File(mImagePth)
            val requestFile =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            mImageParts =
                MultipartBody.Part.createFormData("image_file", file.name, requestFile)

        } else {
            val requestFile =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            mImageParts =
                MultipartBody.Part.createFormData("image_file", "", requestFile)
            //  mImageParts  =null
        }

        val multipartName =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                etName.text.toString()
            ) // Parameter request body

        val multipartGender =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                /*selectGender.text.toString()*/ gender.toString()
            )

        val multipartPassword =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                etPass.text.toString()
            )

        val multipartDateBirth =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                /* dateBirth.text.toString()*/ dobApi
            )

        val multipartAddress =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                selectAddress.text.toString()
            )

        val multipartLatitude =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                lattitue!!
            )

        val multipartLongitude =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                longitude!!
            )

        // call api update profile

        // if (mImageParts != null) {

        profileVM?.updateProfileData(
            this, multipartName, multipartGender,
            multipartPassword, multipartDateBirth, multipartAddress, multipartLatitude,
            multipartLongitude, mImageParts
        )
        //  }

    }

    fun updateDataProfileWithoutImage(mImagePth: String) {

        Log.e("mImagePth", "--" + mImagePth)

        var mImageParts: MultipartBody.Part? = null

        if (mImagePth != null && !mImagePth.isEmpty() && !mImagePth.toString().startsWith("http")) {

            val file = File(mImagePth)
            val requestFile =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            mImageParts =
                MultipartBody.Part.createFormData("image_file", file.name, requestFile)

        } else {
            val requestFile =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "")
            mImageParts =
                MultipartBody.Part.createFormData("image_file", "", requestFile)
            //  mImageParts  =null
        }


        // call api update profile

        // if (mImageParts != null) {

        profileVM?.updateProfileDataImageOnly(
            this, mImageParts
        )
        //  }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //   when (requestCode) {
                if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
                    data.let {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        selectAddress.setText(place.address)

                        Log.d("Place", "PlaceComponent: ${place}, ${place.id}")
                        Log.d(
                            "Place",
                            "latlng" + place.latLng?.latitude + "," + place.latLng?.longitude
                        )

                        lattitue = place.latLng!!.latitude.toString()
                        longitude = place.latLng!!.longitude.toString()
                        // get the required fields using geocoder
                        val geoCoder = Geocoder(this, Locale.getDefault())
                        var address = geoCoder.getFromLocation(
                            place.latLng!!.latitude,
                            place.latLng!!.longitude,
                            1
                        )

                        Log.d("AddressDetails", address[0].toString())

                        // set zipCode
                        if (address[0].postalCode != null) {
                            //  binding.zipCode.setText(address[0].postalCode)
                        }

                        // set state
                        if (address[0].adminArea != null) {
                            //binding.state.setText(address[0].adminArea)
                        }

                        // set city
                        if (address[0].locality != null) {
                            // binding.cityName.setText(address[0].subAdminArea)
                        } else {
                            //   binding.cityName.setText(address.get(0).locality)
                        }
                    }
                } else {

                    val result = data?.data
                    if (resultCode == Activity.RESULT_OK) {
                        result?.let {
                            val resultUri: Uri = result
                            // profile.set(resultUri.getPath())
                            //   AppUtils.roundImageWithGlide(imageView, profilePic)
                            profileImage = getPath(this, resultUri)
                            val selectedImage = resultUri.path
                            Glide.with(this).load(selectedImage).error(R.drawable.ic_user)
                                .placeholder(R.drawable.ic_user).into(dp)

                            if (profileImage == "") {
                                showToast(this, getString(R.string.please_select_profile_photo))
                            } else {
                                updateDataProfile(profileImage)

                            }
                        }


                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        val error = ImagePicker.getError(data)
                        Log.e("err", "--" + error)
                    }
                }
                /*  AUTOCOMPLETE_REQUEST_CODE -> {

                  }*/

                /*  CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {

                  }*/
                /*  CAMERA_REQUESTCODE -> {
                      val selectedImage = data?.extras?.get("data") as Bitmap
                      dp.setImageBitmap(selectedImage)
                      var image_Uri = getImageUriFromBitmap(this, selectedImage)
                      Glide.with(this).load(image_Uri).error(R.drawable.ic_user)
                          .placeholder(R.drawable.ic_user).into(dp)

                      profileImage = getPath(this, image_Uri)

                  }
                  GALLERY_REQUESTCODE -> {
                      val selectedImage = data?.data as Uri
                      Glide.with(this).load(selectedImage).error(R.drawable.ic_user)
                          .placeholder(R.drawable.ic_user).into(dp)

                      //    dp.setImageURI(selectedImage)
                      profileImage = getPath(this, selectedImage)

                  }*/
                //  }
            }
            AutocompleteActivity.RESULT_ERROR -> {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    //  Log.i(TAG, status.statusMessage)
                }
            }
            Activity.RESULT_CANCELED -> {
                // The user canceled the operation.
            }
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    private fun setSpinner() {
        // select Gender spinner
        selectGender.setOnSpinnerItemSelectedListener(object :
            OnSpinnerItemSelectedListener<String> {
            override fun onItemSelected(
                oldIndex: Int, oldItem: String?,
                newIndex: Int,
                newItem: String
            ) {
                if (newIndex == oldIndex) {
                } else {
                    if (newItem.equals("Man")) {
                        gender = 1

                    } else {
                        gender = 2

                    }
                }

//                showToast(this@SettingActivity,"OldIndex$oldIndex")
//                showToast(this@SettingActivity,"OldItem$oldItem")
//                showToast(this@SettingActivity,"newIndex$newIndex")
//                showToast(this@SettingActivity,"newItem$newItem")
            }
        })

        // select language spinner
        languageSelector.setOnSpinnerItemSelectedListener(object :
            OnSpinnerItemSelectedListener<String> {
            override fun onItemSelected(
                oldIndex: Int, oldItem: String?,
                newIndex: Int, newItem: String
            ) {
                if (newIndex == oldIndex) {
                } else {
                    if (newIndex == 1) {
                        profileVM?.changeLanguageUpdate(this@SettingActivity)


                    } else {
                        profileVM?.changeLanguageUpdate(this@SettingActivity)
                    }
                }
            }
        })
    }


    private fun setToolbar() {
        toolbarId.ivBack.setOnClickListener {
            onBackPressed()
        }
        toolbarId.tvTitle.visibility = View.VISIBLE
        toolbarId.tvTitle.setText(getString(R.string.menu_seven))
        toolbarId.separator.visibility = View.VISIBLE
    }

    fun getAddressFromLatLng(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(this, Locale.getDefault())
        var address = geoCoder.getFromLocation(
            lat,
            lng,
            1
        )


        Log.d("AddressDetails", address[0].toString())
        addressFull = address[0].getAddressLine(0)
        // set zipCode
        if (address[0].postalCode != null) {
            //  binding.zipCode.setText(address[0].postalCode)
        }

        // set state
        if (address[0].adminArea != null) {
            //binding.state.setText(address[0].adminArea)
        }

        // set city
        if (address[0].locality != null) {
            // binding.cityName.setText(address[0].subAdminArea)
        } else {
            //   binding.cityName.setText(address.get(0).locality)
        }
        return addressFull
    }

}