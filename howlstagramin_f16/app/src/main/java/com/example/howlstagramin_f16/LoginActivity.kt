package com.example.howlstagramin_f16

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable
import com.example.howlstagramin_f16.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityLoginBinding? = null;
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 기존 setContentView를 제거
//         setContentView(R.layout.activity_login)
        mBinding = ActivityLoginBinding.inflate(layoutInflater);

        //getRoot 메서드로 레이아웃 내부의 최ㅐ상위 위치 뷰의
        //인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        //이제부터 binding  바인딩 변수를 활용하여 xml 파일 내 뷰 id 접근이 가능
        //뷰 id도 파스칼케이스 + 카멜케이스의 네이밍 규칙 적용으로 자동변환
        binding.signinTextview.setText(("안녕하세요 \\=0ㅁ0=/"))
    }

    override fun onDestroy() {
        //onDestroy 에서 binding class 인스턴스 참조를 정리
        mBinding = null
        super.onDestroy()
    }

    fun signinAndSignup(){
        auth?.createUserWithEmailAndPassword(binding.emailEdittext.text.toString(),binding.passwordEdittext.text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 성공 메세지 출력
                })
            }
    }

//1. var / val 키워드
//    var = variable = 읽기/ 쓰기가 가능한 일반 변수
//    val = valuable = 읽기만 가능한 final 변수
//
//2. Non-Null / Nullable
//    Nullable - null을 값으로 가질 수 있다.
//    Non-null - null을 값으로 가질 수 없다.
//
//3. println("텍스트 $변수")

