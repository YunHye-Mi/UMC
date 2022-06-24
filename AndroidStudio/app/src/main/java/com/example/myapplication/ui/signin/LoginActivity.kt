package com.example.myapplication.ui.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.entities.User
import com.example.myapplication.data.remote.AuthService
import com.example.myapplication.data.remote.Result
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.main.MainActivity
import com.example.myapplication.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


        binding.signInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (binding.idEt.text.toString().isEmpty() || binding.directInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.passwordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email: String = binding.idEt.text.toString() + "@" + binding.directInputEt.text.toString()
        val pwd: String = binding.passwordEt.text.toString()

//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.userDao().getUser(email, pwd)
//
//        user?.let {
//            Log.d("LOGIN_ACT/GET_USER", "userId: ${user.id}, $user")
//            saveJwt(user.id)
//            startActivity()
//        }

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(User(email, pwd, ""))
    }

    private fun saveJwt(jwt: Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun saveJwt2(jwt: String){
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    private fun startActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(code: Int, result: Result) {
        when(code) {
            1000 -> {
                saveJwt(result.userIdx)
                saveJwt2(result.jwt) // 개발시 보틍은 userindex와 같이 저장. 상황에 따라 달라질 수 있음.
                Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                startActivity()
            }
        }
    }

    override fun onLoginFailure(code: Int, message: String) {
        when(code){
            2015 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            2019 -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}