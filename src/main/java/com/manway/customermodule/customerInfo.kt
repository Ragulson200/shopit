package com.manway.customermodule

import android.graphics.Bitmap
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.util.Arrays
import java.util.regex.Pattern

public class customerInfo public constructor(name:String,age:Int,gender:String,email:String,phoneNumber:Long,address:String,profilePicture:Bitmap,storageReference:StorageReference):HashMap<String, Any>() {

    private var name:String=""
        get() {
          return field
        }
        set(value){
            this[Companion.name]=value
          field=value
        }
    private var age:Int=0
        get() {
            return field
        }
        set(value){
            this[Companion.age]=value
            field=value
        }
    private var gender:String=""
        get() {
            return field
        }
        set(value){
            this[Companion.gender]=value
            field=value
        }
    private var email:String=""
        get() {
            return field
        }
        set(value){
            this[Companion.email]=value
            field=value
        }
    private var storageReference:StorageReference?=null
    public var addresses= ArrayList<HashMap<String,Any>>()
        get() {
            return field
        }
    private var profilePicture=Bitmap.createBitmap(1024,1024,Bitmap.Config.ARGB_8888)
        get() {
            return field
        }

    public fun putImage(value: Bitmap?,email: String){
        this[Companion.profilePicture]="customerInfo/${email}.jpeg"
        if (value != null)
        {
            this.profilePicture=value
        }
        storageReference?.child("customerInfo/${email}.jpeg")?.putBytes(DataBaseConverter.convertImageToByteArray(value))
    }

    public fun addAddress(address:String,location:String,currentAddress:String){
        var map=HashMap<String,Any>()
        map[addres.address]=address
        map[addres.location]=location
        map[addres.currentAddress]=currentAddress
        addresses.add(map)
        this[Companion.address]= addresses

    }
    private var phoneNumbers=ArrayList<HashMap<String,Any>>()
        get() {
            return field
        }
    public fun addPhoneNumber(numbers:Long,currentNumber:String){
        var map=HashMap<String,Any>()
        map[phoneNumbe.phoneNumber]=numbers
        map[phoneNumbe.currentNumber]=currentNumber
        phoneNumbers.add(map)
        this[Companion.phoneNumbers]= phoneNumbers
    }

    public class phoneNumbe{
        companion object {
            val phoneNumber = "phoneNumber"
            val currentNumber = "currentNumber"

            public fun checkPhoneNumber(text:String):Boolean{
                val pattern=Pattern.compile("(91|\\+91)?\\d{10,}+")
                return pattern.matcher(text).find()
            }
        }

    }

    public class addres{
        companion object {
            val address = "address"
            val location = "location"
            val currentAddress = "currentAddress"
        }
        public class Loc{
            companion object{
                val  Home="Home"
                val Factory="factory"
                val Hostel="Hostel"
            }
        }
    }




    init {
        this[Companion.name]=name
        this[Companion.age]=age
        this[Companion.gender]=gender
        this[Companion.email]=email
        addAddress(address,addres.Loc.Home,"0")
        addPhoneNumber(phoneNumber,"0")
        this.storageReference=storageReference
        putImage(profilePicture,email)
    }

    companion object{
        val name="Name"
        val age="Age"
        val gender="Gender"
        val email="Email"
        val address="Address"
        val phoneNumbers="PhoneNumbers"
        val profilePicture="ProfilePicture"

    }



}