package com.example.firebasecrud.view.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.firebasecrud.data.ItemData
import com.example.firebasecrud.DAO.UserDAO
import com.example.firebasecrud.databinding.ActivityAddBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    companion object {
        const val EMAIL = "gmail@gmail.com"
    }

    lateinit var binding: ActivityAddBinding
    var imageUri: Uri? = null
    lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Glide.with(applicationContext)
                    .load(it.data?.data)
//                    .apply(RequestOptions().override(300, 200))
//                    .centerCrop()
                    .into(binding.ivImageAdd)
                imageUri = it.data?.data
                var cursor = contentResolver.query(it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null)
                cursor?.moveToFirst().let {
                    filePath = cursor!!.getString(0) /*절대경로*/
                }
            }
        } /*requestLauncher*/

        binding.ivImageAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        binding.btnTravelSave.setOnClickListener {
            if (binding.ivImageAdd.drawable == null || binding.edtContentAdd.text.isEmpty()) {
                Toast.makeText(this, "이미지나 내용을 입력하지 않으면 업로드 할 수 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                /*firebaseRealtimeDatabase travelDiary에 입력할때 가져오는 key*/
                val userDAO = UserDAO()
                val key = userDAO.travelDiaryDatabaseReference?.push()?.key

                /*first. insert image*/
                val email = EMAIL
                val content = binding.edtContentAdd.text.toString().trimIndent()
                val date = SimpleDateFormat("yy-MM-dd").format(Date())

                val itemData = ItemData(key, email, content, date)

                /*firebaseStorage에 이미지를 업로드할 경로명 셋팅*/
                val imageReference = userDAO.storage?.reference?.child("images/${key}.jpg")
                val file = Uri.fromFile(File(filePath))
                /*filepath 로 해서 에러가 발생한다면 imageUri 로 변경해서 시도해볼것*/
                imageReference?.putFile(file)?.addOnSuccessListener {
                    Log.d("firebasecrud", "successful upload")
                    /*insert firebase realtime database diary table.
                    위에 (이미지 이름 중복을 피하기위해) push하면서 키값만 가져온 그자리에 데이터를 셋팅*/
                    userDAO.travelDiaryDatabaseReference?.child(key!!)?.setValue(itemData)
                        ?.addOnSuccessListener {
                            Log.d("firebasecrud", "successful upload to tbl")
                        }?.addOnFailureListener {
                        Log.d("firebasecrud", "failed upload to tbl")
                    }
                }?.addOnFailureListener {
                    Log.d("firebasecrud", "failed upload")
                }
            }
        }
    }
}