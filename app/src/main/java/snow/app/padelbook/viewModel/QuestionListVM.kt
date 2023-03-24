package snow.app.padelbook.viewModel

import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snow.app.padelbook.common.Utils.dismissProgress
import snow.app.padelbook.common.Utils.showProgress
import snow.app.padelbook.network.ApiInterface
import snow.app.padelbook.network.WebServiceClient
import snow.app.padelbook.network.responses.questList.QuesionListResponse

class QuestionListVM : BaseObservable() {
    var quest_list : MutableLiveData<QuesionListResponse> ?= null
    var userFailure : MutableLiveData<String> ?= null

    init {
        quest_list = MutableLiveData()
        userFailure = MutableLiveData()
    }

    fun questListData(context : Context, quiz_type_id: String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java)
            .QuestList(quiz_type_id).enqueue(object : Callback<QuesionListResponse>{
                override fun onResponse(call: Call<QuesionListResponse>,
                    response: Response<QuesionListResponse>) {
                    dismissProgress()
                    Log.d("OnResponse",response.message())
                    quest_list?.value = response.body()
                }

                override fun onFailure(call: Call<QuesionListResponse>, t: Throwable) {
                    dismissProgress()
                    Log.d("OnFailure",t.localizedMessage)
                    userFailure?.value = "Something went wrong"
                }

            })
    }


}