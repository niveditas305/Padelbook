package snow.app.padelbook.ui

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_players.*
import kotlinx.android.synthetic.main.toolbar.view.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.PlayersAdapter

class AddPlayersActivity : BaseActivity() {
    private var adapter : PlayersAdapter ?= null
    var inviteRequestList : ArrayList<String> = ArrayList()
    var requestList : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_players)
        inviteRequestList.clear()
        requestList.clear()

        setClick()
        setToggleButton()
    }



    private fun setToggleButton() {
        if(butSwitch.isChecked){
            setRequestAdapter()
        }
        else{
            setInviteRequestAdapter()
        }
    }

    private fun setInviteRequestAdapter() {
        inviteRequestList.add(getString(R.string.invite))
        inviteRequestList.add(getString(R.string.request_text))
        inviteRequestList.add(getString(R.string.invite))
        inviteRequestList.add(getString(R.string.invite))
        inviteRequestList.add(getString(R.string.request_text))
        inviteRequestList.add(getString(R.string.request_text))
        inviteRequestList.add(getString(R.string.invite))
        inviteRequestList.add(getString(R.string.request_text))
        inviteRequestList.add(getString(R.string.invite))
        adapter = PlayersAdapter(this,inviteRequestList)
        recyclePlayers.adapter = adapter
    }

    private fun setRequestAdapter() {
        requestList.add(getString(R.string.request_text))
        requestList.add(getString(R.string.request_text))
        requestList.add(getString(R.string.request_text))
        requestList.add(getString(R.string.request_text))
        requestList.add(getString(R.string.request_text))
        adapter = PlayersAdapter(this,requestList)
        recyclePlayers.adapter = adapter
    }


    private fun setClick() {
        toolbarID.ivBack.setSafeOnClickListener {
            onBackPressed()
        }
        toolbarID.separator.visibility = View.VISIBLE
        toolbarID.tvTitle.visibility = View.VISIBLE
        toolbarID.tvTitle.setText(getString(R.string.add_players))

        butSwitch.setSafeOnClickListener {
           setToggleButton()
        }
    }



}