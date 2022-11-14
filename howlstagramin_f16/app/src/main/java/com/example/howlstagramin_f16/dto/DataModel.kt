package com.example.howlstagramin_f16.dto

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class  DataModel(){

    data class TodoInfo1(val  todo1 : TaskInfo)
    data class TodoInfo2(val  todo2 : TaskInfo)
    data class TodoInfo3(val  todo3 : TaskInfo)

    data class TaskInfo(val task : String? = null)


    data class HTTP_GET_Model(
        var todos: ArrayList<TodoInfo>? = null
    )

    data class TodoInfo(
        @SerializedName("idx")
        var idx: Int? = null,
        @SerializedName("value")
        var value: String? = null
    )

    data class PostModel(
        var idx: Int? = null
    )

    data class PostResult(
        @SerializedName("result")
        var result: String ?= null
    )

    data class PostImage(
        @SerializedName("imageBase64")
        private val imageBase64: String = "",

        @SerializedName("imageName")
        private val imageName : String = ""
    )

    data class ResponseCode(
        val resultCode : Int,
        val resultMsg: String
    )

}
