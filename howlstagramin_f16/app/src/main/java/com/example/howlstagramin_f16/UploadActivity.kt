package com.example.howlstagramin_f16

import android.Manifest
import android.app.DownloadManager
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.howlstagramin_f16.databinding.ActivityUploadBinding
import com.google.gson.JsonObject
import dto.DataModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import service.RetrofitAPI
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class UploadActivity : AppCompatActivity() {

    private  lateinit var viewBinding: ActivityUploadBinding

    val TAG = "TAG_UploadActivity"

    lateinit var  mRetrofit : Retrofit
    lateinit var  mRetrofitAPI : RetrofitAPI
    lateinit var  mPostImage: retrofit2.Call<JsonObject>

    var currentImageURL : Uri? = null
    var base64 : String? = null
    var base64bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setRetrofit()//레트로핏 기본 세팅

        viewBinding.btnImageSend.setOnClickListener {
            uploadingFile()
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

    private fun selectCamera(){
        var permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)


    }

    private fun uploadingFile(){
        openGallery() //갤러리 열기
        doFileUpdload()
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"

        startForResult.launch(intent)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        if(result.resultCode == RESULT_OK){
            currentImageURL = intent?.data

            val ins: InputStream? = currentImageURL?.let{
                applicationContext.contentResolver.openInputStream(it)
            }

            val bitmap: Bitmap = BitmapFactory.decodeStream(ins)
            ins?.close()

            base64 = bitmapToString(bitmap)
            // 여기까지 인코딩 끝

            // 이미지 뷰에 선택한 이미지 출력
            val imageview: ImageView = viewBinding.imgVwSelected
            imageview.setImageURI(currentImageURL)
            try {
                //이미지 선택 후 처리

            }catch (e: Exception){
                e.printStackTrace()
            }
        } else{
            Log.d("ActivityResult", "something wrong")
        }
    }

    private  fun bitmapToString(bitmap: Bitmap):String{
        val resized = Bitmap.createScaledBitmap(bitmap, 256,256,true)
        //Bitmap ->ByteArr 배열
        val byteArrayOutputStream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
        val byteArray : ByteArray = byteArrayOutputStream.toByteArray()

        val outStream = ByteArrayOutputStream()
        val res: Resources = resources

        //Base64 전환
        val base64Str :String = Base64.encodeToString(byteArray, NO_WRAP)
        return base64Str
    }
    //앨범에서 선택한 이미지의 URi를 사용하여 비트맵 이미지를 생성한 후, Base64 gudtlrdmfh dlszhelddmf wlsgodgksek
    //Base64로 인코딩 한 문자열을 Json 형태로 서버로 넘기는 경우, 용량이 크게 늘어나 전송이 되기 때문에 서버측에서는 이를 디코딩하기가 힘들다는 문제 발생한다
    //하지만 현재 개발하고자 하는 프로그램에서는 원본 이미지의 크기가 품질이 크게 중요한 요소가 아니었기 때문에 이미지 크기를 256x256으로 이미지 품질을 60으로 설정하였다.


    private fun stringToBitmap(base64:String?):Bitmap {
        val encodeByte = Base64.decode(base64,Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte,0, encodeByte.size)
    }




}