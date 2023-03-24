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
import snow.app.padelbook.network.responses.selectAnswer.SelectAnswerResponse


class SelectAnswerVM : BaseObservable(), Callback<SelectAnswerResponse> {
    var userSelectAnswer : MutableLiveData<SelectAnswerResponse>?= null
    var userFailure : MutableLiveData<String>?= null

    init {
        userSelectAnswer = MutableLiveData()
        userFailure  = MutableLiveData()
    }

    fun selectAnswerViewData(context : Context, option_id: String,question_id:String,
                             quiz_type_id:String){
        showProgress(context)
        WebServiceClient(context).client.create(ApiInterface::class.java).selectAnswerData(option_id,
        question_id,quiz_type_id).enqueue(this)
    }

    override fun onResponse(call: Call<SelectAnswerResponse>,
        response: Response<SelectAnswerResponse>) {
        dismissProgress()
        Log.d("OnResponse",response.message())
        userSelectAnswer?.value = response.body()
    }

    override fun onFailure(call: Call<SelectAnswerResponse>, t: Throwable) {
        dismissProgress()
        Log.d("OnFailure",t.localizedMessage)
        userFailure?.value = "Something went wrong"
    }
}
