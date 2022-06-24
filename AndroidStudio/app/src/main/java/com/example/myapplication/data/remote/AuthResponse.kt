package com.example.myapplication.data.remote

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName(value = "isSuccess")val isSuccess: Boolean,
    @SerializedName(value = "code")val code: Int,
    @SerializedName(value = "message")val message: String,
    @SerializedName(value = "result") val result: Result? //null 처리를 해야 회원가입 api를 사용했을 때 자동으로 null처리가 되어 login api와 같이 쓸 수 있음.
)

data class Result(
    @SerializedName(value = "userIdx")var userIdx: Int,
    @SerializedName(value = "jwt")var jwt: String
)
