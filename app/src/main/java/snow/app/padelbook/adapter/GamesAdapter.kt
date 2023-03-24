package snow.app.padelbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_games_layout.view.*
import snow.app.padelbook.R
import snow.app.padelbook.network.responses.courtresponse.CategoryData
import snow.app.padelbook.network.responses.singleMatchDetail.Data
import snow.app.padelbook.ui.GamesActivity
import snow.app.padelbook.utils.SafeClickListener

class GamesAdapter(
    var context: Context, val list: ArrayList<String>,
    val listener: OnClickListener, val categoryData: CategoryData?,
    var courtFeature: String?,
    var type: String,
    var callFromHome_Data: Data?,
    var matchType: String

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectedPosition = -1
    var paid_price = "0"

    interface OnClickListener {
        fun onClick(position: String, price: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_games_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.rbText.setText(list[position])

        if (selectedPosition == position) {
            holder.itemView.rbText.isChecked = true
            holder.itemView.description.visibility = View.VISIBLE
        } else {
            holder.itemView.rbText.isChecked = false
            holder.itemView.description.visibility = View.GONE
        }

        if (categoryData?.is_club_pass == 1 && position == 0) {

            holder.itemView.clubPassTextview.visibility = View.VISIBLE
            holder.itemView.tvPrice.visibility = View.GONE

        } else {
            holder.itemView.clubPassTextview.visibility = View.GONE
            holder.itemView.tvPrice.visibility = View.VISIBLE
        }

        Log.e("courtFeature", courtFeature.toString())
        Log.e("court pp", categoryData?.price.toString())
        Log.e("court type", type.toString())
        Log.e("matchType", matchType.toString())
        if (type.equals("from")) {
            if (matchType.equals("0")) {// call from court fragment
                if (position == 0) {
                    if (courtFeature.equals("Single")) {
                        holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data_public))
                        holder.itemView.tvPrice.setText(
                            String.format("%.2f", (categoryData?.price?.toFloat()?.div(2)))
                                .toString() + "€"
                        )
                        paid_price = String.format("%.2f", (categoryData?.price?.toFloat()?.div(2)))
                            .toString()
                    } else {
                        holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data_public))
                        holder.itemView.tvPrice.setText(
                            String.format("%.2f", (categoryData?.price?.toFloat()?.div(4)))
                                .toString() + "€"
                        )

                        paid_price = String.format("%.2f", (categoryData?.price?.toFloat()?.div(4)))
                            .toString()
                    }

                } else {
                    if (courtFeature.equals("Single")) {
                        holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data2))

                    } else {
                        holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data2))

                    }
                    /*    if (courtFeature == "Single") {
                        holder.itemView.tvPrice.setText(
                            (categoryData?.price?.toInt()?.times(2)).toString() + ".00€"
                        )
                    } else {
                        holder.itemView.tvPrice.setText(
                            (categoryData?.price?.toInt()?.times(4)).toString() + ".00€"
                        )
                    }*/
                    holder.itemView.tvPrice.setText(categoryData?.price + ".00€")
                    paid_price = categoryData?.price!!
                }
            } else {
                if (position == 0) {
                    if (courtFeature.equals("Single")) {
                        holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data))
                        holder.itemView.tvPrice.setText(
                            String.format("%.2f", (categoryData?.price?.toFloat()?.div(2)))
                                .toString() + "€"
                        )

                        paid_price = String.format("%.2f", (categoryData?.price?.toFloat()?.div(2)))
                            .toString()
                    } else {
                        holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data))
                        holder.itemView.tvPrice.setText(
                            String.format("%.2f", (categoryData?.price?.toFloat()?.div(4)))
                                .toString() + "€"
                        )

                        paid_price = String.format("%.2f", (categoryData?.price?.toFloat()?.div(4)))
                            .toString()
                    }

                } else {
                    if (courtFeature.equals("Single")) {

                        holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data2))

                    } else {

                        holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data2))

                    }
                    /*    if (courtFeature == "Single") {
                        holder.itemView.tvPrice.setText(
                            (categoryData?.price?.toInt()?.times(2)).toString() + ".00€"
                        )
                    } else {
                        holder.itemView.tvPrice.setText(
                            (categoryData?.price?.toInt()?.times(4)).toString() + ".00€"
                        )
                    }*/
                    holder.itemView.tvPrice.setText(categoryData?.price + ".00€")

                    paid_price = categoryData?.price!!
                }
            }
        } else {
            if (position == 0) {
                if (courtFeature.equals("Single")) {
                    holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data))
                    holder.itemView.tvPrice.setText(
                        String.format("%.2f", (callFromHome_Data?.price?.toFloat()?.div(2)))
                            .toString() + "€"
                    )

                    paid_price =
                        String.format("%.2f", (callFromHome_Data?.price?.toFloat()?.div(2)))
                            .toString()
                } else {
                    holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data))
                    holder.itemView.tvPrice.setText(
                        String.format("%.2f", (callFromHome_Data?.price?.toFloat()?.div(4)))
                            .toString() + "€"
                    )

                    paid_price =
                        String.format("%.2f", (callFromHome_Data?.price?.toFloat()?.div(4)))
                            .toString()
                }

            } else {
                if (courtFeature.equals("Single")) {
                    holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data2))

                } else {
                    holder.itemView.dataTxt.setText(context.resources.getString(R.string.games_data2))

                }
                /*    if (courtFeature == "Single") {
                        holder.itemView.tvPrice.setText(
                            (categoryData?.price?.toInt()?.times(2)).toString() + ".00€"
                        )
                    } else {
                        holder.itemView.tvPrice.setText(
                            (categoryData?.price?.toInt()?.times(4)).toString() + ".00€"
                        )
                    }*/
                holder.itemView.tvPrice.setText(callFromHome_Data?.price + ".00€")

                paid_price = callFromHome_Data?.price!!
            }
            // holder.itemView.tvPrice.setText(callFromHome_Data?.price + ".00€")
        }
        holder.itemView.rbText.setSafeOnClickListener {
            Log.e("mMatchType", matchType)
            selectedPosition = position
            notifyDataSetChanged()
            listener.onClick(list[position], paid_price)
        }

    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun getItemCount() = list.size

}
