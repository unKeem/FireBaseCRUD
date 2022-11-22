package com.example.firebasecrud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasecrud.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("key") && intent.hasExtra("name") && intent.hasExtra("age") && intent.hasExtra(
                "phone")
        ) {
            val key = intent.getStringExtra("key")!!
            binding.edtName.setText(intent.getStringExtra("name"))
            binding.edtAge.setText(intent.getStringExtra("age"))
            binding.edtPhone.setText(intent.getStringExtra("phone"))

            binding.btnUpdate.setOnClickListener {
                val userDAO = UserDAO()
                val name = binding.edtName.text.toString()
                val age = binding.edtAge.text.toString()
                val phone = binding.edtPhone.text.toString()
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["userName"] = name
                hashMap["userAge"] = age
                hashMap["userPhone"] = phone
                userDAO.updateUser(key, hashMap).addOnSuccessListener {
                    Toast.makeText(this, "update user success", Toast.LENGTH_SHORT).show()
                    Log.d("firebasecrud", "success updateUser() @UpdateActivity")
                    val intent = Intent(this@UpdateActivity, ListActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "update user fail", Toast.LENGTH_SHORT).show()
                    Log.d("firebasecrud", "failed updateUser() @UpdateActivity")
                }
            }
        } else {
            binding.btnUpdate.isEnabled = true
        }
    }
}