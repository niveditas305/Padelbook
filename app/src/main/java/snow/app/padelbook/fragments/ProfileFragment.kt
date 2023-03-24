package snow.app.padelbook.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_profile.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.ClubFacilityAdapter
import snow.app.padelbook.adapter.DaysTimeScheduleAdapter
import snow.app.padelbook.databinding.FragmentProfileBinding
import snow.app.padelbook.network.responses.profileresponse.ClubProfile

class ProfileFragment(var profileData : ClubProfile) : BaseFragment(), OnMapReadyCallback {

    lateinit var  binding : FragmentProfileBinding
    var googleMap: GoogleMap? = null
    private var adapter : ClubFacilityAdapter ?= null
    private var daysTimeAdapter : DaysTimeScheduleAdapter?= null


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container,false)
        initMap(savedInstanceState)
        setAdapter()
        setUpData()

        if(profileData.aminity.isNotEmpty()){
            binding.aminitiesSec.visibility = View.VISIBLE
            binding.rvClubFacility.visibility = View.VISIBLE
        }
        else{
            binding.aminitiesSec.visibility = View.GONE
            binding.rvClubFacility.visibility = View.GONE
        }

        return binding.root

    }

    private fun setUpData() {

        if(profileData.favourite_status){
            binding.imgCard.setImageResource(R.drawable.ic_heart_white)
        }
        else{
            binding.imgCard.setImageResource(R.drawable.ic_icon_awesome_heart_1)
        }

        if(profileData.club_image != null) {
            Glide.with(this).load(profileData.club_image)
                .error(R.drawable.ic_user).placeholder(R.drawable.ic_user).into(binding.profileImage)
        }

        profileData.description?.let {
            binding.descriptionText.setText(profileData.description)
        }?:kotlin.run {
            binding.descriptionText.setText("N/A")
        }

        // <----------website click link----->
        binding.websiteSec.setOnClickListener {

            if(profileData.website != null){
                val i = Intent("android.intent.action.VIEW")
                if(profileData.website.startsWith("https") ||
                    profileData.website.startsWith("http") ) {
                    i.data = Uri.parse(profileData.website)
                }
                else{
                    i.data = Uri.parse("https://"+profileData.website)
                }
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    requireContext().startActivity(i)
            }
        }  // </------------------------------------>


        // <------------phone click link ----------->
        binding.phoneSec.setOnClickListener {
            if(profileData.phone != null){
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:"+profileData.phone)
                activity?.startActivity(dialIntent)
            }
        }
        // </-------------------------------------->


        // <-------------directions click --------->
        binding.navigationSec.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q="+profileData.latitude+","+profileData.longitude)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
        // </------------------------------------>

    }

    private fun setAdapter() {
        adapter = ClubFacilityAdapter(requireContext(),profileData.aminity)
        binding.rvClubFacility.adapter = adapter

        daysTimeAdapter = DaysTimeScheduleAdapter(requireContext(),profileData.availability)
        binding.recycleDaysTime.adapter = daysTimeAdapter
    }

    private fun initMap(savedInstanceState: Bundle?) {
        binding.mapFragment.onCreate(savedInstanceState)
        binding.mapFragment.onResume()
        binding.mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap= p0
        googleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
    }


}