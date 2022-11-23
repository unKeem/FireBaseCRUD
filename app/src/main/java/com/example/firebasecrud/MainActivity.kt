package com.example.firebasecrud

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasecrud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*insert user @realtime base*/
        binding.btnInsert.setOnClickListener {
            /*for saving resource local variable*/
            val userdao = UserDAO()
            val name = binding.edtName.text.toString()
            val age = binding.edtAge.text.toString()
            val phone = binding.edtPhone.text.toString()
            val user = User("", name, age, phone)

            userdao.insertUser(user)?.addOnSuccessListener {
                Toast.makeText(this, "insert user success", Toast.LENGTH_SHORT).show()
                /*after insert, clear edtiTextView*/
                binding.edtName.text.clear()
                binding.edtAge.text.clear()
                binding.edtPhone.text.clear()
            }?.addOnFailureListener {
                Toast.makeText(this, "insert user fail", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnList.setOnClickListener{
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
        binding.btnDiaryInsert.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        binding.btnDiaryList.setOnClickListener{
            val intent = Intent(this, TravelListActivity::class.java)
            startActivity(intent)
        }
    }
}