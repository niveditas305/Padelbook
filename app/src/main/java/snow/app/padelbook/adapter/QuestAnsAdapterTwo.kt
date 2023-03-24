package snow.app.padelbook.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_ques_type_two.view.*
import snow.app.padelbook.R
import snow.app.padelbook.listener.OptionClickListener
import snow.app.padelbook.network.responses.questList.QuestionData

class QuestAnsAdapterTwo(var context: Context, val optionList: ArrayList<QuestionData>,
                         val listener: OptionClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), OptionClickListener {

    private var adapter : QuestAnsAdapterSubTwo ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_ques_type_two, parent, false)
        return QuestionAnsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tvQuestion.text = optionList[position].question

        adapter = QuestAnsAdapterSubTwo(context,optionList[position].option,
            this,optionList[position],position,optionList.size)

        holder.itemView.recycleOptions.adapter = adapter
    }

    override fun getItemCount() = optionList.size

    override fun onOptionClick(position: Int, questionModel: QuestionData, questionPosition: Int) {
       //  listener.onOptionClick(position, item, questionPosition)
    }

    override fun onOptionSelected(size: Int, QuestionData: QuestionData) {
        listener.onOptionSelected(optionList.size-1, QuestionData)
        Log.e("QuestionData","--"+QuestionData)
       // adapter?.notifyDataSetChanged()
    }
}