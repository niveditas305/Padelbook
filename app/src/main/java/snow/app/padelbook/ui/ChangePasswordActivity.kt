package snow.app.padelbook.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_changepassword.*
import kotlinx.android.synthetic.main.toolbar.*
import snow.app.padelbook.R
import snow.app.padelbook.viewModel.ChangePasswordVM

class ChangePasswordActivity : BaseActivity() {
    private var changePassVM : ChangePasswordVM ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepassword)

        changePassVM = ChangePasswordVM()

        setToolbar()
        setClick()
        observer()
    }

    private fun observer() {
        // change password api response
        changePassVM?.changePass?.observe(this, Observer { user ->
            if(user.status){
                showToast(this, user.message)
                onBackPressed()
            }
            else{
                showToast(this, user.message)
            }
        })
    }

    private fun setClick() {
        tvSubmit.setOnClickListener {
            if(!isNetworkConnected()){
                showInternetToast()
            }
            else{
                if(etOldPass.text.toString().isEmpty()){
                    showToast(this,"Please enter old password")
                }
                else if(etNewPass.text.toString().isEmpty()){
                    showToast(this,"Please enter new password")
                }
                else if(etConfirmPass.text.toString().isEmpty()){
                    showToast(this,"Please enter confirm password")
                }
                else if(etConfirmPass.text.toString() != etNewPass.text.toString() ){
                    showToast(this,"New password and confirm password should be same.")
                }
                else{
                    // change password api implementation
                    changePassVM?.changePassword(this,etOldPass.text.toString(),etNewPass.text.toString())
                }
            }
        }
    }

    private fun setToolbar() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvTitle.visibility = View.VISIBLE
        tvTitle.setText(getString(R.string.change_password_two))
        separator.visibility = View.VISIBLE
    }
}