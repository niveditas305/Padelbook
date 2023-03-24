package snow.app.padelbook.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import kotlinx.android.synthetic.main.items_home.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.homeList.Data
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.ui.CategoryDetailActivity
import snow.app.padelbook.ui.HomeActivity
import snow.app.padelbook.utils.SafeClickListener

class HomeAdapterFirst(var context: Context, val courtDataList: ArrayList<Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val handler: Handler? = Handler()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        /*    handler!!.postDelayed(Runnable {
                holder.itemView.imageData.visibility = View.VISIBLE
                holder.itemView.shimmer_view_container2.stopShimmer()
                holder.itemView.shimmer_view_container2.visibility=View.GONE
           }, 3000)*/

        if (courtDataList.size > 5) {
            if (position == courtDataList.size   ) {

                holder.itemView.lastIndex.visibility = View.VISIBLE
                holder.itemView.mainContent.visibility = View.GONE
                holder.itemView.lastIndex.setSafeOnClickListener {
                    (context as HomeActivity).loadFragment()
                }
            } else {
                holder.itemView.lastIndex.visibility = View.GONE
                holder.itemView.mainContent.visibility = View.VISIBLE
                if (courtDataList.get(position).club_name != null) {
                    holder.itemView.tvClubName.setText(courtDataList.get(position).club_name)
                }

                if (courtDataList.get(position).club_image != null) {
                    holder.itemView.shimmer_view_container2.startShimmer()
                    Glide.with(context)
                        .load(courtDataList.get(position).club_image) // image url
                        //  .placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //  .error(R.drawable.profileimg_home)  // any image in case of error
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                p0: GlideException?,
                                p1: Any?,
                                p2: com.bumptech.glide.request.target.Target<Drawable>?,
                                p3: Boolean
                            ): Boolean {
                                Log.e("yyyyyyyyyyy", "nnnnnnnnnnn")
                                return false
                            }

                            override fun onResourceReady(
                                p0: Drawable?,
                                p1: Any?,
                                p2: com.bumptech.glide.request.target.Target<Drawable>?,
                                p3: DataSource?,
                                p4: Boolean
                            ): Boolean {
                                Log.e("xxxxxx", "OnResourceReady")
                                //do something when picture already loaded
                                /*   holder.itemView.imageData.visibility = View.VISIBLE
                                   holder.itemView.shimmer_view_container2.stopShimmer()
                                   holder.itemView.shimmer_view_container2.visibility=View.GONE*/
                                holder.itemView.imageData.visibility = View.VISIBLE
                                holder.itemView.shimmer_view_container2.stopShimmer()
                                holder.itemView.shimmer_view_container2.visibility = View.GONE
                                return false
                            }
                        })
                        .into(holder.itemView.imageData1);
                    Glide.with(context)
                        .load(courtDataList.get(position).club_image) // image url
                        //  .placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                        //   .error(R.drawable.profileimg_home)  // any image in case of error
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                p0: GlideException?,
                                p1: Any?,
                                p2: com.bumptech.glide.request.target.Target<Drawable>?,
                                p3: Boolean
                            ): Boolean {
                                Log.e("yyyyyyyyyyy", "nnnnnnnnnnn")
                                return false
                            }

                            override fun onResourceReady(
                                p0: Drawable?,
                                p1: Any?,
                                p2: com.bumptech.glide.request.target.Target<Drawable>?,
                                p3: DataSource?,
                                p4: Boolean
                            ): Boolean {
                                Log.e("xxxxxx", "OnResourceReady")
                                holder.itemView.shimmer_view_container2.stopShimmer()
                                holder.itemView.shimmer_view_container2.visibility = View.GONE
                                //do something when picture already loaded
                                /*   holder.itemView.imageData.visibility = View.VISIBLE
                                   holder.itemView.shimmer_view_container2.stopShimmer()
                                   holder.itemView.shimmer_view_container2.visibility=View.GONE*/
                                /*   holder.itemView.imageData.visibility = View.VISIBLE
                                   holder.itemView.shimmer_view_container2.stopShimmer()
                                   holder.itemView.shimmer_view_container2.visibility=View.GONE*/
                                return false
                            }
                        })
                        .into(holder.itemView.imageData);
                }

                /*    handler!!.postDelayed(Runnable {
                        holder.itemView.imageData.visibility = View.VISIBLE
                        holder.itemView.shimmer_view_container2.stopShimmer()
                        holder.itemView.shimmer_view_container2.visibility=View.GONE
                    }, 1500)*/
                holder.itemView.mainContent.setSafeOnClickListener {


                    //  if(!SessionClass(context).loginData.id.equals(courtData.user_id.toString())) {
                    context.startActivity(
                        Intent(context, CategoryDetailActivity::class.java)
                            .putExtra("club_id", courtDataList.get(position).id)
                            .putExtra("callFrom", "")
                    )
                    //  }
                }


            }
        }else{

            holder.itemView.lastIndex.visibility = View.GONE
            holder.itemView.mainContent.visibility = View.VISIBLE
            if (courtDataList.get(position).club_name != null) {
                holder.itemView.tvClubName.setText(courtDataList.get(position).club_name)
            }

            if (courtDataList.get(position).club_image != null) {
                holder.itemView.shimmer_view_container2.startShimmer()
                Glide.with(context)
                    .load(courtDataList.get(position).club_image) // image url
                    //  .placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                    //  .error(R.drawable.profileimg_home)  // any image in case of error
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            p0: GlideException?,
                            p1: Any?,
                            p2: com.bumptech.glide.request.target.Target<Drawable>?,
                            p3: Boolean
                        ): Boolean {
                            Log.e("yyyyyyyyyyy", "nnnnnnnnnnn")
                            return false
                        }

                        override fun onResourceReady(
                            p0: Drawable?,
                            p1: Any?,
                            p2: com.bumptech.glide.request.target.Target<Drawable>?,
                            p3: DataSource?,
                            p4: Boolean
                        ): Boolean {
                            Log.e("xxxxxx", "OnResourceReady")
                            //do something when picture already loaded
                            /*   holder.itemView.imageData.visibility = View.VISIBLE
                               holder.itemView.shimmer_view_container2.stopShimmer()
                               holder.itemView.shimmer_view_container2.visibility=View.GONE*/
                            holder.itemView.imageData.visibility = View.VISIBLE
                            holder.itemView.shimmer_view_container2.stopShimmer()
                            holder.itemView.shimmer_view_container2.visibility = View.GONE
                            return false
                        }
                    })
                    .into(holder.itemView.imageData1);
                Glide.with(context)
                    .load(courtDataList.get(position).club_image) // image url
                    //  .placeholder(R.drawable.profileimg_home) // any placeholder to load at start
                    //   .error(R.drawable.profileimg_home)  // any image in case of error
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            p0: GlideException?,
                            p1: Any?,
                            p2: com.bumptech.glide.request.target.Target<Drawable>?,
                            p3: Boolean
                        ): Boolean {
                            Log.e("yyyyyyyyyyy", "nnnnnnnnnnn")
                            return false
                        }

                        override fun onResourceReady(
                            p0: Drawable?,
                            p1: Any?,
                            p2: com.bumptech.glide.request.target.Target<Drawable>?,
                            p3: DataSource?,
                            p4: Boolean
                        ): Boolean {
                            Log.e("xxxxxx", "OnResourceReady")
                            holder.itemView.shimmer_view_container2.stopShimmer()
                            holder.itemView.shimmer_view_container2.visibility = View.GONE
                            //do something when picture already loaded
                            /*   holder.itemView.imageData.visibility = View.VISIBLE
                               holder.itemView.shimmer_view_container2.stopShimmer()
                               holder.itemView.shimmer_view_container2.visibility=View.GONE*/
                            /*   holder.itemView.imageData.visibility = View.VISIBLE
                               holder.itemView.shimmer_view_container2.stopShimmer()
                               holder.itemView.shimmer_view_container2.visibility=View.GONE*/
                            return false
                        }
                    })
                    .into(holder.itemView.imageData);
            }

            /*    handler!!.postDelayed(Runnable {
                    holder.itemView.imageData.visibility = View.VISIBLE
                    holder.itemView.shimmer_view_container2.stopShimmer()
                    holder.itemView.shimmer_view_container2.visibility=View.GONE
                }, 1500)*/
            holder.itemView.mainContent.setSafeOnClickListener {


                //  if(!SessionClass(context).loginData.id.equals(courtData.user_id.toString())) {
                context.startActivity(
                    Intent(context, CategoryDetailActivity::class.java)
                        .putExtra("club_id", courtDataList.get(position).id)
                        .putExtra("callFrom", "")
                )
                //  }
            }
        }

    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun getItemCount():Int {
        if (courtDataList.size>5){
           return courtDataList.size + 1
        }else{
            return courtDataList.size
        }
    }
}


