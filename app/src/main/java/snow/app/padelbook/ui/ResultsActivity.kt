package snow.app.padelbook.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.toolbar.view.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.ResultAdapter
import snow.app.padelbook.network.responses.matchResult.matchResultNEw.MatchResultData
import snow.app.padelbook.viewModel.MatchResultVM

class ResultsActivity : BaseActivity() {
    private var adapter: ResultAdapter? = null
    private var matchResultVM: MatchResultVM? = null
    private var matchList: ArrayList<MatchResultData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        matchResultVM = MatchResultVM()

        // < ------------ match result --------->
        if (!isNetworkConnected()) {
            showToast(this, "")
        } else {
            matchResultVM?.matchResult(this, "")
        }
        // <------------------------------------>

        setToolbar()
        setTabData()

        listener()
    }

    private fun listener() {
        matchResultVM?.matchResult?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    if (user.data != null) {
                        matchList.clear()
                        if (user.data.size > 0) {
                            matchList.addAll(user.data)
                            recycleResults.visibility = View.VISIBLE
                            llNoResultsFound.visibility = View.GONE

                        } else {
                            recycleResults.visibility = View.GONE
                            llNoResultsFound.visibility = View.VISIBLE
                            if (tabLayout.selectedTabPosition == 0) {
                                tvEmptyText.setText(getString(R.string.no_score_added_yet))
                            } else if (tabLayout.selectedTabPosition == 1) {
                                tvEmptyText.setText(getString(R.string.no_maches_yet))
                            } else if (tabLayout.selectedTabPosition == 2) {
                                tvEmptyText.setText(getString(R.string.no_match_lost_yet))
                            }
                        }
                        setAdapter()
                    } else {
                        recycleResults.visibility = View.GONE
                        llNoResultsFound.visibility = View.VISIBLE
                        if (tabLayout.selectedTabPosition == 0) {
                            tvEmptyText.setText(getString(R.string.no_score_added_yet))
                        } else if (tabLayout.selectedTabPosition == 1) {
                            tvEmptyText.setText(getString(R.string.no_maches_yet))
                        } else if (tabLayout.selectedTabPosition == 2) {
                            tvEmptyText.setText(getString(R.string.no_match_lost_yet))
                        }
                       // showToast(this, user.message)
                    }
                } else {
                    recycleResults.visibility = View.GONE
                    llNoResultsFound.visibility = View.VISIBLE
                    if (tabLayout.selectedTabPosition == 0) {
                        tvEmptyText.setText(getString(R.string.no_score_added_yet))
                    } else if (tabLayout.selectedTabPosition == 1) {
                        tvEmptyText.setText(getString(R.string.no_maches_yet))
                    } else if (tabLayout.selectedTabPosition == 2) {
                        tvEmptyText.setText(getString(R.string.no_match_lost_yet))
                    }
                    //showToast(this, user.message)
                }
            } else {
                showToast(this, "Something went wrong")
            }
        })
    }

    private fun setTabData() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // < ------------ match result --------->
                        if (!isNetworkConnected()) {
                            showToast(this@ResultsActivity, "")
                        } else {
                            matchResultVM?.matchResult(this@ResultsActivity, "")
                        }
                        // <------------------------------------>
                    }
                    1 -> {
                        // < ------------ match result --------->
                        if (!isNetworkConnected()) {
                            showToast(this@ResultsActivity, "")
                        } else {
                            matchResultVM?.matchResult(this@ResultsActivity, "1")
                        }
                        // <------------------------------------>
                    }
                    2 -> {
                        // < ------------ match result --------->
                        if (!isNetworkConnected()) {
                            showToast(this@ResultsActivity, "")
                        } else {
                            matchResultVM?.matchResult(this@ResultsActivity, "2")
                        }
                        // <------------------------------------>

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun setAdapter() {
        adapter = ResultAdapter(this, matchList)
        recycleResults.adapter = adapter
    }

    private fun setToolbar() {
        toolbarId.ivBack.setOnClickListener {
            onBackPressed()
        }
        toolbarId.tvTitle.visibility = View.VISIBLE
        toolbarId.separator.visibility = View.VISIBLE
        toolbarId.tvTitle.setText(getString(R.string.menu_six))
    }
}