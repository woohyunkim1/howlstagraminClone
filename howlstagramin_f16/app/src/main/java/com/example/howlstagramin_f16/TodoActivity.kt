package com.example.howlstagramin_f16

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.howlstagramin_f16.databinding.ActivityTodoBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import dto.DataModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import service.RetrofitAPI

class TodoActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityTodoBinding

    val TAG = "TAG_TodoActivity"//로그를 분류할 태그

    lateinit var mRetrofit: Retrofit //사용할 레트로핏 객체
    lateinit var mRetrofitAPI: RetrofitAPI//사용할 레트로핏 api 객체
    lateinit var mCallTodoList: retrofit2.Call<JsonObject>//Json 형식의 데이터를 요청하는 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setRetrofit()//레트로핏 세팅

        //버튼 클릭하면 가져오기
        viewBinding.button1.setOnClickListener {
            callTodoList()
        }
        viewBinding.button2.setOnClickListener {
            postTodoList()
        }
    }

    private fun setRetrofit() {
        //레트로핏으로 가져올 url 설정하고 세팅
        mRetrofit = Retrofit
            .Builder()
            .baseUrl("http://192.168.0.81:5555") // 외부 기기의 경우 127.0.0.1로 설정할 경우 해당 ip로 요청 => 자신의 컴퓨터 ip로 설정
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스로 만든 레트로핏 api 요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(RetrofitAPI::class.java)

    }


    //http 요청을 보내면 그에 상응하는 응담을 받을 수 있는 Callback을 비동기방식으로 만들어줌
    private val mRetrofitCallbackGet  = (object : retrofit2.Callback<JsonObject>{//Json객체를 응답받는 콜백 객체

        //응답을 가져오는데 실패
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            t.printStackTrace()
            Log.d(TAG, "에러입니다. => ${t.message.toString()}")
            viewBinding.textView.text = "에러\n" + t.message.toString()
        }
        //응답을 가져오는데 성공 -> 성공한 반응 처리
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            if(response.isSuccessful){
                val result = response.body()
                Log.d(TAG, "결과는 => $result")

                viewBinding.textView.text = "해야할 일\n" + result

            }

        }
    })

    private val mRetrofitCallbakPost = (object :retrofit2.Callback<JsonObject>{
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            Log.d(TAG,"결과는 =>${response.message().toString()}")
            val result = response.body().toString()
            viewBinding.textView.text = result
        }
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            Log.d(TAG, "에러입니다. =>${t.message.toString()}")
        }
    })


    private fun callTodoList(){
        mCallTodoList = mRetrofitAPI.getTodoList()//RetrofitAPI에서 JSON 객체 요청을 반환하는 메서드를 불러줌
        mCallTodoList.enqueue(mRetrofitCallbackGet)//응답을 큐 대기열에 넣음
    }

    private fun postTodoList(){
        mCallTodoList = mRetrofitAPI.postTodoList()
        mCallTodoList.enqueue(mRetrofitCallbakPost)
    }
}
