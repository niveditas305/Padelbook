package snow.app.padelbook.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.toolbar.view.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.FavouriteAdapter
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.clubList.ClubData
import snow.app.padelbook.network.responses.favourite.Data
import snow.app.padelbook.viewModel.FavouriteVM
import snow.app.padelbook.viewModel.LikeUnlikeVM
import kotlinx.android.synthetic.main.fragment_favoruit.view.*

class FavoruitFragment : BaseFragment(), FavouriteListener {
    private var favouriteVm: FavouriteVM? = null
    private var likeUnlikeVm: LikeUnlikeVM? = null


    private var adapter: FavouriteAdapter? = null
    private var mContext: Context? = null
    var arrayList: ArrayList<ClubData> = ArrayList()
    var removeFromList: ClubData? = null
    var mView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_favoruit, container, false)
        return mView
    }

    override fun onAttach(context: Activity) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // <---------initialize view models ------------>
        favouriteVm = FavouriteVM()
        likeUnlikeVm = LikeUnlikeVM()
        // </--------------------------------->


        //< -------------api call of favourite list -------->
        if (!isNetworkConnected()) {
            showInternetToast()
        } else {
            favouriteVm?.favouriteData(mContext!!)
        }
        // </----------------------->


        setToolbar()
        observer()
    }

    private fun observer() {
        // favourite api response
        favouriteVm?.favList?.observe(requireActivity(), Observer { user ->
            if (user != null) {
                if (user.status) {
                    //   showToast(user.message)
                    arrayList.clear()
                    arrayList.addAll(user.data)

                    if (arrayList.size > 0) {
                        if (mView?.tvEmptyData != null) {
                            mView?.llNoResultsFound?.visibility = View.GONE
                            mView?.recycleFav?.visibility = View.VISIBLE
                        }
                    } else {
                        mView?.llNoResultsFound?.visibility = View.VISIBLE
                        mView?.recycleFav?.visibility = View.GONE
                    }
                    setAdapter()
                } else {
                    if (arrayList.size > 0) {
                        if (mView?.tvEmptyData != null) {
                            mView?.llNoResultsFound?.visibility = View.GONE
                            mView?.recycleFav?.visibility = View.VISIBLE
                        }
                    } else {
                        mView?.llNoResultsFound?.visibility = View.VISIBLE
                        mView?.recycleFav?.visibility = View.GONE
                    }
                    //  showToast(user.message)
                }
            } else {
                showToast(getString(R.string.something_went_wrong))
            }
        })

        // fav/unfav api response
        likeUnlikeVm?.likeUnlike?.observe(requireActivity(), Observer { user ->
            if (user.status) {
                arrayList.remove(removeFromList)    // remove clicked item from list

                if (arrayList.size > 0) {
                    mView?.llNoResultsFound?.visibility = View.GONE
                    mView?.recycleFav?.visibility = View.VISIBLE
                } else {
                    mView?.llNoResultsFound?.visibility = View.VISIBLE
                    mView?.recycleFav?.visibility = View.GONE
                }

                adapter?.notifyDataSetChanged()
                showToast(user.message)
            } else {
                showToast(user.message)
            }
        })
    }

    private fun setToolbar() {
        mView?.toolbarId?.ivBack?.visibility = View.GONE
        mView?.toolbarId?.tvTitle?.visibility = View.VISIBLE
        mView?.toolbarId?.separator?.visibility = View.VISIBLE
        mView?.toolbarId?.tvTitle?.setText(getString(R.string.favourites))
    }

    private fun setAdapter() {

        adapter = FavouriteAdapter(mContext!!, arrayList, this)
        if (mView?.recycleFav != null) {
            mView?.recycleFav?.adapter = adapter
        }
    }

    override fun onIconClick(position: Int, clubId: String) {

    }

    override fun onFavClick(position: Int, data: ClubData) {
        removeFromList = data

        //<----------api call of fav/unfav api------------------>
        if (isNetworkConnected()) {
            likeUnlikeVm?.likeUnlikeData(mContext!!, data.club_id)

        } else {
            showInternetToast()
        }
        // </------------------------------------------------------>
    }
}