package com.example.firebasecrud.view.listActivity

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasecrud.DAO.UserDAO
import com.example.firebasecrud.R
import com.example.firebasecrud.adapter.TravelAdapter
import com.example.firebasecrud.data.ItemData
import com.example.firebasecrud.databinding.ActivityTravelListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class TravelListActivity : AppCompatActivity() {
    lateinit var binding: ActivityTravelListBinding
    lateinit var itemDatalist: MutableList<ItemData>
    lateinit var adapter: TravelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTravelListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemDatalist = mutableListOf()
        adapter = TravelAdapter(this, itemDatalist)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        Log.d("firebasecrud", "Success oncreate @TravelListActivity")
        selectItemData()

        /*add recyvlerView swipe function*/
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val userDAO = UserDAO()
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val key = itemDatalist.get(position).docID
                        userDAO.deleteTravelDiary(key!!).addOnSuccessListener {
                            Toast.makeText(applicationContext,
                                "delete user success",
                                Toast.LENGTH_SHORT).show()
                            Log.d("firebasecrud", "success deleteTravel() @TravelListActivity")
                        }.addOnFailureListener {
                            Toast.makeText(applicationContext,
                                "delete user fail",
                                Toast.LENGTH_SHORT).show()
                            Log.d("firebasecrud", "fail deleteTravel() @TravelListActivity")
                        }
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean,
            ) {
                RecyclerViewSwipeDecorator.Builder(c,recyclerView, viewHolder, dX,dY, actionState,isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(Color.RED)
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_24)
                    .addSwipeLeftLabel("delete")
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .create()
                    .decorate()
                super.onChildDraw(c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive)
            }
        }).attachToRecyclerView(binding.recyclerView)
    }

    private fun selectItemData() {
        val userDAO = UserDAO()
        userDAO.selectItemData()?.addValueEventListener(object : ValueEventListener {
            /*run on success - Receive data one by one in json format and set class type*/
            override fun onDataChange(snapshot: DataSnapshot) {
                itemDatalist.clear()
                for (userData in snapshot.children) {
                    val itemData = userData.getValue(ItemData::class.java)
                    if (itemData != null) {
                        itemDatalist.add(itemData)
                    }
                }//for
                adapter.notifyDataSetChanged()
            }//onDataChange
            /*run on failure*/
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,
                    "failed loading storage data",
                    Toast.LENGTH_SHORT).show()
                Log.d("firebasecrud", "failed selectItemData() @TravelListActivity")
            }
        })
    }
}
