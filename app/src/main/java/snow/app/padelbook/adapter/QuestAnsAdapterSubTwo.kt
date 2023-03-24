package snow.app.padelbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_qustans.view.*
import snow.app.padelbook.R
import snow.app.padelbook.listener.OptionClickListener
import snow.app.padelbook.network.responses.questList.QuestOption
import snow.app.padelbook.network.responses.questList.QuestionData
import snow.app.padelbook.utils.SafeClickListener

class QuestAnsAdapterSubTwo(
    var context: Context, val questionList: List<QuestOption>,
    val listener: OptionClickListener,
    var questionModel: QuestionData,
    var positionn: Int,
    var QuestListSize: Int ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

     var selectedPosition : Int ?= null
    var selectedPositionData : Int = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_qustans, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.rbText.text = questionList[position].title

        if(questionList[position].isSelected){
    //    if(selectedPositionData == position ){
            holder.itemView.rbText.isChecked = true
            holder.itemView.rbText.setTextColor(ContextCompat.getColor(context, R.color.theme_blue))
        }
        else{
            holder.itemView.rbText.isChecked = false
            holder.itemView.rbText.setTextColor(ContextCompat.getColor(context, R.color.text_hint_color))
        }


        holder.itemView.rbText.setSafeOnClickListener {
            setSelected(position)
        }

    }
    private fun setSelected(position: Int)  {
        for(i in 0 until questionList.size){                //save the selected option
            if(i == position){
                questionList[i].isSelected = true
                selectedPosition = i
            }
            else{
                questionList[i].isSelected = false
            }
        }

     //   listener.onOptionClick(selectedPosition!!,questionModel,positionn)

        selectedPositionData = position

        if(positionn == QuestListSize-1){                         // check is it last index question or not
            listener.onOptionSelected(QuestListSize-1,questionModel)
        }

        notifyDataSetChanged()

    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun getItemCount() = questionList.size

}