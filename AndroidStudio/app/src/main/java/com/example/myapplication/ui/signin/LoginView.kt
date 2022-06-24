package com.example.myapplication.ui.signin

import com.example.myapplication.data.remote.Result

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure(code: Int, message: String)
}