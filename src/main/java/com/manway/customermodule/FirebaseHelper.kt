package com.manway.customermodule

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference


class FirebaseHelper(databaseReference: DatabaseReference,storageReference: StorageReference,context: Context) {
    lateinit var databaseReference: DatabaseReference
    lateinit var storageReference: StorageReference
    lateinit var context: Context

    init {
        this.databaseReference = databaseReference
        this.storageReference = storageReference
        this.context = context

    }

    public fun addDataString(tableName: String?, key: String?, value: String?):Boolean{
        var k:String?=null
        if (tableName != null) {
            if (key != null) {
                databaseReference.child(tableName).child(key).setValue(value)
            }
        }
        return true
    }

    @SuppressLint("Range")
    fun filterColoumnList(tableName: String, coloumn: String?, values: ArrayList<String?>): ArrayList<String> {
        val filterList = ArrayList<String>()
//        val reference = FirebaseDatabase.getInstance().reference
//        val query: Query = reference
//            .child()
//            .orderByChild("username")
//            .equalTo(username)
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.childrenCount > 0) {
//                    //username found
//                } else {
//                    // username not found
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
        return filterList
    }


    public fun insertData(tableName: String?,key: String?,value: String?){
        if (key != null) {
            if (tableName != null) {
                databaseReference.child(tableName).child(key).setValue(value)
            }
        }
    }


    @Composable
    public fun getString(tableName: String?,key: String?):String{
        var k:String="null"
        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                k=dataSnapshot.value.toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        if (tableName != null) {
            if (key != null) {
                databaseReference.child(tableName).child(key).addValueEventListener(postListener)
            }
        }
        return k
    }

    @Composable
    public fun getValue(key: String):String{
        var k by remember {
            mutableStateOf("")
        }
        val postListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                k=dataSnapshot.value.toString()
                Toast.makeText(context,k,Toast.LENGTH_LONG).show()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        databaseReference.child(key).addValueEventListener(postListener)
        return k
    }

    }


    //set Primary value

