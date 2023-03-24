package snow.app.padelbook.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_category_detail.*
import kotlinx.android.synthetic.main.activity_intro_screen.*
import snow.app.padelbook.R
import snow.app.padelbook.adapter.IntroSliderAdapter
import snow.app.padelbook.model.IntroSlide
import snow.app.padelbook.session.SessionClass
import snow.app.padelbook.utils.DepthPageTransformer

class IntroScreenActivity : AppCompatActivity() {
    private var introSliderAdapter: IntroSliderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)
        initViews()
    }

    fun initViews() {
        introSliderAdapter = IntroSliderAdapter(
            listOf(
                IntroSlide(
                    applicationContext.resources.getString(R.string.screen_one_title_text),
                    applicationContext.resources.getString(R.string.screen_one_sub_title),
                    R.drawable.ic_circle_circle, R.drawable.img_one, R.color.intro_one
                ),
                IntroSlide(
                    applicationContext.resources.getString(R.string.screen_two_title),
                    applicationContext.resources.getString(R.string.screen_two_sub_title),
                    R.drawable.ic_triangle_triangle, R.drawable.img_two, R.color.intro_two
                ),
                IntroSlide(
                    applicationContext.resources.getString(R.string.screen_three_title),
                    applicationContext.resources.getString(R.string.screen_three_sub_title),
                    R.drawable.ic_square_square, R.drawable.img_th, R.color.intro_three
                ),
                IntroSlide(
                    applicationContext.resources.getString(R.string.screen_four_title),
                    applicationContext.resources.getString(R.string.screen_four_sub_title),
                    R.drawable.ic_square_square, R.drawable.img_th, R.color.intro_four
                )
            ), this@IntroScreenActivity
        )

        introSliderViewPager.adapter = introSliderAdapter
        indicatorContainer.setViewPager(introSliderViewPager)
        //  introSliderViewPager.setPageTransformer( DepthPageTransformer())
        introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 0) {
                    /*    parentLayoutMain.setBackgroundColor(
                            ContextCompat.getColor(
                                this@IntroScreenActivity,
                                R.color.intro_one
                            )
                        )*/
                    buttonNext.visibility = View.GONE
                } else if (position == 1) {
                    /*   parentLayoutMain.setBackgroundColor(
                           ContextCompat.getColor(
                               this@IntroScreenActivity,
                               R.color.intro_two
                           )
                       )*/
                    buttonNext.visibility = View.GONE

                } else if (position == 2) {

                    /*  parentLayoutMain.setBackgroundColor(
                          ContextCompat.getColor(
                              this@IntroScreenActivity,
                              R.color.intro_three
                          )
                      )*/
                    buttonNext.visibility = View.GONE

                } else if (position == 3) {

                    /*   parentLayoutMain.setBackgroundColor(
                           ContextCompat.getColor(
                               this@IntroScreenActivity,
                               R.color.intro_four
                           )
                       )*/
                    buttonNext.visibility = View.VISIBLE

                } else {
                    buttonNext.visibility = View.GONE
                }
            }
        })



        buttonNext.setOnClickListener {
            if (intent.hasExtra("from_que_ans")) {
                SessionClass(this).isLogin = true
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()


            } else {
                onBackPressed()
            }

        }
    }

    override fun onBackPressed() {
        if (intent.hasExtra("from_que_ans")) {
            SessionClass(this).isLogin = true
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        } else {
            super.onBackPressed()
        }

    }
}