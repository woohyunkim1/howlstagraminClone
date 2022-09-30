package com.example.howlstagramin_f16

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.howlstagramin_f16.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class LoginActivity : AppCompatActivity() {

    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityLoginBinding? = null

    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    //lateinit : 초깃값 설정 않고, 선언 => 이후 사용할 떄 값 지정
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 기존 setContentView를 제거
//      setContentView(R.layout.activity_login)

        //자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해
        //액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityLoginBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

//        auth = Firebase.auth //FirebaseAuth 인스턴스 초기화
        //auth 객체 초기화
        auth = FirebaseAuth.getInstance()

        //이제부터 binding  바인딩 변수를 활용하여 xml 파일 내 뷰 id 접근이 가능
        //뷰 id도 파스칼케이스 + 카멜케이스의 네이밍 규칙 적용으로 자동변환
        binding.signinTextview.setText(("안녕하세요 \\=0ㅁ0=/"))
        binding.emailLoginButton.setOnClickListener {
            signUp()
        }

    }

    override fun onStart() {
        super.onStart()
        val currenUser = auth.currentUser
        if(currenUser != null){

        }
    }

    override fun onDestroy() {
        //onDestroy 에서 binding class 인스턴스 참조를 정리
        mBinding = null
        super.onDestroy()
    }

    private fun signUp(){
        var email = binding.emailEdittext.text?.toString()?: "woohyun_kim@daekyo.co.kr"
        var password = binding.passwordEdittext.text?.toString()?: "tl7820td!"

        if(email =="" || password==  ""){
            email ="woohyun_kim@daekyo.co.kr"
            password = "tl7820td!"
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->//통신 완료가 된 후 무슨 일을 할지
                if (task.isSuccessful) {
                    //로그인 성공
                    Toast.makeText(
                        this,
                        getString(R.string.signin_complete), Toast.LENGTH_SHORT
                    ).show()

                    moveMainPage(task.result?.user)
                }else if(task.exception?.toString().isNullOrEmpty()){
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()

                } else if (task.exception!!.toString()
                        .contains("FirebaseAuthWeakPasswordException")
                ) {
                    //회원가입 시 비밀번호가 6자리 이상으로 입력하지 않은 경우
                    Toast.makeText(
                        this, "비밀번호는 6자리 이상이어야 합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (task.exception!!.toString()
                        .contains("FirebaseAuthUserCollisionException")
                ) {
                    //이미 존재하는 사용자 -> 로그인 함수 호출.
                    emailSignIn()
                } else {
                    println(task.exception.toString())
                    Toast.makeText(
                        this, task.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun emailSignIn(){

        var email = binding.emailEdittext.text?.toString()?: "woohyun_kim@daekyo.co.kr"
        var password = binding.passwordEdittext.text?.toString()?: "tl7820td!"


        if(email =="" || password==  ""){
            email ="woohyun_kim@daekyo.co.kr"
            password = "tl7820td!"
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //로그인 성공
                    Toast.makeText(
                        this,
                        getString(R.string.signin_complete),
                        Toast.LENGTH_SHORT
                    ).show()
                    moveMainPage(task.result?.user)
                } else {
                    //로그인 실패
                    Toast.makeText(
                        this,
                        getString(R.string.signout_fail_null),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun moveMainPage(user: FirebaseUser?){
        if(user!= null){
            //Activity 간 이동
            //startActivity() - 결과를 받지 않음
            //startActivityForResult() - 결과를 받음
            startActivity(Intent(this,MainActivity::class.java))
        }
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

//4. nullCheck
// ? 는 변수에 null 값을 넣을 수 있다(널 허용)는 것을 뜻함.
// !! 는 해당 변수가 현재 널 값이 아니라고 컴파일러에게 알려줘서 컴파일 에러가 나지 않도록 할 때 사용.
// 둘 다 null 선언이 된 상태에서 값을 수정하거나 출력하려 하면 null check 에러가 뜨는 경우 해결

//5. 형변환
// to.타입()을 붙여 타입 변경 가능
// nullCheck를 하는 경우 변수!!.toInt()로 가능

//6. 프로퍼티 초기화
//6-1. lateinit
//- var 프로퍼티에서 사용가능
//- null을 허용하지 않음
//- get() set() 사용이 불가능함
//- 생성자에서 사용 불가능
//isInitialized를 사용해서 프로퍼티가 초기화 되었는지 확인 가능
//6-2.  be lazy
//- val 에서 사용 가능
//- get() set() 을 지원하지 않음
//- 널 허용
//- 클래스 생성자에서 사용 불가
//6-3. be lazy를 사용하는 경우
//- 반드시 초기화를 안해도 되는 경우(null 허용)
//- 최초 초기화 후 다시 초기화 할 일이 없을 떄 (val)