package com.example.howlstagramin_f16.service

import com.google.gson.JsonObject
import com.example.howlstagramin_f16.dto.DataModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UploadApi {
    @GET("/image")
        fun getImageList(
           @Query("imageBase64") imageBase64 : String,
        ) : Call<DataModel.ResponseCode>

    @Multipart
    @POST("/upload")
        fun uploadImage()
}