package snow.app.padelbook.listener

import snow.app.padelbook.network.responses.questList.QuestionData

interface OptionClickListener {

    fun onOptionClick(position: Int, questionModel: QuestionData, questionPosition: Int)
    fun onOptionSelected(size: Int, QuestionData: QuestionData)
}