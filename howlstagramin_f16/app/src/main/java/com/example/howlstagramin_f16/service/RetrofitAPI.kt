package com.example.howlstagramin_f16.service

import com.google.gson.JsonObject
import com.example.howlstagramin_f16.dto.DataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitAPI{
    @GET("/todos") //서버에 GET 요청을 할 주소 입력
    fun getTodoList() : Call<JsonObject> // TodoActivity에서 사용할 json 파일 가져오는 메서드

    @POST("/todos")
    fun postTodoList() : Call<JsonObject>
}
