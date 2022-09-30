package com.example.howlstagramin_f16

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.howlstagramin_f16.databinding.ActivityAddphotoBinding

import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dto.ContentDTO
import retrofit2.http.Url
import java.text.SimpleDateFormat
import java.util.*

class AddphotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ABUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null

    var auth: FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    private lateinit var viewBinding: ActivityAddphotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddphotoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //스토리지 초기화
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        //앨범 열기
        val photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) //Intent(Intent.Action_PICk)을 사용하면 사진을 가져올 수 있다.
        photoPickerIntent.type = "image/*"
        startForResult.launch(photoPickerIntent)//이미지 정보를 넘겨주는 역할

        //업로드 이벤트 추가
        viewBinding.addphotoBtnUpload.setOnClickListener {
            contentUpload()
        }

    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode == RESULT_OK){

            photoUri = result.data?.data
            viewBinding.addphotoImage.setImageURI(photoUri)

        }else{
            finish()
        }
    }

    fun contentUpload(){
        //파일 이름 만들기
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())//이름이 중복되지 않도록 날짜값을 이미지 파일명으로 지정
        var imageFileName = "IMAGE_" + timestamp+ "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)//첫 번째는 폴더명, 두번 째는 파일이름을 넣어준다.

        Log.d("URL", storageRef.toString())
        //파일 업로드
        //콜백 메소드
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            //성공했다면 url 다운로드
            storageRef.downloadUrl.addOnSuccessListener { uri ->


                //데이터 저장을 위해 contentDTO 객체 생성
                var contentDTO = ContentDTO()

                //이미지의 downloadUrl
                contentDTO.imageUrl = uri.toString()

                contentDTO.uid = auth?.currentUser?.uid

                contentDTO.userId = auth?.currentUser?.email

                contentDTO.explain = viewBinding.addphotoEditExplain.text.toString()

                contentDTO.timestamp = System.currentTimeMillis()

                firestore?.collection("images")?.document()?.set(contentDTO)

                setResult(Activity.RESULT_OK)

                finish()
            }
            Toast.makeText(this, getString((R.string.upload_success)),Toast.LENGTH_SHORT).show()
        }

    }

}