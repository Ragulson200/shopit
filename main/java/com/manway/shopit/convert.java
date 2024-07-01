package com.manway.shopit;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

public class convert {
DatabaseReference db;
StorageReference sr;
public convert(){

    String[] s=new String[5];

 sr.child("").getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
     @Override
     public void onSuccess(byte[] bytes) {

     }
 });

 db.child("").addChildEventListener(new ChildEventListener() {
     @Override
     public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

     }

     @Override
     public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

     }

     @Override
     public void onChildRemoved(@NonNull DataSnapshot snapshot) {

     }

     @Override
     public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

     }

     @Override
     public void onCancelled(@NonNull DatabaseError error) {

     }
 });
}



}
