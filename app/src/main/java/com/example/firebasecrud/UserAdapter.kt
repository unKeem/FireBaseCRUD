package com.example.firebasecrud

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(val userList: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    lateinit var context: Context

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        val tvAge = itemView.findViewById<TextView>(R.id.tv_age)
        val tvPhone = itemView.findViewById<TextView>(R.id.tv_phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList.get(position)
        holder.apply {
            tvName.text = user.userName
            tvAge.text = user.userAge
            tvPhone.text = user.userPhone
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("key", user.userKey)
            intent.putExtra("name", user.userName)
            intent.putExtra("age", user.userAge)
            intent.putExtra("phone", user.userPhone)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}