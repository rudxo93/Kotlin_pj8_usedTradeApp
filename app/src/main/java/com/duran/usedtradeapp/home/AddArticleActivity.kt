package com.duran.usedtradeapp.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.duran.usedtradeapp.DBkey.Companion.DB_ARTICLES
import com.duran.usedtradeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class AddArticleActivity : AppCompatActivity() {

    private var selectedUri: Uri? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_ARTICLES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        // 이미지 추가하기
        findViewById<Button>(R.id.imageAddBtn).setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> { // 권한을 가지고있는 경우
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 교육이 필요한 경우
                    showPermissionContextPop()
                }
                else -> {
                    // 권한요청
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1010
                    )
                }
            }
        }

        // 게시글 등록하기 버튼
        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            // 입력된 값
            val title = findViewById<EditText>(R.id.titleEt).toString()
            val price = findViewById<EditText>(R.id.priceEt).toString()
            val sellerId = auth.currentUser?.uid.orEmpty()
            // 모델 생성하기
            val model = ArticleModel(sellerId, title, System.currentTimeMillis(), "${price}원", "")
            // 데이터베이스 업로드
            articleDB.push().setValue(model)
            finish()
        }
    }

    // 교육용 팝업
    private fun showPermissionContextPop() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해서 권한이 필요하빈다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010)
            }
            .create().show()
    }

    // 권한 요청 결과 확인
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            1010 -> {
                // 권한을 허용한 경우
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider()
                } else { // 권한을 거부한 경우
                    Toast.makeText(this, "권한을 거부했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startContentProvider() {
        // 이미지 SAF기능 실행 -> 이미지 가져오기
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2020)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){

        }

        when(requestCode){
            2020 -> {
                val uri = data?.data
                if(uri != null) {
                    // 사진은 정상적으로 가져온경우
                    findViewById<ImageView>(R.id.photoIv).setImageURI(uri)
                    selectedUri = uri
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}