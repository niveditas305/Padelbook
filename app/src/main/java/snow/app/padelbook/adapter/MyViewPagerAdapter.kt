package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.layout_banner.view.*
import snow.app.padelbook.R
import snow.app.padelbook.common.Utils.loadImage
import snow.app.padelbook.network.responses.profileresponse.CoverImage

class MyViewPagerAdapter( var context: Context,val imageList: ArrayList<CoverImage>) : PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

        val view =
            layoutInflater!!.inflate(R.layout.layout_banner, container, false)

        if(imageList[position].club_image != null) {
            loadImage(imageList[position].club_image, context, view.imgCate)
        }

        container.addView(view)
        return view
    }

    override fun getCount() = imageList.size

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val view = `object` as View
        container.removeView(view)

    }

}