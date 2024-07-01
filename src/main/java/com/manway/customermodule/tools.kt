package com.manway.customermodule

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import java.util.Random


val random= Random()

public fun randomizer(from:Int=0,to:Int):Int{
    return random.nextInt(to-from)+from
}

public fun RandamList(items:Array<String>):String{
    return items[randomizer(0,items.size-1)]
}

fun RandamColor():Color{
    val sam= Color(randomizer(0,255), randomizer(0,255), randomizer(0,255))
    return sam

}

fun RandamPaint():android.graphics.Paint{
    val paint= android.graphics.Paint()
    paint.setColor(android.graphics.Color.rgb(randomizer(0,255), randomizer(0,255), randomizer(0,255)))
    android.graphics.Color.rgb(randomizer(0,255), randomizer(0,255), randomizer(0,255))
    return paint
}

fun RandamColorhex():String{
    return "ff"+hex(randomizer(0,255))+ hex(randomizer(0,255))+ hex(randomizer(0,255))
}

private fun hex(n:Int):String{
    if(n<16){
        return "0"+Integer.toHexString(n)
    }
    else{
        return Integer.toHexString(n)
    }

}


public fun SearchByKeyMap(mapList:ArrayList<HashMap<String,String>>,ingnoreThis:Array<String> = arrayOf("")):HashSet<String>{
    val setList:HashSet<String> =HashSet()
    mapList.forEach {
        it.keys.forEach {s:String->
            if(!ingnoreThis.contains(s)){
                it[s]?.let { it1 -> setList.add(it1) }
            }

        }
    }
    return setList
}

public fun mergeSet(set1:HashSet<String>,set2:HashSet<String>):HashSet<String>{
    val joinSet:HashSet<String> = HashSet()
    set1.forEach{
        joinSet.add(it)
    }
    set2.forEach{
        joinSet.add(it)
    }
    return joinSet

}



//fun  RandomProductInfo(sqlHelper: SqlHelper): ProductInfo {
//
//    var i=0;
//    var productName:String= RandamList(arrayOf("sample1","sample2","sample3","sample4","sample5","sample6","sample7","sample8"))
//    var brandName:String= RandamList(arrayOf("Apple","pumo","Arc","swellow","begita","sepectra"))
//    var color= RandamColorhex()
//
//    val map1=ValueTransfer.list[randomizer(0,ValueTransfer.list.size-1)]
//
//
//    var info= ProductInfo(sqlHelper =sqlHelper,
//        productId = "$i++", productName = productName,
//        brandName = brandName,
//        productImageSources = randomBitmap(productName,brandName,color),
//        isFavorite = false,
//        amount = randomizer(250,25000),
//        discountEmi = "${randomizer(25,75)}%discount",
//        isCart = false,
//        color = color,
//        colorName =color,
//        size = randomizer(10,25).toFloat(),
//        productStock = randomizer(5,100),
//        unitType = "Qty",
//        isFreeDelivery =if(randomBoolean()) "Available" else null.toString(),
//
//        isCashOnDelivery ="Available",
//
//        emi = if(!randomBoolean()) null.toString() else "${randomizer(6,18)} Month",
//        customerSupport = "24*7",
//        returns = RandamList(arrayOf("2days","3days","7days")),
//        medicalUsage = if( randomBoolean() ) null.toString() else "Yes",
//
//        Exchangeable=if( randomBoolean() ) null.toString() else "available",
//        guaranty=if( randomBoolean() ) null.toString() else "${randomizer(1,5)}Years",
//        warranty=if( randomBoolean() ) null.toString() else "${randomizer(1,5)}Years",
//        refund=if( randomBoolean() ) null.toString() else "available",
//        waterResistance=if( randomBoolean() ) null.toString() else "${RandamList(arrayOf("100ppm","150ppm","250ppm","500ppm","750ppm","1000ppm"))}",
//        fire_heat_resistance=if( randomBoolean() ) null.toString() else "${RandamList(arrayOf("100","200","250","350","500","650","750","1000"))}C",
//        chemical_resistance=if( randomBoolean() ) null.toString() else "${RandamList(arrayOf("CH4","NAOH","NACL","OH"))}",
//        gasResistance=if( randomBoolean() ) null.toString() else "${RandamList(arrayOf("Co2","O3","N2O","NaCh3"))}",
//        electricalResistance=if( randomBoolean() ) null.toString() else "${randomizer(0,25)}V",
//        ratiationResistance=if( randomBoolean() ) null.toString() else "${randomizer(0,100)}mum",
//        iceResistance=if( randomBoolean() ) null.toString() else "${randomizer(0,10)*-1}C",
//
//
//        materialName = RandamList(arrayOf("plastic","cotton","leather","nylon","metal","rubber","carbon composite")),
//        shape_type = RandamList(arrayOf("shorts","paint","saree","watch","belt")),
//        modelName = "model#${randomizer(0,55)}${randomizer(12,155)}",
//
//        users =map1["users"].toString(),
//        usages = ArrayList(),
//        specMap = randomMap() ,
//        primaryType =map1["primaryType"].toString(),
//        secondaryType = map1["secondaryType"].toString(),
//        shape_Primary =map1["shape_Primary"].toString(),
//        shape_secondary = map1["shape_secondary"].toString()
//        , place_type = map1["place_type"].toString()
//    )
//
//    var map:HashMap<String,String> = HashMap()
//    map[ProductInfo.productName]=info.productName
//    map[ProductInfo.brandName]=info.brandName
//    map[ProductInfo.materialName]=info.materialName
//    map[ProductInfo.shape_type]=info.shape_type
//    map[ProductInfo.amount]=info.amount.toString()
//    map[ProductInfo.colorName]=info.colorName
//    map[ProductInfo.productSize]=info.productSize.toString()
//    map[ProductInfo.isFreeDelivery]=info.isFreeDelivery
//    map[ProductInfo.Exchangeable]=info.exchangeable
//    map[ProductInfo.guaranty]=info.guaranty
//    map[ProductInfo.warranty]=info.warranty
//    map[ProductInfo.refund]=info.refund
//    map[ProductInfo.waterResistance]=info.waterResistance
//    map[ProductInfo.fire_heat_resistance]=info.fire_heat_resistance
//    map[ProductInfo.chemical_resistance]=info.chemical_resistance
//    map[ProductInfo.gasResistance]=info.gasResistance
//    map[ProductInfo.electricalResistance]=info.electricalResistance
//    map[ProductInfo.ratiationResistance]=info.ratiationResistance
//    map[ProductInfo.iceResistance]=info.iceResistance
//    map[ProductInfo.users]=info.users
//    map["primaryType"]=map1["primaryType"].toString()
//    map["secondaryType"]=map1["secondaryType"].toString()
//    map["shape_Primary"]=map1["shape_Primary"].toString()
//    map["shape_secondary"]=map1["shape_secondary"].toString()
//
//
//    info.specMap=map
//
//    return info
//
//}

