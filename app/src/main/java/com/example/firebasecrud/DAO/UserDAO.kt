package com.example.firebasecrud.DAO

import com.example.firebasecrud.data.ItemData
import com.example.firebasecrud.data.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class UserDAO {
    /*firebaseRealtimeDatabase userTbl */
    var databaseReference: DatabaseReference? = null

    /*firebaseRealtimeDatabase travelDiaryTbl */
    var travelDiaryDatabaseReference: DatabaseReference? = null

    /*firebaseStorage*/
    var storage: FirebaseStorage? = null

    init {
        /*get instance realtime database of firebase*/
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("user")
        travelDiaryDatabaseReference = db.getReference("travelDiary")
        storage = Firebase.storage
    }

    /*insert into userTBL @realtime database*/
    fun insertUser(user: User?): Task<Void> {
        //insert into user(userKey, userName, userAge, userPhone) values('keyValue', 'nameValue'......)
        return databaseReference!!.push().setValue(user)
    }

    /*insert into diaryTBL @realtime database*/
    fun insertTravelDiary(itemData: ItemData?): Task<Void> {
        //insert into user(userKey, userName, userAge, userPhone) values('keyValue', 'nameValue'......)
        return travelDiaryDatabaseReference!!.push().setValue(itemData)
    }
    /*select diaryTBL @realtime database*/
    fun selectItemData(): Query? {
        return travelDiaryDatabaseReference
    }

    /*realtime database diaryTBL table delete*/
    fun deleteTravelDiary(key: String): Task<Void> {
        return travelDiaryDatabaseReference!!.child(key).removeValue()
    }

    /*realtime database user table select*/
    fun selectUser(): Query? {
        return databaseReference
    }

    /*realtime database user table update*/
    fun updateUser(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    /*realtime database user table delete*/
    fun deleteUser(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }


}