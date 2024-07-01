package com.manway.shopit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.manway.customermodule.LoginScreen
import com.manway.customermodule.ProfileScreen
import com.manway.customermodule.RegisterScreen
import com.manway.productmodule.CartScreen
import com.manway.productmodule.FavScreen
import com.manway.productmodule.HomeScreen
import com.manway.productmodule.OptProductData
import com.manway.productmodule.OrderScreen
import com.manway.productmodule.ProductInfo
import com.manway.productmodule.ProductScreen
import com.manway.productmodule.cdb
import com.manway.productmodule.contextAll
import com.manway.productmodule.cstorageReference
import com.manway.productmodule.mapData
import com.manway.productmodule.optimizeProductData
import com.manway.productmodule.orderListScreen
import com.manway.productmodule.orders
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.TransactionDetails


class MainActivity() : ComponentActivity(),PaymentStatusListener
{

    companion object{
        var uri: Uri?=null
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        cdb = Firebase.firestore.collection("ProductDetails")

        super.onCreate(savedInstanceState)
        setContent {

            cdb.get().addOnSuccessListener {
                var list=ArrayList<ProductInfo>()
                var lis = ArrayList<mapData>()
                it.forEach {
                    list.add(ProductInfo(cdb, cstorageReference,it.data[ProductInfo.productId].toString()))
                    it.data.keys.forEach { s -> try { lis.add(mapData(s, it[s].toString())) } catch (_: Exception) { } }

                }
                optimizeProductData= OptProductData(list,lis)
            }


            contextAll= LocalContext.current
            val activity = this
            val navigationControl = rememberNavController()

            NavHost(navController =navigationControl , startDestination = "SplashScreen" ){

                
                composable("RegisterScreen"){
                    RegisterScreen(context=activity, navigationControl = navigationControl)
                }

                composable("LoginScreen"){
                    LoginScreen(context= LocalContext.current,navigationControl)
                }

                composable("HomeScreen"){
                    HomeScreen(context=activity,navigationControl=navigationControl)
                }

                composable("SplashScreen"){
                    SplashScreen(context = LocalContext.current, navigationControl = navigationControl)
                   //ProfileScreen(this@MainActivity,navigationControl)
                }
                
                composable("ProductScreen"){
                    ProductScreen(context=activity,navigationControl=navigationControl)
                }

                composable("OrderScreen"){
                    OrderScreen(context=activity,navigationControl=navigationControl)
                }

                composable("PaymentScreen"){
                    PaymentScreen(this@MainActivity,navigationControl)
                }

                composable("ProfileScreen"){
                    ProfileScreen(this@MainActivity,navigationControl)
                }

                composable("OrderPlacedScreen"){

                }
                composable("LikeProductScreen"){
                        FavScreen(context = this@MainActivity, navigationControl =navigationControl )
                }
                composable("CartProductScreen"){
                    CartScreen(context = this@MainActivity, navigationControl = navigationControl)
                }
                composable("OrderListScreen"){
                    orderListScreen(context = this@MainActivity, navigationControl = navigationControl)
                }

            }

            threadFlow()


              // navigationControl.navigate("RegisterScreen")
        }


    }

//    override fun addOnTrimMemoryListener(listener: Consumer<Integer>)
//    {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeOnTrimMemoryListener(listener: Consumer<Integer>)
//    {
//        TODO("Not yet implemented")
//    }


    override fun onTransactionCancelled()
    {
        Toast.makeText(contextAll,"The payment Cancelled",Toast.LENGTH_LONG).show()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails)
    {
        var orderList = Firebase.firestore.collection("OrderList")
        orders?.let {
            it.forEach {
                orders.forEach {
                    orderList.add(it.copy(paymentStatus = true).getMap())
                }
            }
        }
        orders= listOf()
        Toast.makeText(contextAll,"The payment Completed",Toast.LENGTH_LONG).show()
    }

    private  val PICK_IMAGE_REQUEST = 1

   fun Activity.launchImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
               uri=it
            }
        }
    }


}









