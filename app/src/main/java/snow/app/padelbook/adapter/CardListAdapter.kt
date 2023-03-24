package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items_card_list.view.*
import snow.app.padelbook.R
import snow.app.padelbook.listener.FavouriteListener
import snow.app.padelbook.network.responses.cardlistnew.CardsListResponseNew

class CardListAdapter(var context: Context, val data: MutableList<CardsListResponseNew.Datum>,val favouriteListener: FavouriteListener ) :  RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_card_list, parent, false)
        return HomeAdapterSecond.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        holder.itemView.tvName.setOnClickListener {
            favouriteListener.onIconClick(position,data.get(position).id)

        }
        holder.itemView.tvName.setText("XXXX XXXX XXXX "+data.get(position).last4)
        Glide.with(context)
            .load(data.get(position).cardImage)               // image url
            //.placeholder(R.drawable.profileimg_home)  // any placeholder to load at start
            //.error(R.drawable.profileimg_home)        // any image in case of error
            .into(holder.itemView.imgDp)
    }

    override fun getItemCount() = data.size
}