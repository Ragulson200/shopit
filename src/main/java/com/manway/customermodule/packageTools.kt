package com.manway.customermodule

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class packageTools {
    companion object{
        fun textBitmap(str:String): Bitmap {
            val bitmap: Bitmap = Bitmap.createBitmap(1024,1024, Bitmap.Config.ARGB_8888)
            val canvas: Canvas = Canvas(bitmap)
            val paint: Paint = Paint()
            val paint1: Paint = Paint()
            paint.color= android.graphics.Color.parseColor("#"+ RandamList(arrayOf("EF5350","EC407A","AB47BC","7E57C2","5C6BC0","42A5F5","29B6F6","26C6DA","26A69A","66BB6A","9CCC65","D4E157","FFEE58","FFCA28","FFA726","FF7043")))
            Color(0xFFFF7043)
            paint1.color=android.graphics.Color.parseColor("#000000")
            paint1.textSize=((512.0*0.85).toFloat())
            canvas.drawCircle(512f,512f,512f*0.75f,paint)
            try {
                canvas.drawText(str[0].uppercase() + str[1].lowercase(), 270f, 612f, paint1)
            }catch (e:Exception){
                canvas.drawText(str[0].uppercase() , 270f, 612f, paint1)
            }
            return bitmap
        }
    }

}