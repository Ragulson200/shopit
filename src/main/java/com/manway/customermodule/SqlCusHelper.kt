package com.manway.customermodule

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream

class SqlCusHelper(context: Context?) : SQLiteOpenHelper(context, db, null, 1) {
    init {
        val db = this.writableDatabase
    }

    //carefully if not include space database could not create
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS customerInfo ( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , email text ,  isVerified NUMERIC , isLogin NUMERIC )")
      // sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS productInfo ( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, dummy text, place_type text, primaryType text, secondaryType text, shape_Primary text, shape_secondary text, amount integer, productStock	integer, isFavorite	NUMERIC, isCart	NUMERIC, productSize NUMERIC, Usages NUMERIC, productImage1	BLOB, productImage2	BLOB, productImage3	BLOB, productImage4	BLOB, productImage5	BLOB, productImage6	BLOB, productImage7	BLOB, productImage8	BLOB, productImage9	BLOB, productImage10 BLOB, specMap	TEXT, emi	text, color	text, colorName	text, discount_emi	text, chemical_Resistance	text, waterResistance	text, productName	text, medicalUsage	text, unitType	text, expiryDate	text, Exchangeable	text, rationResistance	text, warranty	text, isCashOnDelivery	text, guaranty	text, gasResistance	text, customerSupport	text,brandName	text,productId	text, fire_heat_resistance	text, isFreeDelivery	text, users	text, iceResistance	text, materialName	text, modelName	text, shape_type TEXT, returns	text, electricalResistance text,refund	text)")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+productInfo);
    }

    fun addDataString(tableName: String?, key: String?, value: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(key, value)
        val result = db.insert(tableName, null, contentValues)
        return result != -1L
    }

    @SuppressLint("Range")
    fun filterColoumnList(
        tableName: String,
        coloumn: String?,
        values: ArrayList<String?>
    ): ArrayList<String> {
        val filterList = ArrayList<String>()
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        while (rs.moveToNext()) {
            var i = 0
            while (i < rs.columnCount) {
                try {
                    if (values.contains(rs.getString(i))) {
                        filterList.add(rs.getString(rs.getColumnIndex(coloumn)))
                        break
                    }
                    i++
                } catch (f: SQLiteException) {
                } catch (e: Exception) {
                }
                i++
            }
        }
        return filterList
    }

    fun delete(tableName: String?, id: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        val strings = arrayOfNulls<String>(1)
        strings[0] = id.toString()
        val result = java.lang.Long.valueOf(db.delete(tableName, "id = ?", strings).toLong())
        return result != -1L
    }

    fun dataFromSelectQuery(query: String?, repeat: Boolean): ArrayList<HashMap<Int, String>> {
        val db = this.writableDatabase
        val rs = db.rawQuery(query, null)
        val list = ArrayList<String>()
        val list1 = ArrayList<String>()
        val list2 = ArrayList<HashMap<Int, String>>()
        while (rs.moveToNext()) {
            for (i in 0 until rs.columnCount) {
                try {
                    if (!(list.contains(rs.getString(i)) && list1.contains(rs.getColumnName(i))) || repeat) {
                        val map = HashMap<Int, String>()
                        if (rs.getString(i) != null && rs.getString(i) != "" && rs.getString(i) != "null") {
                            list.add(rs.getString(i))
                            list1.add(rs.getColumnName(i))
                            map[1] = rs.getString(i)
                            map[0] = rs.getColumnName(i)
                            list2.add(map)
                        }
                    }
                } catch (f: SQLiteException) {
                }
            }
        }
        return list2
    }

    fun dataFromSelectQueryBitmap(query: String?): ArrayList<HashMap<Int, Bitmap>> {
        val db = this.writableDatabase
        val rs = db.rawQuery(query, null)
        val list2 = ArrayList<HashMap<Int, Bitmap>>()
        while (rs.moveToNext()) {
            for (i in 0 until rs.columnCount) {
                try {
                    val map = HashMap<Int, Bitmap>()
                    if (rs.getBlob(i) != null) {
                        map[1] =
                            DataBaseConverter.convertByteArrayToBitmap(
                                rs.getBlob(i)
                            )
                        list2.add(map)
                    }
                } catch (f: SQLiteException) {
                }
            }
        }
        return list2
    }

    fun keyList(query: String?): ArrayList<String> {
        val db = this.writableDatabase
        val rs = db.rawQuery(query, null)
        var i = 0
        val list1 = ArrayList<String>()
        for (s in rs.columnNames) {
            list1.add(i++.toString() + "." + s)
        }
        return list1
    }

    fun getTableString(tableName: String): String {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        val buffer = StringBuffer("\n")
        var i = 0
        while (rs.moveToNext()) {
            i = 0
            while (true) {
                try {
                    buffer.append(rs.getString(i++) + " ")
                } catch (f: SQLiteException) {
                    buffer.append("blob" + " ")
                } catch (e: Exception) {
                    buffer.append("\n")
                    break
                }
            }
        }
        return buffer.toString()
    }

    fun getTableSearchList(tableName: String): ArrayList<String> {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        val list = ArrayList<String>()
        while (rs.moveToNext()) {
            for (i in 0 until rs.columnCount) {
                try {
                    if (!list.contains(rs.getString(i))) {
                        list.add(rs.getString(i))
                    }
                } catch (f: SQLiteException) {
                }
            }
        }
        return list
    }

    fun getTableColoumn(tableName: String, index: Int): ArrayList<String> {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        val list = ArrayList<String>()
        val i = 0
        while (rs.moveToNext()) {
            list.add(rs.getString(index))
        }
        return list
    }

    @SuppressLint("Range")
    fun getTableColoumn(tableName: String, coloumnName: String?): ArrayList<String> {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        val list = ArrayList<String>()
        val i = 0
        while (rs.moveToNext()) {
            list.add(rs.getString(rs.getColumnIndex(coloumnName)))
        }
        return list
    }

    fun getTableRow(tableName: String, rowIndex: Int): ArrayList<String> {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        val buffer = StringBuffer("\n")
        val list = ArrayList<String>()
        var k = 0
        while (rs.moveToNext()) {
            if (k == rowIndex) {
                for (i in 0 until rs.columnCount) {
                    try {
                        list.add(rs.getString(i))
                    } catch (e: Exception) {
                        list.add("blob")
                    }
                }
            }
            k++
        }
        return list
    }

    //it only worked on primary value
    fun getPrimaryKeyIndex(tableName: String, primaryKey: String?, primaryValue: String): Int {
        return getTableColoumn(tableName, primaryKey).indexOf(primaryValue)
    }

    fun getTableRow(
        tableName: String,
        primaryKey: String?,
        primaryValue: String
    ): ArrayList<String> {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        val buffer = StringBuffer("\n")
        val list = ArrayList<String>()
        var k = 0
        while (rs.moveToNext()) {
            if (k == getPrimaryKeyIndex(tableName, primaryKey, primaryValue)) {
                for (i in 0 until rs.columnCount) {
                    try {
                        list.add(rs.getString(i))
                    } catch (e: Exception) {
                        list.add("")
                    }
                }
            }
            k++
        }
        return list
    }

    //it only worked on primary valuy
    fun deleteTableData(tableName: String) {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        var i = 1
        while (rs.moveToNext()) {
            val str = arrayOf(i++.toString())
            db.delete(tableName, " id = ?", str)
        }
    }

    fun contains(tableName: String, coloumn: Int, value: String): Boolean {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT * FROM $tableName", null)
        val buffer = StringBuffer("\n")
        var bol = false
        val i = 0
        while (rs.moveToNext()) {
            try {
                if (rs.getString(coloumn) == value) {
                    bol = true
                    break
                } else {
                    bol = false
                }
            } catch (e: Exception) {
                bol = false
            }
        }
        return bol
    }

    fun contains(tableName: String, coloumn: String?, value: String): Boolean {
        val db = this.writableDatabase
        val rs = db.rawQuery("SELECT $coloumn FROM $tableName", null)
        val buffer = StringBuffer("\n")
        val bol = false
        val list = ArrayList<String>()
        var i = 0
        while (rs.moveToNext()) {
            list.add(rs.getString(i++))
        }
        return list.contains(value)
    }

    fun getByteArray(tableName: String, coloumn: Int, row: Int): ByteArray {
        val db = this.writableDatabase
        @SuppressLint("Recycle") val rs = db.rawQuery("SELECT * FROM $tableName", null)
        var bytes = ByteArray(0)
        var i = 0
        while (rs.moveToNext()) {
            if (row == i) {
                bytes = rs.getBlob(coloumn)
                break
            }
            i++
        }
        return bytes
    }


    @SuppressLint("Range")
    fun getString(
        tableName: String,
        coloumn: String,
        primaryKey: String,
        primaryValue: String
    ): String {
        val db = this.writableDatabase
        //  Cursor rs=db.rawQuery("SELECT * FROM "+tableName,null);
        val rs =
            db.rawQuery("SELECT $coloumn from $tableName where $primaryKey='$primaryValue'", null)
        val i = 0
        var s = ""
        while (rs.moveToNext()) {
            s = rs.getString(rs.getColumnIndex(coloumn))
        }
        return s
    }

    @SuppressLint("Range")
    fun getBoolean(
        tableName: String,
        coloumn: String,
        primaryKey: String,
        primaryValue: String
    ): Boolean {
        val db = this.writableDatabase
        //  Cursor rs=db.rawQuery("SELECT * FROM "+tableName,null);
        val rs =
            db.rawQuery("SELECT $coloumn from $tableName where $primaryKey='$primaryValue'", null)
        val b: Boolean
        var s = 0
        while (rs.moveToNext()) {
            s = rs.getInt(rs.getColumnIndex(coloumn))
        }
        b = s != 0
        return b
    }

    @SuppressLint("Range")
    fun getFloat(tableName: String, coloumn: String?, row: Int): Float? {
        val db = this.writableDatabase
        @SuppressLint("Recycle") val rs = db.rawQuery("SELECT * FROM $tableName", null)
        var i = 0
        var s: Float? = null
        while (rs.moveToNext()) {
            if (row == i) {
                s = rs.getFloat(rs.getColumnIndex(coloumn))
                break
            }
            i++
        }
        return s
    }

    @SuppressLint("Range")
    fun getDouble(tableName: String, coloumn: String?, row: Int): Double? {
        val db = this.writableDatabase
        @SuppressLint("Recycle") val rs = db.rawQuery("SELECT * FROM $tableName", null)
        var i = 0
        var s: Double? = null
        while (rs.moveToNext()) {
            if (row == i) {
                s = rs.getDouble(rs.getColumnIndex(coloumn))
                break
            }
            i++
        }
        return s
    }

    @SuppressLint("Range")
    fun getInt(tableName: String, coloumn: String?, row: Int): Int {
        val db = this.writableDatabase
        @SuppressLint("Recycle") val rs = db.rawQuery("SELECT * FROM $tableName", null)
        var i = 0
        var s = 0
        while (rs.moveToNext()) {
            if (row == i) {
                s = rs.getInt(rs.getColumnIndex(coloumn))
                break
            }
            i++
        }
        return s
    }

    @SuppressLint("Range")
    fun getBitmap(
        tableName: String,
        coloumn: String,
        primaryKey: String,
        primaryValue: String
    ): Bitmap {
        val db = this.writableDatabase
        //  Cursor rs=db.rawQuery("SELECT * FROM "+tableName,null);
        val rs =
            db.rawQuery("SELECT $coloumn from $tableName where $primaryKey='$primaryValue'", null)
        var s: ByteArray? = null
        while (rs.moveToNext()) {
            s = rs.getBlob(rs.getColumnIndex(coloumn))
        }
        return convertByteArrayToBitmap(s)
    }

    fun updateData(
        tableName: String?,
        whereCase: String?,
        keyValue: String,
        key: String?,
        value: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(key, value)
        db.update(tableName, contentValues, whereCase, arrayOf(keyValue))
        return true
    }

    fun updateData(
        tableName: String?,
        whereCase: String?,
        keyValue: String,
        key: String?,
        value: Float
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(key, value)
        db.update(tableName, contentValues,"$whereCase = ?", arrayOf(keyValue))
        return true
    }

    fun updateData(
        tableName: String?,
        whereCase: String?,
        keyValue: String,
        key: String?,
        value: Boolean
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(key, value)
        val s = arrayOfNulls<String>(1)
        s[0]=keyValue
        try {
            db.update(tableName, contentValues, "$whereCase = ?", s)
            Log.e("ragul","sucess")
        }
        catch (e:Exception){
            for(k in  arrayOf(keyValue)) Log.e("ragul",k)
        }

        return true
    }

    fun updateData(
        tableName: String?,
        whereCase: String?,
        keyValue: String,
        key: String?,
        value: Bitmap
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(key, convertImageToByteArray(value))
        db.update(tableName, contentValues, "$whereCase = ?", arrayOf(keyValue))
        return true
    }

    companion object {
        const val db = "shopIt.db"
        const val productInfo = "productInfo"
        @JvmField
        var customerInfo = "customerInfo"
        fun convertImageToByteArray(bitmap: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            return stream.toByteArray()
        }

        fun convertByteArrayToBitmap(byteArray: ByteArray?): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        }
    }
}