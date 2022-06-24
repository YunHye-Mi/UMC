package com.example.myapplication.ui.signup

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.entities.User
import com.example.myapplication.data.remote.AuthService
import com.example.myapplication.databinding.ActivitySignupBinding

class SignUpActivity: AppCompatActivity(), SignUpView {

    lateinit var  binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            signUp()
        }
    }

    private fun getUser(): User {
        val email: String = binding.idEt.text.toString() + "@" + binding.directInputEt.text.toString()
        val pwd: String = binding.passwordEt.text.toString()
        val name: String = binding.nameEt.text.toString()

        return  User(email, pwd, name)
    }

//    private fun signUp() {
//        if (binding.idEt.text.toString().isEmpty() || binding.directInputEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (binding.nameEt.text.toString().isEmpty()){
//            Toast.makeText(this, "이름을 입력하지 않았습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (binding.passwordEt.text.toString() != binding.passwordCheckEt.text.toString()) {
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())
//
//        val user = userDB.userDao().getUsers()
//        Log.d("SIGNUPACT", user.toString())
//    }

    private fun signUp(){
        if (binding.idEt.text.toString().isEmpty() || binding.directInputEt.text.toString().isEmpty()) {
            //Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.nameEt.text.toString().isEmpty()){
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.passwordEt.text.toString() != binding.passwordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
    }

    override fun onSignUpSuccess() {
        Toast.makeText(this, "회원가입이 되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        when(code){
            2016-> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            2000-> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            else-> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}