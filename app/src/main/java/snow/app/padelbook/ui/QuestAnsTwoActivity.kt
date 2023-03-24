package snow.app.padelbook.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_quest_ans_two.*
import kotlinx.android.synthetic.main.popup_quiz.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.QuestAnsAdapterTwo
import snow.app.padelbook.listener.OptionClickListener
import snow.app.padelbook.network.responses.questList.QuestOption
import snow.app.padelbook.network.responses.questList.QuestionData
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.viewModel.QuestionListVM
import snow.app.padelbook.viewModel.SelectAnswerVM

class QuestAnsTwoActivity : BaseActivity(), OptionClickListener {
    private var selectAnswer_ViewModel: SelectAnswerVM? = null

    var questListVM: QuestionListVM? = null
    private var adapter: QuestAnsAdapterTwo? = null
    var allQuestList: ArrayList<QuestionData> = ArrayList()            //  all questions list
    var questList: ArrayList<QuestionData> = ArrayList()               //  new question list
    var setOfThree_QuestList: ArrayList<QuestionData> =
        ArrayList()    //  set of three questions list
    var optionList: ArrayList<QuestOption> = ArrayList()
    var quizID: String? = null
    var genderValueFromFirstQue: String? = null
    var startIndex = 0
    var startIndexTemp = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_ans_two)

        selectAnswer_ViewModel = SelectAnswerVM()

        quizID = intent?.extras?.get("QUIZ_ID") as String?

        questListVM = QuestionListVM()


        // <--------------- get Question list api ----------->
        if (isNetworkConnected()) {
            questListVM?.questListData(this, quizID!!)
        } else {
            showInternetToast()
        }
        // </------------------>

        observer()
    }

    private fun observer() {
        // <------------- get Question list api observer -------->
        questListVM?.quest_list?.observe(this, Observer { user ->
            if (user.status) {
                // showToast(this,user.message)
                optionList.clear()
                allQuestList.clear()
                questList.clear()
                setOfThree_QuestList.clear()

                if (user.data != null) {
                    var staticOptionlist: ArrayList<QuestOption> = ArrayList()
                    staticOptionlist.clear()
                    staticOptionlist.add(QuestOption("Man", "", resources.getString(R.string.man), false))
                    staticOptionlist.add(QuestOption("Woman", "",  resources.getString(R.string.woman), false))
                    allQuestList.add(
                        QuestionData(
                           staticOptionlist,
                            getString(R.string.what_is_your_gender),
                            "static_gender",
                            "",
                            ""
                        )
                    )
                    allQuestList.addAll(user.data)
                    if (allQuestList.size > 0) {
                        setOfThree_QuestList.addAll(allQuestList.subList(startIndex, (startIndex + 3)))
                        questList.add(setOfThree_QuestList[0])   // add index[0] value to new list
                        if (user.data[0].option != null) {
                            optionList.addAll(user.data[0].option)
                        }
                    }
                }
                setAdapter()
            } else {
                showToast(this, user.message)
            }
        })

        // select answer api response
        selectAnswer_ViewModel?.userSelectAnswer?.observe(this, Observer { user ->
            if (user.status) {

                if (lastindexreached == 1) {

                    showDialog(user.userScore)
                } else {
                    // showToast(this,user.message)
                }
            } else {
                showToast(this, user.message)
            }
        })
    }

    private fun showDialog(score:String?) {
        val dialogData = Dialog(this)
        dialogData.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogData.setCancelable(false)
        dialogData.setContentView(R.layout.popup_quiz)
        dialogData.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        );
        dialogData.window?.setGravity(Gravity.CENTER)
        dialogData.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        score?.let {
            if (it.isNotEmpty()){
                dialogData.findViewById<TextView>(R.id.heading).text = getString(R.string.popup_title) +" "+it
            }
        }

        dialogData.dismiss()
        dialogData.popupSubmit.setSafeOnClickListener {
            Log.e("TAG", "showDialog: ")
            dialogData.dismiss()
            startActivity(Intent(this, IntroScreenActivity::class.java).putExtra("from_que_ans","1"))
           /* SessionClass(this).isLogin = true
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()*/
        }
        dialogData.show()
    }


    private fun setAdapter() {
        adapter = QuestAnsAdapterTwo(this, questList, this)
        recycleQuestions.adapter = adapter
    }

    override fun onOptionClick(
        position: Int,
        questionModel: QuestionData,
        questionPosition: Int
    ) {  // radiobutton recycle click position
//       Log.d("optionPosition", position.toString())
//       Log.d("questionPosition", questionPosition.toString())
//
//        if(questList.size == 3){
//            tvSubmit.visibility = View.VISIBLE  // last index reached , set button visibility visible
//        }
//        else{
//            tvSubmit.visibility = View.GONE
//            adapter?.notifyDataSetChanged()
//        }

    }

    override fun onOptionSelected(size: Int, QuestionData: QuestionData) {
        val addPosition = size + 1
        startIndexTemp = startIndexTemp + 1
        Log.e("------size-------", "------tempListSize" + size.toString())
        Log.e("------size-------", "------AllDataList" + allQuestList.size.toString())
        Log.e("------size-------", "------questList" + questList.size.toString())
        Log.e("------size-------", "------startindex" + startIndex.toString())
        Log.e("------size-------", "------startIndexTemp" + startIndexTemp.toString())
        Log.e("------size-------", "------addPosition" + addPosition.toString())
        Log.e(
            "------size-------",
            "------setOfThree_QuestList" + setOfThree_QuestList.size.toString()
        )

        Log.e("------indexxxx-----out--", "------addPosition" + addPosition.toString())
        Log.e("------que id------out-", "------" + QuestionData.question_id)

        if (addPosition == setOfThree_QuestList.size) {
            // < --------- when index 3,6,9,.... reached ------------->
            if (isNetworkConnected()) {

                    selectAnswer_ViewModel?.selectAnswerViewData(
                        this, QuestionData.option[0].option_id,
                        QuestionData.question_id, QuestionData.quiz_type_id
                    )


            } else {
                showInternetToast()
            }

            tvSubmit.visibility = View.VISIBLE     // make button visible

            tvSubmit.setOnClickListener {
                Log.d("startIndex", startIndex.toString())
                Log.d("questList", questList.size.toString())
                Log.d("setOfThree_QuestList", setOfThree_QuestList.size.toString())
                if (startIndexTemp == allQuestList.size) {

                    // < ---------- when last index reached --------->
                    if (isNetworkConnected()) {
                        lastindexreached = 1
                        selectAnswer_ViewModel?.selectAnswerViewData(
                            this, QuestionData.option[0].option_id,
                            QuestionData.question_id, QuestionData.quiz_type_id
                        )
                    } else {
                        showInternetToast()
                    }
                    // <------------------------------------------/>
                } else {
                    tvSubmit.visibility = View.GONE
                    startIndex = startIndexTemp
                    setOfThree_QuestList.clear()
                    questList.clear()
                    if (startIndex + 3 > allQuestList.size) {
                        setOfThree_QuestList.addAll(
                            allQuestList.subList(
                                startIndex,
                                allQuestList.size
                            )
                        )
                    } else {
                        setOfThree_QuestList.addAll(
                            allQuestList.subList(
                                startIndex,
                                (startIndex + 3)
                            )
                        )
                    }
                    questList.add(setOfThree_QuestList[0])
                    adapter?.notifyDataSetChanged()
                }
            }
        } else {


            if (startIndexTemp == allQuestList.size) {
                tvSubmit.visibility = View.GONE

            } else {

                tvSubmit.visibility = View.GONE
                if (startIndexTemp == allQuestList.size) {

                    // < ---------- when last index reached --------->
                    if (isNetworkConnected()) {
                        lastindexreached = 1
                        selectAnswer_ViewModel?.selectAnswerViewData(
                            this, QuestionData.option[0].option_id,
                            QuestionData.question_id, QuestionData.quiz_type_id
                        )
                    } else {
                        showInternetToast()
                    }
                    // <------------------------------------------/>
                } else {
                    if (isNetworkConnected()) {
                        if (QuestionData.question_id.equals("static_gender")) {
                            Log.e("------indexxxx-------", "------addPosition" + addPosition.toString())
                            Log.e("------que id-------", "------" + QuestionData.question_id)
                            Log.e("------que id-------", "QuestionData.option[0].option_id------" + QuestionData.option[0].option_id)

                            if(QuestionData.option[0].isSelected == true) {
                                selectAnswer_ViewModel?.selectAnswerViewData(
                                    this, QuestionData.option[0].option_id,
                                    "0", "0"
                                )} else if(QuestionData.option[1].isSelected == true) {
                                selectAnswer_ViewModel?.selectAnswerViewData(
                                    this, QuestionData.option[1].option_id,
                                    "0", "0"
                                )
                            }

                        }else{
                            selectAnswer_ViewModel?.selectAnswerViewData(
                                this, QuestionData.option[0].option_id,
                                QuestionData.question_id, QuestionData.quiz_type_id
                            )
                        }

                    } else {
                        showInternetToast()
                    }
                }


                questList.add(setOfThree_QuestList[addPosition])  // add next question to list
                adapter?.notifyDataSetChanged()
            }
            // setAdapter()
        }
        Log.e("------size-------", "------tempListSize" + questList.size.toString())
        Log.e("------size-------", "------AllDataList" + allQuestList.size.toString())
        //   adapter?.notifyDataSetChanged()

    }

    companion object {
        var lastindexreached = 0
    }
}