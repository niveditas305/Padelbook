package snow.app.padelbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items_contacts.view.*
import snow.app.padelbook.R
import snow.app.padelbook.fragments.ContactActivity
import snow.app.padelbook.network.responses.contactListResponse.ContactInfoData

class ContactAdapter(
    var mcontext: Context,
    var contactList: ArrayList<ContactInfoData>,
    val listener: ContactInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ContactInterface {
        fun onContactItemClick(position: Int, contactInfoData: ContactInfoData)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_contacts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // var data = arrayList[position]

        var contactListData = contactList[position]
        holder.itemView.tvName.setText(contactListData.username)
        holder.itemView.tvMobile.setText(contactListData.phone_num)
        Glide.with(mcontext)
            .load(contactListData.image_file)             // image url
            .placeholder(R.drawable.ic_user) // any placeholder to load at start
            .error(R.drawable.ic_user)
            //.error(R.drawable.profileimg_home)  // any image in case of error
            .into(holder.itemView.circularImg)

        Log.e("ifrombooking", "--" + ContactActivity.isFromBooking)

        if (contactListData.status == 1) {
            if (ContactActivity.isFromBooking) {
                if (contactListData.userInvite) {
                    holder.itemView.etFirst.setText(mcontext.getString(R.string.invitation_sent))
                    holder.itemView.etFirst.setBackgroundResource(R.drawable.rectangular_box)
                    holder.itemView.etFirst.setTextColor(
                        ContextCompat.getColor(
                            mcontext,
                            R.color.text_hint_color
                        )
                    )
                } else {
                    holder.itemView.etFirst.setText(mcontext.getString(R.string.invite))
                    holder.itemView.etFirst.setBackgroundResource(R.drawable.blue_rect)
                    holder.itemView.etFirst.setTextColor(
                        ContextCompat.getColor(
                            mcontext,
                            R.color.white
                        )
                    )
                }
            } else {
                holder.itemView.etFirst.setText(mcontext.getString(R.string.member))
                holder.itemView.etFirst.setBackgroundResource(R.drawable.rectangular_box)
                holder.itemView.etFirst.setTextColor(
                    ContextCompat.getColor(
                        mcontext,
                        R.color.text_hint_color
                    )
                )

            }
        } else if (contactListData.status == 2) {
            holder.itemView.etFirst.setText(mcontext.getString(R.string.invitation_sent))
            holder.itemView.etFirst.setBackgroundResource(R.drawable.rectangular_box)
            holder.itemView.etFirst.setTextColor(
                ContextCompat.getColor(
                    mcontext,
                    R.color.text_hint_color
                )
            )
        } else {
            if (contactListData.userInvite) {
                holder.itemView.etFirst.setText(mcontext.getString(R.string.invitation_sent))
                holder.itemView.etFirst.setBackgroundResource(R.drawable.rectangular_box)
                holder.itemView.etFirst.setTextColor(
                    ContextCompat.getColor(
                        mcontext,
                        R.color.text_hint_color
                    )
                )
            } else {
                holder.itemView.etFirst.setText(mcontext.getString(R.string.suggest_app))
                holder.itemView.etFirst.setBackgroundResource(R.drawable.blue_rect)
                holder.itemView.etFirst.setTextColor(
                    ContextCompat.getColor(
                        mcontext,
                        R.color.white
                    )
                )
            }
        }


//        else
//        {
//            holder.itemView.etFirst.setText(mcontext.getString(R.string.invitation_sent))
//            holder.itemView.etFirst.setBackgroundResource(0)
//            holder.itemView.etFirst.setTextColor(ContextCompat.getColor(mcontext,R.color.dark_color_two))
//        }

        holder.itemView.etFirst.setOnClickListener {
            listener.onContactItemClick(position, contactList[position])
        }

        holder.itemView.mainViewClick.setOnClickListener {
            listener.onContactItemClick(position, contactList[position])
        }


        // Log.d("Bitmap image",contactListData.photo.toString())

//        holder.itemView.etFirst.setText(data)
//        when(data){
//            mcontext.getString(R.string.member) ->{
//                holder.itemView.etFirst.setBackgroundResource(R.drawable.rectangular_box)
//                holder.itemView.etFirst.setTextColor(ContextCompat.getColor(mcontext,R.color.text_hint_color))
//            }
//            mcontext.getString(R.string.invite) ->{
//                holder.itemView.etFirst.setBackgroundResource(R.drawable.blue_rect)
//                holder.itemView.etFirst.setTextColor(ContextCompat.getColor(mcontext,R.color.white))
//            }
//            mcontext.getString(R.string.invitation_sent) ->{
//                holder.itemView.etFirst.setBackgroundResource(0)
//                holder.itemView.etFirst.setTextColor(ContextCompat.getColor(mcontext,R.color.dark_color_two))
//            }
//        }

    }

    fun filterList(list: ArrayList<ContactInfoData>) {
        contactList = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = contactList.size
}

