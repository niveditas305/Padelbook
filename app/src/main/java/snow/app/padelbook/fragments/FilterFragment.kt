package snow.app.padelbook.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.toolbar.view.*
import snow.app.padelbook.R
import snow.app.padelbook.databinding.FragmentFilterBinding
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_filter.*
import snow.app.padelbook.common.CheckConnectivity
import snow.app.padelbook.common.Utils.showToast
import snow.app.padelbook.fragments.SearchFragment.Companion.filter_type
import snow.app.padelbook.network.responses.filterresponse.Data
import snow.app.padelbook.utils.SafeClickListener
import snow.app.padelbook.viewModel.SearchFilterVM

class FilterFragment(var court_id: Int) : BottomSheetDialogFragment() {

    var mContext: Activity? = null
    lateinit var binding: FragmentFilterBinding
    private var searchFilterVM: SearchFilterVM? = null
    var sortingType = "2"
    var duration = ""
    var courtType = ""
    var courtSize = ""
    var genderSelected = ""
    var levelSelected = ""
    var distanceSelected = ""

    var onSubmitClick: ((String) -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation

        searchFilterVM = SearchFilterVM()

        if (court_id == 1) {
            binding.newSection.visibility = View.GONE
        } else if (court_id == 2) {
            binding.newSection.visibility = View.VISIBLE
        } else {
            binding.newSection.visibility = View.GONE
        }


        if (isNetworkConnected()) {
            searchFilterVM?.getFilterData(requireContext())
        } else {
            showToast(requireContext(), "No Internet Connection")
        }

        //set title
        setTitle()

        //clicks
        clicks()
        listeners()

        binding.seekbar4.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, p2: Boolean) {
                Log.d("onProgressChanged----------", seekbar.toString())
                Log.d("onProgressChanged----------", progress.toString())
                Log.d("onProgressChanged----------", p2.toString())
                val `val`: Int =
                    progress * (seekbar!!.width - 2 * seekbar.thumbOffset) / seekbar.max

                binding.tvSeekbarLabel.setX(seekbar.x + `val` + seekbar.thumbOffset / 2)

                if (progress == 0) {
                    distanceSelected = "3"
                    binding.tvSeekbarLabel.setText("3")
                    binding.labelFirst.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.theme_blue
                        )
                    )
                    binding.labelSecond.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelThird.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelForth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelFivth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                } else if (progress == 1) {
                    distanceSelected = "7"
                    binding.tvSeekbarLabel.setText("7")
                    binding.labelFirst.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelSecond.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.theme_blue
                        )
                    )
                    binding.labelThird.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelForth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelFivth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                } else if (progress == 2) {
                    distanceSelected = "15"
                    binding.tvSeekbarLabel.setText("15")
                    binding.labelFirst.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelSecond.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelThird.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.theme_blue
                        )
                    )
                    binding.labelForth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelFivth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                } else if (progress == 3) {
                    distanceSelected = "20"
                    binding.tvSeekbarLabel.setText("20")
                    binding.labelFirst.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelSecond.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelThird.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelForth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.theme_blue
                        )
                    )
                    binding.labelFivth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                } else if (progress == 4) {
                    distanceSelected = "0"
                    binding.tvSeekbarLabel.setText("All")

                    binding.labelFirst.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelSecond.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelThird.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelForth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_color
                        )
                    )
                    binding.labelFivth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.theme_blue
                        )
                    )
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Log.d("onStartTrackingTouch----------", p0.toString())
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Log.d("onStopTrackingTouch----------", p0.toString())
            }

        })

    }

    private fun listeners() {
        searchFilterVM?.searchFilter?.observe(this, Observer { user ->
            if (user.status) {
                // showToast(requireContext(),user.message)

                onSubmitClick?.invoke("")
            } else {
                showToast(requireContext(), user.message)
            }
        })


        searchFilterVM?.getSearchFilterData?.observe(this, Observer { user ->
            if (user.status) {
                // showToast(requireContext(),user.message)
                setDataToItems(user.data)
            } else {
                showToast(requireContext(), user.message)
            }
        })
    }

    private fun setDataToItems(data: Data) {
        if (data.sort_type != null) {
            if (data.sort_type == "1") {
                sortingType = "1"
                setBlueSolidBack(tvSortinOne)
            } else if (data.sort_type == "2") {
                sortingType = "2"
                setBlueSolidBack(tvSortingTwo)
            } else {
                setBlueSolidBack(tvSortinThree)
                sortingType = "3"
            }
        } else {
            sortingType = ""
            setrectngularGreyColor(tvSortinOne)
            setrectngularGreyColor(tvSortingTwo)
            setrectngularGreyColor(tvSortinThree)
        }

        if (data.distance != null) {
            distanceSelected = data.distance.toString()
            if (data.distance == 3) {
                seekbar4.setProgress(0)
            } else if (data.distance == 7) {
                seekbar4.setProgress(1)
            } else if (data.distance == 15) {
                seekbar4.setProgress(2)
            } else if (data.distance == 20) {
                seekbar4.setProgress(3)
            } else {
                seekbar4.setProgress(4)
            }

        } else {
            distanceSelected = ""
        }

        if (data.duration != null) {
            if (data.duration == "60") {
                duration = data.duration
                setBlueSolidBack(tvDurationOne)
            } else if (data.duration == "90") {
                setBlueSolidBack(tvDurationTwo)
                duration = data.duration
            } else {
                setBlueSolidBack(tvDurationthree)
                duration = data.duration
            }
        } else {
            duration = ""
            setrectngularGreyColor(tvDurationOne)
            setrectngularGreyColor(tvDurationTwo)
            setrectngularGreyColor(tvDurationthree)
        }

        if (data.court_type != null) {
            if (data.court_type == "Indoors") {
                // courtType
                courtType = "Indoors"
                setBlueSolidBack(tvCourtTthree)
            } else if (data.court_type == "Roofed") {
                // courtType
                courtType = "Roofed"
                setBlueSolidBack(tvCourtTTwo)
            } else if (data.court_type == "Open") {
                // courtType
                courtType = "Open"
                setBlueSolidBack(tvCourtTone)
            } else if (data.court_type == "Outdoor") {
                setBlueSolidBack(tvCourtTTwo)
                courtType = "Outdoor"
            } else {
                courtType = ""
                setBlueSolidBack(tvCourtTone)
            }
        } else {
            setrectngularGreyColor(tvCourtTone)
            setrectngularGreyColor(tvCourtTTwo)
            setrectngularGreyColor(tvCourtTthree)
        }


        if (data.court_feature != null) {
            if (data.court_feature == "Double") {
                setBlueSolidBack(tvCourtSizeOne)
                courtSize = "Double"
            } else {
                setBlueSolidBack(tvCourtSizeTwo)
                courtSize = "Single"
            }
        } else {
            setrectngularGreyColor(tvCourtSizeOne)
            setrectngularGreyColor(tvCourtSizeTwo)
        }


        if (data.gender != null) {
            if (data.gender == "1") {
                setBlueSolidBack(tvMale)
            } else if (data.gender == "2") {
                setBlueSolidBack(tvFemale)
            } else {
                setBlueSolidBack(tvMixed)

            }
        } else {
            setrectngularGreyColor(tvMale)
            setrectngularGreyColor(tvFemale)
            setrectngularGreyColor(tvMixed)
        }


    }

    fun setBlueSolidBack(textData: TextView) {
        textData.background =
            ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
        textData.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
        val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
        textData.setTypeface(typeface)
    }

    fun setrectngularGreyColor(textData: TextView) {
        textData
            .background =
            ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
        textData.setTextColor(
            ContextCompat.getColor(
                mContext!!,
                R.color.dark_color
            )
        )
        val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
        textData.setTypeface(typefaceTwo)
    }

    fun setTitle() {
        binding.toolbar.tvTitle.setText(mContext?.resources?.getString(R.string.search_filters))
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.separator.visibility = View.VISIBLE
        binding.toolbar.arc_progress.visibility = View.GONE
    }


    fun isNetworkConnected(): Boolean {
        return CheckConnectivity(requireContext()).isNetworkAvailable
    }

    fun clicks() {
        binding.toolbar.ivBack.setSafeOnClickListener {
            dialog?.onBackPressed()
        }


        binding.tvSortinOne.setSafeOnClickListener {

//            if (binding.tvSortinOne.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            sortingType = "1"
            binding.tvSortinOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvSortinOne.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvSortinOne.setTypeface(typeface)

//            } else {
//                binding.tvSortinOne.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvSortinOne.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvSortinOne.setTypeface(typeface)
//            }
            binding.tvSortingTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvSortingTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvSortingTwo.setTypeface(typefaceTwo)


            binding.tvSortinThree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvSortinThree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvSortinThree.setTypeface(typefaceTwo)


        }
        binding.tvSortingTwo.setSafeOnClickListener {

//            if (binding.tvSortingTwo.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            sortingType = "2"
            binding.tvSortingTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvSortingTwo.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvSortingTwo.setTypeface(typeface)
//
//            } else {
//                binding.tvSortingTwo.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvSortingTwo.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvSortingTwo.setTypeface(typeface)
//            }
            binding.tvSortinOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvSortinOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvSortinOne.setTypeface(typefaceTwo)



            binding.tvSortinThree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvSortinThree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvSortinThree.setTypeface(typefaceTwo)

        }

        binding.tvSortinThree.setSafeOnClickListener {

//            if (binding.tvSortinThree.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            sortingType = "3"
            binding.tvSortinThree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvSortinThree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvSortinThree.setTypeface(typeface)

//            } else {
//                binding.tvSortinThree.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvSortinThree.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvSortinThree.setTypeface(typeface)
//            }
            binding.tvSortinOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvSortinOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvSortinOne.setTypeface(typefaceTwo)


            binding.tvSortingTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvSortingTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvSortingTwo.setTypeface(typefaceTwo)

        }


        binding.tvDurationOne.setSafeOnClickListener {

//            if (binding.tvDurationOne.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            duration = "60"
            binding.tvDurationOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvDurationOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvDurationOne.setTypeface(typeface)

//            } else {
//                binding.tvDurationOne.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvDurationOne.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvDurationOne.setTypeface(typeface)
//            }


            binding.tvDurationTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvDurationTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvDurationTwo.setTypeface(typefaceTwo)


            binding.tvDurationthree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvDurationthree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvDurationthree.setTypeface(typefaceTwo)
        }

        binding.tvDurationTwo.setSafeOnClickListener {

//            if (binding.tvDurationTwo.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            duration = "90"
            binding.tvDurationTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvDurationTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvDurationTwo.setTypeface(typeface)

//            } else {
//                binding.tvDurationTwo.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvDurationTwo.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvDurationTwo.setTypeface(typeface)
//            }

            binding.tvDurationOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvDurationOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvDurationOne.setTypeface(typefaceTwo)


            binding.tvDurationthree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvDurationthree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvDurationthree.setTypeface(typefaceTwo)


        }

        binding.tvDurationthree.setSafeOnClickListener {

//            if (binding.tvDurationthree.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            duration = "120"
            binding.tvDurationthree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvDurationthree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvDurationthree.setTypeface(typeface)

//            } else {
//                binding.tvDurationthree.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvDurationthree.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvDurationthree.setTypeface(typeface)
//            }
            binding.tvDurationOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvDurationOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvDurationOne.setTypeface(typefaceTwo)


            binding.tvDurationTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvDurationTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvDurationTwo.setTypeface(typefaceTwo)


        }


        binding.tvCourtSizeOne.setSafeOnClickListener {
            courtSize = "Double"
//            if (binding.tvCourtSizeOne.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            binding.tvCourtSizeOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvCourtSizeOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvCourtSizeOne.setTypeface(typeface)

//            } else {
//                binding.tvCourtSizeOne.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvCourtSizeOne.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvCourtSizeOne.setTypeface(typeface)
//            }

            binding.tvCourtSizeTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtSizeTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvCourtSizeTwo.setTypeface(typefaceTwo)

        }


        binding.tvCourtSizeTwo.setSafeOnClickListener {
            courtSize = "Single"

//            if (binding.tvCourtSizeTwo.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            binding.tvCourtSizeTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvCourtSizeTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvCourtSizeTwo.setTypeface(typeface)

//            } else {
//                binding.tvCourtSizeTwo.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvCourtSizeTwo.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvCourtSizeTwo.setTypeface(typeface)
//            }

            binding.tvCourtSizeOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtSizeOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvCourtSizeOne.setTypeface(typefaceTwo)

        }
        binding.tvLogin.setSafeOnClickListener {
            if (isNetworkConnected()) {
                searchFilterVM?.applyFilterData(
                    requireContext(),
                    "",
                    "",
                    "",
                    sortingType,
                    courtSize,
                    courtType,
                    duration,
                    genderSelected,
                    "",
                    court_id.toString(),
                    distanceSelected,
                    SearchFragment.type
                )

                filter_type = "1"
            } else {
                showToast(requireContext(), "No Internet Connection")
            }
        }


        binding.tvCourtTthree.setSafeOnClickListener {

//            if (binding.tvCourtTthree.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {

            courtType = "Indoors"
            binding.tvCourtTthree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvCourtTthree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvCourtTthree.setTypeface(typeface)

//            } else {
//                binding.tvCourtTthree.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvCourtTthree.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvCourtTthree.setTypeface(typeface)
//            }

            binding.tvCourtTone.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtTone.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvCourtTone.setTypeface(typefaceTwo)



            binding.tvCourtTTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtTTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvCourtTTwo.setTypeface(typefaceTwo)

        }
        binding.tvCourtTone.setSafeOnClickListener {

//            if (binding.tvCourtTone.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            courtType = "Open"
            binding.tvCourtTone.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvCourtTone.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvCourtTone.setTypeface(typeface)

//            } else {
//                binding.tvCourtTone.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvCourtTone.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvCourtTone.setTypeface(typeface)
//            }
            binding.tvCourtTTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtTTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvCourtTTwo.setTypeface(typefaceTwo)


            binding.tvCourtTthree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtTthree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvCourtTthree.setTypeface(typefaceTwo)

        }
        binding.tvCourtTTwo.setSafeOnClickListener {

            courtType = "Roofed"
//            if (binding.tvCourtTTwo.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            binding.tvCourtTTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvCourtTTwo.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvCourtTTwo.setTypeface(typeface)

//            } else {
//                binding.tvCourtTTwo.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvCourtTTwo.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvCourtTTwo.setTypeface(typeface)
//            }

            binding.tvCourtTone.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtTone.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvCourtTone.setTypeface(typefaceTwo)

            binding.tvCourtTthree.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtTthree.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvCourtTthree.setTypeface(typefaceTwo)

        }



        binding.tvCourtSizeOne.setSafeOnClickListener {
            courtSize = "Double"
//            if (binding.tvCourtSizeOne.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            binding.tvCourtSizeOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvCourtSizeOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvCourtSizeOne.setTypeface(typeface)

            //    } else {
            binding.tvCourtSizeTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtSizeTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvCourtSizeTwo.setTypeface(typefaceTwo)
            //   }

        }
        binding.tvCourtSizeTwo.setSafeOnClickListener {
            courtSize = "Single"
//            if (binding.tvCourtSizeTwo.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            binding.tvCourtSizeTwo.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvCourtSizeTwo.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvCourtSizeTwo.setTypeface(typeface)

//            } else {
            binding.tvCourtSizeOne.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvCourtSizeOne.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvCourtSizeOne.setTypeface(typefaceTwo)
//            }

        }


        binding.tvMale.setSafeOnClickListener {

//            if (binding.tvMale.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            genderSelected = "1"
            binding.tvMale.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvMale.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvMale.setTypeface(typeface)

//            } else {
            binding.tvFemale.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvFemale.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvFemale.setTypeface(typefaceTwo)
//            }

            binding.tvMixed.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvMixed.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvMixed.setTypeface(typefaceTwo)


        }


        binding.tvFemale.setSafeOnClickListener {

//            if (binding.tvFemale.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            genderSelected = "2"
            binding.tvFemale.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvFemale.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvFemale.setTypeface(typeface)

//            } else {
            binding.tvMale.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvMale.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvMale.setTypeface(typefaceTwo)
//            }

            binding.tvMixed.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvMixed.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvMixed.setTypeface(typefaceTwo)

        }


        binding.tvMixed.setSafeOnClickListener {

//            if (binding.tvMixed.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            genderSelected = "3"
            binding.tvMixed.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvMixed.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvMixed.setTypeface(typeface)

//            } else {
//                binding.tvMixed.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvMixed.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvMixed.setTypeface(typeface)
//            }
            binding.tvMale.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvMale.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvMale.setTypeface(typefaceTwo)


            binding.tvFemale.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvFemale.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            binding.tvFemale.setTypeface(typefaceTwo)
        }

        binding.tvAllLevel.setSafeOnClickListener {

//            if (binding.tvAllLevel.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            binding.tvAllLevel.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvAllLevel.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvAllLevel.setTypeface(typeface)

//            } else {
//                binding.tvAllLevel.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvAllLevel.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvAllLevel.setTypeface(typeface)
//            }

            binding.tvMyLevel.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvMyLevel.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvMyLevel.setTypeface(typefaceTwo)

        }

        binding.tvMyLevel.setSafeOnClickListener {

//            if (binding.tvMyLevel.background.constantState ==
//                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)?.constantState
//            ) {
            binding.tvMyLevel.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box_full)
            binding.tvMyLevel.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.white
                )
            )
            val typeface = ResourcesCompat.getFont(mContext!!, R.font.bold)
            binding.tvMyLevel.setTypeface(typeface)

//            } else {
//                binding.tvMyLevel.background =
//                    ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
//                binding.tvMyLevel.setTextColor(
//                    ContextCompat.getColor(
//                        mContext!!,
//                        R.color.dark_color
//                    )
//                )
//                val typeface = ResourcesCompat.getFont(mContext!!, R.font.reg)
//                binding.tvMyLevel.setTypeface(typeface)
//            }

            binding.tvAllLevel.background =
                ContextCompat.getDrawable(mContext!!, R.drawable.rectangular_box)
            binding.tvAllLevel.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    R.color.dark_color
                )
            )
            val typefaceTwo = ResourcesCompat.getFont(mContext!!, R.font.reg)
            binding.tvAllLevel.setTypeface(typefaceTwo)

        }


    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.isFitToContents = false
        behavior.isDraggable = false
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        Log.d("onStart", "onStart")
    }


    override fun onAttach(context: Activity) {
        super.onAttach(context)
        mContext = context
    }


    override fun getTheme(): Int {
        return R.style.BottomSheetDialog

    }

}