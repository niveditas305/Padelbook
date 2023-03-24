package snow.app.padelbook.ui


import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_category_detail.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.MyViewPagerAdapter
import snow.app.padelbook.fragments.CourtFragment
import snow.app.padelbook.fragments.MatchFragment
import snow.app.padelbook.fragments.ProfileFragment
import snow.app.padelbook.network.responses.profileresponse.ClubProfile
import snow.app.padelbook.network.responses.profileresponse.CoverImage
import snow.app.padelbook.viewModel.ProfileResponseVM

class CategoryDetailActivity : BaseActivity() {
    private var viewPagerAdapter: MyViewPagerAdapter? = null
    private var ClubID: String? = null
    private var profileVM: ProfileResponseVM? = null
    var imageList: ArrayList<CoverImage> = ArrayList()
    var profileData: ClubProfile? = null
    var CALL_FROM = ""
    var scheduleID: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_category_detail)


        ClubID = intent.extras?.get("club_id") as String
        CALL_FROM = intent?.extras?.get("callFrom") as String
        scheduleID = intent?.extras?.get("scheduleID") as String?
        profileVM = ProfileResponseVM()

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, CourtFragment(ClubID!!, CALL_FROM, scheduleID))
            .commit()


        // <----------------- club image data and profile detail api
        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            profileVM?.profileData(this, ClubID!!)
        }

        // </------------------------------------------>

        setClick()
        setTabLayout()
       statusBarTransparent()
        observer()

    }

    private fun observer() {
        profileVM?.userProfile?.observe(this, Observer { user ->
            if (user.status) {
                imageList.clear()
                imageList.addAll(user.data.cover_image)

                setViewPagerAdapter()
                 if (user.data.club_name != null) {
                    tvTitleTwo.setText(user.data.club_name)
                }

                if (user.data.club_profile != null) {
                    profileData = user.data.club_profile
                }

            } else {
                showToast(this, user.message)
            }
        })
    }

    private fun setViewPagerAdapter() {
        viewPagerAdapter = MyViewPagerAdapter(this, imageList)
        viewPager.adapter = viewPagerAdapter
        dots_indicator.setViewPager(viewPager)
    }

    private fun setClick() {
        imgBackBut.setSafeOnClickListener {
            onBackPressed()
        }
    }

    private fun setTabLayout() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        Log.e("ClubID", "" + ClubID)
                        Log.e("CALL_FROM", "" + CALL_FROM)
                        Log.e("scheduleID", "" + scheduleID)
                        if (scheduleID == null) {
                            scheduleID = ""
                        }

                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.frameContainer,
                                CourtFragment(ClubID!!, CALL_FROM, scheduleID!!)
                            )
                            .commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameContainer, MatchFragment(ClubID!!))
                            .commit()
                    }
                    2 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameContainer, ProfileFragment(profileData!!))
                            .commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        Log.e("ClubID", "" + ClubID)
                        Log.e("CALL_FROM", "" + CALL_FROM)
                        Log.e("scheduleID", "" + scheduleID)
                        if (scheduleID == null) {
                            scheduleID = ""
                        }

                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.frameContainer,
                                CourtFragment(ClubID!!, CALL_FROM, scheduleID!!)
                            )
                            .commit()
                    }
                    1 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameContainer, MatchFragment(ClubID!!))
                            .commit()
                    }
                    2 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frameContainer, ProfileFragment(profileData!!))
                            .commit()
                    }
                }
            }

        })

    }

}