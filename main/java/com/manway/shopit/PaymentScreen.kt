package com.manway.shopit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.manway.productmodule.OrderInfo
import com.manway.productmodule.cdb
import com.manway.productmodule.orders
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
public fun PaymentScreen(mainActivity: MainActivity,navController: androidx.navigation.NavHostController){
upiPayments(mainActivity =mainActivity, orders,navController )
}

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun upiPayments(mainActivity: MainActivity,orders:List<OrderInfo>,navController: androidx.navigation.NavHostController ) {

    var editTextGo: @Composable (Modifier, String, @Composable ()->Unit, (String) -> String) -> String = { modifier, label, trailingIcon, onResponse ->
        var text by remember { mutableStateOf("") }
        Column {
            TextField(value = text, singleLine = true, label = { Text(text = label, color = Color.Black) }, modifier = modifier.border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10)).fillMaxWidth(0.70f), onValueChange = { text = it; },trailingIcon=trailingIcon ,colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
            Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
            Spacer(modifier = Modifier.height(15.dp))
        }
        text
    }

    var orderList =Firebase.firestore.collection("OrderList")

    var cost=orders.sumBy { it.cost.toInt() }.toFloat()

    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    val amount by remember { mutableStateOf(cost.toString()) }
    val upiId by remember { mutableStateOf("6382174793@paytm") }
    val name by remember { mutableStateOf("ShopIt Delivery") }
    val description by remember { mutableStateOf("Buy the ${orders.size} Products") }
    var changeCashMode by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        //  Image(painter = , contentDescription = "")
        Row(Modifier.fillMaxWidth()) { Text(text ="" ) }
        Text(text = "$cost", fontSize =50.sp )
        Spacer(modifier = Modifier.height(25.dp))
        Row(horizontalArrangement = Arrangement.Absolute.Left, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(55.dp))
            RadioButton(selected =changeCashMode , onClick = {changeCashMode=!changeCashMode})
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Cash On Delivery")
        }
        Spacer(modifier = Modifier.height(25.dp))
        Row(horizontalArrangement = Arrangement.Absolute.Left, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(55.dp))
            RadioButton(selected =!changeCashMode , onClick = {changeCashMode=!changeCashMode})
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Upi payments")
        }
        Button(onClick = {
            if(!changeCashMode){
                val c: Date = Calendar.getInstance().getTime()
                val df = SimpleDateFormat("ddMMyyyyHHmmss", Locale.ENGLISH)
                val transcId: String = df.format(c)
                makePayment(amount, upiId, name, description, transcId, ctx, activity!!, mainActivity)
            }
            else{
                orders.forEach {
                    orderList.add(it.getMap())
                }
                com.manway.productmodule.orders = listOf()
               // navController.navigate("ProductScreen")
            }
        }) {
            Text(text = if(changeCashMode) "PlaceOrder" else "MakePayment")
        }

    }

}

private fun makePayment(amount: String, upi: String, name: String, desc: String, transcId: String, ctx: Context, activity: Activity, mainActivity: PaymentStatusListener) {
    try {
        val easyUpiPayment = EasyUpiPayment(activity) {
            this.paymentApp = PaymentApp.ALL
            this.payeeVpa = upi
            this.payeeName = name
            this.transactionId = transcId
            this.transactionRefId = transcId
            this.payeeMerchantCode = transcId
            this.description = desc
            this.amount = amount
        }
        easyUpiPayment.setPaymentStatusListener(mainActivity)


        easyUpiPayment.startPayment()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(ctx, e.message, Toast.LENGTH_SHORT).show()
    }
}