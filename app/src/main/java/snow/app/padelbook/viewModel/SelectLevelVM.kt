package snow.app.padelbook.viewModel

import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snow.app.padelbook.common.Utils.*
import snow.app.padelbook.network.ApiInterface
import snow.app.padelbook.network.WebServiceClient
import snow.app.padelbook.network.responses.selectlevel.LevlQuestListResponse
import snow.app.padelbook.network.responses.selectlevel.SignUpStepTwoResponse
import snow.app.padelbook.network.responses.selectlevel.signupTwo.SignUpTwoNewResponse

class SelectLevelVM : BaseObservable() {
    var quesLevel : MutableLiveData<LevlQuestListResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null
    var selectedLevel : MutableLiveData<SignUpTwoNewResponse> ?= null

    init {
        quesLevel = MutableLiveData()
        userFailure = MutableLiveData()
        selectedLevel = MutableLiveData()
    }

    fun selectLevelData(context : Context){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).selectLevelList()
            .enqueue(object : Callback<LevlQuestListResponse>{
                override fun onResponse(call: Call<LevlQuestListResponse>,
                    response: Response<LevlQuestListResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    quesLevel?.value = response.body()
                }

                override fun onFailure(call: Call<LevlQuestListResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                }
            })
    }

    fun selectedLevelData(context : Context, quiz_typeId : String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .selectLevel(quiz_typeId).enqueue(object : Callback<SignUpTwoNewResponse>{
                override fun onResponse(call: Call<SignUpTwoNewResponse>,
                    response: Response<SignUpTwoNewResponse>) {
                    dismissProgress()
                    Log.d("OnResposne",response.message())
                    selectedLevel?.value = response.body()
                }

                override fun onFailure(call: Call<SignUpTwoNewResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }
}