package snow.app.padelbook.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import snow.app.padelbook.R
import snow.app.padelbook.fragments.*
import snow.app.padelbook.session.SessionClass
import com.google.android.material.badge.BadgeDrawable
import kotlinx.android.synthetic.main.fragment_menu.*
import snow.app.padelbook.viewModel.NotifyCountReadVM


class HomeActivity : BaseActivity() {
    private var notifyCount: NotifyCountReadVM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notifyCount = NotifyCountReadVM()
        setContentView(R.layout.activity_home)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment())
            .commit()
        setClick()
        setCollpasToolbar()

        if (SessionClass(this).loginData.language_type == "0") {
            changeLanguage("en")
        } else {
            changeLanguage("el")
        }
        observers()
    }

    private fun setCollpasToolbar() {
    }


    fun observers() {
        // notification Count response
        notifyCount?.notifyCount?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    if (!user.data.equals("0")) {
                        //   tvCount.visibility = View.VISIBLE
                        //    tvCount.setText(user.data)

                        val badgeDrawable = tabLayout.getTabAt(4)!!.orCreateBadge
                        badgeDrawable.backgroundColor = ContextCompat.getColor(this,R.color.theme_blue)
                        badgeDrawable.isVisible = true
                        badgeDrawable.number = user.data.toInt()
                    } else {
                        val badgeDrawable = tabLayout.getTabAt(4)!!.orCreateBadge
                        badgeDrawable.backgroundColor = ContextCompat.getColor(this,R.color.theme_blue)
                        badgeDrawable.isVisible = false
                        //  badgeDrawable.number = user.data.toInt()
                    }
                } else {
                    //     (mContext as HomeActivity).showToast(requireContext(),user.message)
                }
            } else {
                //   (mContext as HomeActivity).showToast(requireContext(),"Something went wrong")
            }
        })

    }

    override fun onResume() {
        super.onResume()
        if (isNetworkConnected()) {
            notifyCount?.countNotify(this)
        } else {
            showInternetToast()
        }
    }
    private fun setClick() {
        //set the badge


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {
                    0 -> {
                        lastSelectedTab = 0
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment())
                            .addToBackStack(null)
                            .commit()

                    }
                    1 -> {
                        lastSelectedTab = 1
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, SearchFragment())
                            .commit()
                    }
                    2 -> {
                        lastSelectedTab = 2
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CreateGameActivity())
                            .commit()
                    }
                    3 -> {
                        lastSelectedTab = 3
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, FavoruitFragment())
                            .commit()
                    }
                    4 -> {
                        val bottomSheetFragment = MenuFragment(this@HomeActivity)
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        lastSelectedTab = 0
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment())
                            .commit()
                    }
                    1 -> {
                        lastSelectedTab = 1
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, SearchFragment())
                            .commit()
                    }
                    2 -> {
                        lastSelectedTab = 2
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, CreateGameActivity())
                            .commit()
                    }
                    3 -> {
                        lastSelectedTab = 3
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, FavoruitFragment())
                            .commit()
                    }
                    4 -> {
                        val bottomSheetFragment = MenuFragment(this@HomeActivity)
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                        bottomSheetFragment.ondismissClick = {
                            bottomSheetFragment.dismiss()
                            //tabLayout.getTabAt(lastSelectedTab)?.select()

                        }
                    }
                }
            }

        })
    }

    companion object {
        var lastSelectedTab = 0

    }

    fun loadFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SearchFragment())
            .commit()
    }

    override fun onBackPressed() {
        if (getCurrentFragment() != null && getCurrentFragment() is HomeFragment) {
super.onBackPressed()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }

    }
    fun getCurrentFragment(): Fragment? {
        return supportFragmentManager
            .findFragmentById(R.id.container)
    }

}