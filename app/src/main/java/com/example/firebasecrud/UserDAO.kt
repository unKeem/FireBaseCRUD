package com.example.firebasecrud

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class UserDAO {
    var databaseReference: DatabaseReference? = null

    init {
        /*get instance realtime database of firebase*/
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("user")
    }

    //insert into userTBL @realtime database
    fun insertUser(user: User?): Task<Void> {
        //insert into user(userKey, userName, userAge, userPhone) values('keyValue', 'nameValue'......)
        return databaseReference!!.push().setValue(user)
    }

    /*realtime database user table select*/
    fun selectUser(): Query? {
        return databaseReference
    }

    /*realtime database user table update*/
    fun updateUser(key: String, hashMap: HashMap<String, Any>): Task<Void>{
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    /*realtime database user table delete*/
    fun deleteUser(key:String):Task<Void>{
        return databaseReference!!.child(key).removeValue()
    }

}