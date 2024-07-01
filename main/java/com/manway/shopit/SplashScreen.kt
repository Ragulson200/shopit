package com.manway.shopit

import android.content.Context

import android.view.View
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

import androidx.navigation.NavHostController
import kotlinx.coroutines.delay


@Composable
public fun SplashScreen(navigationControl:NavHostController,context:Context){


//                    AndroidView(modifier =Modifier.fillMaxSize(), factory = {
//                        val v:View=View.inflate(context, com.manway.customermodule.R.layout.splashlayout,null)
//                        v.findViewById<WebView>(com.manway.customermodule.R.id.webAni).apply {
//                            this.loadUrl("file:///android_asset/shopItAnimation.html")
//                        }
//                        WebView(it).apply {
//                        this.loadUrl("file:///android_assets/shopItAnimation.html")
//                         }
//
//                    })
                   LaunchedEffect(key1 = true) { delay(1000L);navigationControl.navigate("LoginScreen") }
}