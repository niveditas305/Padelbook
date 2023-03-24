package snow.app.padelbook.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_home.*
import snow.app.padelbook.R
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.ui.*
import snow.app.padelbook.ui.HomeActivity.Companion.lastSelectedTab
import snow.app.padelbook.viewModel.LogoutVM
import snow.app.padelbook.viewModel.NotifyCountReadVM
import kotlinx.android.synthetic.main.fragment_menu.view.*
import snow.app.padelbook.common.BASE_URL_ONE

class MenuFragment(var mContext: Context) : BottomSheetDialogFragment() {
    private var logout: LogoutVM? = null
    private var loginPref: SessionClass? = null
    var mView: View? = null
    private var notifyCount: NotifyCountReadVM? = null
    var ondismissClick: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_menu, container, false)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logout = LogoutVM()
        loginPref = SessionClass(requireContext())
        notifyCount = NotifyCountReadVM()

        if ((mContext as HomeActivity).isNetworkConnected()) {
            notifyCount?.countNotify(requireContext())
        } else {
            (mContext as HomeActivity).showInternetToast()
        }

        initListners()
        setClick()
        observerLogout()
    }

    private fun observerLogout() {
        // logout api response
        logout?.userLogout?.observe(this, Observer { user ->
            if (user.status) {
                (mContext as HomeActivity).showToast(requireContext(), user.message)
                lastSelectedTab = -1
                loginPref?.clearSession()
                loginPref?.isLogin = false
                ondismissClick?.invoke("")
                startActivity(Intent(requireContext(), WelcomeScreen::class.java))
                requireActivity().finishAffinity()

            } else {
                (mContext as HomeActivity).showToast(requireContext(), user.message)
            }
        })

        // notification Count response
        notifyCount?.notifyCount?.observe(this, Observer { user ->
            if (user != null) {
                if (user.status) {
                    if (!user.data.equals("0")) {
                        mView?.tvCount?.visibility = View.VISIBLE
                        mView?.tvCount?.setText(user.data)
                    } else {
                        mView?.tvCount?.visibility = View.GONE
                    }
                } else {
                    (mContext as HomeActivity).showToast(requireContext(), user.message)
                }
            } else {
                (mContext as HomeActivity).showToast(requireContext(), "Something went wrong")
            }
        })

    }

    private fun setClick() {
        mView?.notifySec?.setOnClickListener {
            dialog?.dismiss()
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }
        mView?.bookingSec?.setOnClickListener {
            dialog?.dismiss()
            startActivity(Intent(requireContext(), BookingActivity::class.java))
        }
        mView?.contactSec?.setOnClickListener {

            startActivity(Intent(requireActivity(), ContactActivity::class.java))
            dialog?.dismiss()
        }
        mView?.resultSec?.setOnClickListener {
            dialog?.dismiss()
            startActivity(Intent(requireContext(), ResultsActivity::class.java))
            // startActivity(Intent(requireContext(), AddResultActivity::class.java))
        }
        mView?.settingSec?.setOnClickListener {
            dialog?.dismiss()
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }
        mView?.llClubPass?.setOnClickListener {
            dialog?.dismiss()
            startActivity(Intent(requireContext(), ClubPassListActivity::class.java))
        }
        mView?.secLogOut?.setOnClickListener {
            showLogOut()
        }
    }

    private fun showLogOut() {
        val alertdialog = AlertDialog.Builder(requireContext())
        alertdialog.setTitle(getString(R.string.are_you_sure_you_want_to_logout))
        alertdialog.setCancelable(false)

        alertdialog.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            if ((mContext as HomeActivity).isNetworkConnected()) {
                logout?.logoutData(requireContext())
            } else {
                (mContext as HomeActivity).showInternetToast()
            }
        }
        alertdialog.setNegativeButton(getString(R.string.no),
            object : DialogInterface.OnClickListener {
                override fun onClick(alertdialog: DialogInterface?, which: Int) {
                    alertdialog?.dismiss()
                }
            })
        alertdialog.show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        ondismissClick?.invoke("")
        /* (mContext as HomeActivity).tabLayout.getTabAt(lastSelectedTab)?.select()
         dialog.dismiss()*/
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    private fun initListners() {
        mView?.helpLayout?.setOnClickListener {
            dialog?.dismiss()
            startActivity(Intent(requireContext(), IntroScreenActivity::class.java))
/*
if ((mContext as HomeActivity).isNetworkConnected()){
    val i = Intent("android.intent.action.VIEW")
    i.data = Uri.parse("http://padelbook.gr/help/")
    startActivity(i)
}else{
    (mContext as HomeActivity).showInternetToast()
}
*/

        }
    }
}