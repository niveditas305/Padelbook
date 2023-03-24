package snow.app.padelbook.ui

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_qusetion_ans_screen.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.QuestionAnsAdapter
import snow.app.padelbook.model.OptionsModel
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.lifecycle.Observer
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.viewModel.SelectLevelVM

class QusetionAnsScreen : BaseActivity() {

    private var levelListVM : SelectLevelVM ?= null
    var arraylist: ArrayList<OptionsModel> = ArrayList()
    var adapter: QuestionAnsAdapter? = null
    private var loginPref : SessionClass?=null

     var levelFirstID = ""
     var levelSecondID = ""
     var levelThirdID = ""
     var levelForthID = ""
     var levelFivthID = ""
     var QuizID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qusetion_ans_screen)
        loginPref = SessionClass(this)
        //  setAdapter()
        levelListVM = SelectLevelVM()
        if(!isNetworkConnected()){
            showInternetToast()
        }
        else{
            levelListVM?.selectLevelData(this)
        }
        clicks()
        observer()
        setClick()
        if(SessionClass(this).loginData.language_type == "0"){
            changeLanguage("en")
        }
        else{
            changeLanguage("el")
        }

    }

    private fun observer() {
        levelListVM?.quesLevel?.observe(this, Observer { user ->
            if(user.status){
                //showToast(this,user.message)
                if(user.data != null){
                    rbTextTwo.setText(user.data[0].title)
                    tvDesTwo.setText(user.data[0].description)
                    levelFirstID = user.data[0].quiz_type_id
                    QuizID = user.data[0].quiz_type_id

                    rbText.setText(user.data[1].title)
                    tvDes.setText(user.data[1].description)
                    levelSecondID = user.data[1].quiz_type_id
                    QuizID = user.data[1].quiz_type_id

                    rbTextThree.setText(user.data[2].title)
                    tvDesThree.setText(user.data[2].description)
                    levelThirdID = user.data[2].quiz_type_id
                    QuizID = user.data[2].quiz_type_id

                    rbTextFour.setText(user.data[3].title)
                    tvDesFour.setText(user.data[3].description)
                    levelForthID = user.data[3].quiz_type_id
                    QuizID = user.data[3].quiz_type_id

                    rbTextFive.setText(user.data[4].title)
                    tvDesFive.setText(user.data[4].description)
                    levelFivthID = user.data[4].quiz_type_id
                    QuizID = user.data[4].quiz_type_id
                }
            }
            else{
               showToast(this,user.message)
            }
        })


        levelListVM?.selectedLevel?.observe(this, Observer { user ->
            if(user.status){
               // showToast(this,user.message)
                loginPref?.loginData = user.data
                startActivity(Intent(this, QuestAnsTwoActivity::class.java)
                    .putExtra("QUIZ_ID",QuizID))
            }
            else{
                showToast(this,user.message)
            }
        })
    }

    private fun setClick() {
        tvSubmit.setSafeOnClickListener {
            if(!isNetworkConnected()){
                showInternetToast()
            }
            else{
                if(rbTextTwo.isChecked){
                    levelListVM?.selectedLevelData(this,levelFirstID)
                }
                else if(rbText.isChecked){
                    levelListVM?.selectedLevelData(this,levelSecondID)
                }
                else if(rbTextThree.isChecked){
                    levelListVM?.selectedLevelData(this,levelThirdID)
                }
                else if(rbTextFour.isChecked){
                    levelListVM?.selectedLevelData(this,levelForthID)
                }
                else{
                    levelListVM?.selectedLevelData(this,levelFivthID)
                }
            }
        }
    }

    private fun setAdapter() {
        arraylist.clear()
        arraylist.add(OptionsModel(getString(R.string.beginner), false))
        arraylist.add(OptionsModel(getString(R.string.intermediate), false))
        arraylist.add(OptionsModel(getString(R.string.intermediate_high), false))
        arraylist.add(OptionsModel(getString(R.string.advanced), false))
        arraylist.add(OptionsModel(getString(R.string.master), false))

        adapter = QuestionAnsAdapter(this, arraylist)
        recycleQuestions.adapter = adapter
    }

    fun View.slideDown(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    fun clicks() {
        rbText.setSafeOnClickListener {

            llParent.slideUp(500)

            tvDes.visibility = View.VISIBLE
            tvDesThree.visibility = View.GONE
            tvDesTwo.visibility = View.GONE
            tvDesFive.visibility = View.GONE
            tvDesFour.visibility = View.GONE

            rbTextTwo.isChecked = false
            rbTextThree.isChecked = false
            rbTextFour.isChecked = false
            rbTextFive.isChecked = false

        }

        rbTextTwo.setSafeOnClickListener {
            if (!rbTextTwo.isChecked){
                tvDesTwo.slideDown(500)
        }
            rbText.isChecked = false
            rbTextThree.isChecked = false
            rbTextFour.isChecked = false
            rbTextFive.isChecked = false

            tvDes.visibility = View.GONE
            tvDesThree.visibility = View.GONE
            tvDesTwo.visibility = View.VISIBLE
            tvDesFive.visibility = View.GONE
            tvDesFour.visibility = View.GONE

        }

        rbTextThree.setSafeOnClickListener {

            rbTextTwo.isChecked = false
            rbText.isChecked = false
            rbTextFour.isChecked = false
            rbTextFive.isChecked = false

            tvDes.visibility = View.GONE
            tvDesThree.visibility = View.VISIBLE
            tvDesTwo.visibility = View.GONE
            tvDesFive.visibility = View.GONE
            tvDesFour.visibility = View.GONE

            llParentThree.slideUp(500)
        }
        rbTextFour.setSafeOnClickListener {

            rbTextTwo.isChecked = false
            rbText.isChecked = false
            rbTextThree.isChecked = false
            rbTextFive.isChecked = false

            tvDes.visibility = View.GONE
            tvDesThree.visibility = View.GONE
            tvDesTwo.visibility = View.GONE
            tvDesFive.visibility = View.GONE
            tvDesFour.visibility = View.VISIBLE


            llParentFour.slideUp(500)

        }

        rbTextFive.setSafeOnClickListener {

            rbTextTwo.isChecked = false
            rbText.isChecked = false
            rbTextFour.isChecked = false
            rbTextThree.isChecked = false

            tvDes.visibility = View.GONE
            tvDesThree.visibility = View.GONE
            tvDesTwo.visibility = View.GONE
            tvDesFive.visibility = View.VISIBLE
            tvDesFour.visibility = View.GONE

            llParentFive.slideUp(500)

        }
    }

    fun View.slideUp(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }
}