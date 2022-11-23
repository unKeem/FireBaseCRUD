package com.example.firebasecrud.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasecrud.data.ItemData
import com.example.firebasecrud.DAO.UserDAO
import com.example.firebasecrud.databinding.TravelLayoutBinding

class TravelAdapter(val context: Context, val itemList: MutableList<ItemData>):
    RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    class  TravelViewHolder(val binding: TravelLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val binding = TravelLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val data = itemList.get(position)
        val binding = (holder as TravelViewHolder).binding
        binding.apply {
            tvEmail.text = data.email
            tvDate.text = data.date
            tvContent.text = data.content
        }
        /*사진을 firebaseStorage에서 가져온다(경로지정)*/
        val userDAO = UserDAO()
        /*path of saved image in the firevase storage*/
        val imgRef = userDAO.storage!!.reference.child("images/${data.docID}.jpg")
        imgRef.downloadUrl.addOnCompleteListener {
            if(it.isSuccessful){
                Glide.with(context).load(it.result).into(binding.ivImage)
            }
        }
    }

    override fun getItemCount(): Int {
      return itemList.size
    }
}