fun randomList(list:Array<String>){
    val l:ArrayList<String> = ArrayList()
    list.forEach {
        if(randomBoolean()){
            l.add(it)
        }
    }
}


fun randomMap():HashMap<String,String>{
    val l:HashMap<String,String> = HashMap()
    return l
}


fun randomBoolean():Boolean{
    return randomizer(0,10)%2 == 0
}

fun randomBitmap(productName:String, brandName:String, color:String):ArrayList<Bitmap>{
    val list:ArrayList<Bitmap> = ArrayList()
    var paint1= RandamPaint()
    paint1.setColor(android.graphics.Color.parseColor("#"+color.substring(2,color.length)))
    var paint2= RandamPaint()
    var paint3= RandamPaint()
    var paint4= RandamPaint()

    for(i in 1..randomizer(6,10) ) {
        val bitmap=Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        canvas.drawPaint(paint1)
//        canvas.drawText(productName+i.toString(), 25f, 15f, paint2)
//        canvas.drawText(brandName+i.toString(), 25f, 35f, paint3)
//        canvas.drawText(color+i.toString(), 25f, 55f, paint4)
        list.add(bitmap)
    }
    return list
}

@Preview
@Composable
public fun testSql(){
//    Text(DataBaseConverter.convertKeyTOTable(RandomProductInfo() as java.util.HashMap<String, Any>,"sample"))
//    Log.e("sql",DataBaseConverter.convertKeyTOTable(RandomProductInfo() as java.util.HashMap<String, Any>,"sample"))

    //create table if not EXISTS sample (id integer primary key autoincrement,emi text,color text,discount_emi text,productSize REAL,isCart NUMERIC,chemical_Resistance text,waterResistance text,productName text,medicalUsage text,unitType text,expiryDate text,productImageSources blob,Exchangeable text,rationResistance text,warranty text,id text,isCashOnDelivery text,Usages blob,guaranty text,gasResistance text,customerSupport text,brandName text,amount integer,productId text,productStock integer,fire_heat_resistance text,specMap blob,isFreeDelivery text,users text,iceResistance text,materialName text,modelName text,shape_type text,returns text,electricalResistance text,isFavorite NUMERIC,refund text)

    // create table if not EXISTS sample (id integer primary key autoincrement,emi text,color text,discount_emi text,productSize REAL,isCart NUMERIC,chemical_Resistance text,waterResistance text,productName text,medicalUsage text,unitType text,expiryDate text,productImageSources blob,Exchangeable text,rationResistance text,warranty text,isCashOnDelivery text,Usages blob,guaranty text,gasResistance text,customerSupport text,brandName text,amount integer,productId text,productStock integer,fire_heat_resistance text,specMap blob,isFreeDelivery text,users text,iceResistance text,materialName text,modelName text,shape_type text,returns text,electricalResistance text,isFavorite NUMERIC,refund text)

//    CREATE TABLE "sample" (
//        "id"	integer,
//        "amount"	integer,
//        "productStock"	integer,
//        "isFavorite"	NUMERIC,
//        "isCart"	NUMERIC,
//        "productSize"	NUMERIC,
//        "Usages"	NUMERIC,
//        "productImage1"	BLOB,
//        "productImage2"	BLOB,
//        "productImage3"	BLOB,
//        "productImage4"	BLOB,
//        "productImage5"	BLOB,
//        "productImage6"	BLOB,
//        "productImage7"	BLOB,
//        "productImage8"	BLOB,
//        "productImage9"	BLOB,
//        "productImage10"	BLOB,
//        "specMap"	TEXT,
//        "emi"	text,
//        "color"	text,
//        "discount_emi"	text,
//        "chemical_Resistance"	text,
//        "waterResistance"	text,
//        "productName"	text,
//        "medicalUsage"	text,
//        "unitType"	text,
//        "expiryDate"	text,
//        "Exchangeable"	text,
//        "rationResistance"	text,
//        "warranty"	text,
//        "isCashOnDelivery"	text,
//        "guaranty"	text,
//        "gasResistance"	text,
//        "customerSupport"	text,
//        "brandName"	text,
//        "productId"	text,
//        "fire_heat_resistance"	text,
//        "isFreeDelivery"	text,
//        "users"	text,
//        "iceResistance"	text,
//        "materialName"	text,
//        "modelName"	text,
//        "shape_type"	TEXT,
//        "returns"	text,
//        "electricalResistance"	text,
//        "refund"	text,
//        PRIMARY KEY("id" AUTOINCREMENT)
//    )
}
