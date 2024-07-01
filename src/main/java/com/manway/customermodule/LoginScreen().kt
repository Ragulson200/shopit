package com.manway.customermodule

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(context: Context,navigationControl:NavHostController){
    Text(text = "")
    val auth= FirebaseAuth.getInstance()
    if (auth.currentUser != null){
        navigationControl.navigate("HomeScreen")
    }
    var mail by remember {
        mutableStateOf("")
    }
    var showBottomSheet by remember { mutableStateOf(false) }
    var sqlCusHelper=SqlCusHelper(context)
    var editText: @Composable (Modifier, String, (String) -> String) -> String =
        { modifier, label, onResponse ->
            var text by remember { mutableStateOf("") }
            Column {
                TextField(value = text, singleLine = true, label = { Text(text = label, color = Color.Black) }, modifier = modifier.border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10)).fillMaxWidth(0.70f), onValueChange = { text = it; }, colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
                Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
                Spacer(modifier = Modifier.height(15.dp))
            }
            text
        }

    var editTextNum: @Composable (Modifier, String, (String) -> String) -> String =
        { modifier, label, onResponse ->
            var text by remember { mutableStateOf("") }
            Column {
                TextField(value = text, singleLine = true, label = { Text(text = label, color = Color.Black) }, modifier = modifier
                    .border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10))
                    .fillMaxWidth(0.70f), onValueChange = { text = it; }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
                Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
                Spacer(modifier = Modifier.height(15.dp))
            }
            text
        }



    var editTextAdr: @Composable (Modifier, String, (String) -> String) -> String =
        { modifier, label, onResponse ->
            var text by remember { mutableStateOf("") }
            Column {
                TextField(value = text, label = { Text(text = label, color = Color.Black) }, maxLines = 7, modifier = modifier
                    .border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(3))
                    .fillMaxWidth(0.70f), onValueChange = { text = it; }, colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
                Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
                Spacer(modifier = Modifier.height(15.dp))
            }
            text
        }


    var editTextMail: @Composable (Modifier, String, (String) -> String, @Composable (String) -> Unit) -> String =
        { modifier, label, onResponse, trailingIcon ->
            var text by remember { mutableStateOf("") }
            Column {
                TextField(value = text, singleLine = true, label = { Text(text = label, color = Color.Black) }, modifier = modifier
                    .border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10))
                    .fillMaxWidth(0.70f), onValueChange = { text = it; }, trailingIcon = { trailingIcon(text) }, colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
                Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
                Spacer(modifier = Modifier.height(15.dp))
            }
            text
        }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Login", fontSize = 25.sp, color = Color(0xFFAF9D95), modifier = Modifier.padding(15.dp))


        var password by remember {
            mutableStateOf("")
        }

        var loginAccount: @Composable (String) -> Unit = { email ->


            Column(Modifier.clickable { mail=email;showBottomSheet=true;  if(!auth.currentUser?.email.equals(email)) auth.signOut() }) {

                val firebaseStorage = Firebase.storage.reference.child("customerInfo/${email}.jpeg")
                var bitmap = remember {
                    mutableStateOf<Bitmap?>(null)
                }
                firebaseStorage.getBytes(1024 * 1024).addOnSuccessListener {
                    bitmap.value = DataBaseConverter.convertByteArrayToBitmap(it)
                    if (bitmap == null) Toast.makeText(context, "sucess", Toast.LENGTH_LONG).show()
                }

                Row(modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(0.dp)
                    .border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10)), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    bitmap.value?.let {
                        Image(bitmap = it.asImageBitmap(), modifier = Modifier
                            .fillMaxSize(0.15f)
                            .offset(x = 5.dp), contentDescription = "")
                    }
                        Text(text = email, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(15.dp))
                }
                Spacer(modifier = Modifier.height(15.dp))
            }


        }

       // sqlHelper.updateData(SqlCusHelper.customerInfo, " email", email, "isVerified", isVerfied)
        if (SqlCusHelper(context).dataFromSelectQuery("Select email from ${SqlCusHelper.customerInfo}", false).size != 0){
                for (s in SqlCusHelper(context).dataFromSelectQuery("Select email from ${com.manway.customermodule.SqlCusHelper.customerInfo}", false)){
                    loginAccount(s[1].toString())
            }
        }

            editTextMail(
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.75f),
                "Add Account(Email)",
                { "" }) { email: String ->
                val db = Firebase.firestore.collection("Users Data")
                Button(modifier = Modifier.padding(10.dp), colors =ButtonDefaults.buttonColors(containerColor = Color.LightGray), shape = RoundedCornerShape(5), onClick = {
                    db.whereEqualTo("Email", email).get().addOnCompleteListener {
                        it.result.forEach { t->
                            Log.e("ran", t.data["Email"].toString())
                            Toast.makeText(context, t.data["Email"].toString(),Toast.LENGTH_SHORT).show()
                        }
                        var arr=ArrayList<String>()
                        if (SqlCusHelper(context).dataFromSelectQuery("Select email from ${SqlCusHelper.customerInfo}", false).size != 0){
                            for (s in SqlCusHelper(context).dataFromSelectQuery("Select email from ${com.manway.customermodule.SqlCusHelper.customerInfo}", false)){
                                arr.add(s[1].toString())
                            }
                        }

                        if (it.result.size() == 0) {
                            Toast.makeText(context, "This Email not Registered", Toast.LENGTH_LONG).show()
                        } else {
                         if(!auth.currentUser?.email.equals(email)) auth.signOut()
                            mail=email
                            if(!arr.contains(email)) {
                                sqlCusHelper.addDataString(SqlCusHelper.customerInfo, "email", email)
                                sqlCusHelper.updateData(SqlCusHelper.customerInfo, " email", email, "isVerified",false)
                                sqlCusHelper.updateData(SqlCusHelper.customerInfo, "email", email, "isLogin", false)
                            }

                            showBottomSheet=true
                        }

                    }
                }
                ) { Text(text = "Go") }

            }

        Text(text = "Register New",
            Modifier
                .clickable { navigationControl.navigate("RegisterScreen") }
                .fillMaxWidth(0.75f), textAlign = TextAlign.End);

        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()


        Scaffold(
        ) { contentPadding ->
            // Screen content


            var isVerfied by remember { mutableStateOf(sqlCusHelper.getBoolean(SqlCusHelper.customerInfo, "isVerified", "email", mail)) }

            var passwordEntered by remember { mutableStateOf(false) }



            if (showBottomSheet) {
                ModalBottomSheet(modifier = Modifier.fillMaxHeight(0.5f), onDismissRequest = { showBottomSheet = false }, sheetState = sheetState) {
                    val firebaseStorage = Firebase.storage.reference.child("customerInfo/${mail}.jpeg")
                    var bitmap = remember { mutableStateOf<Bitmap?>(null) }
                    firebaseStorage.getBytes(1024 * 1024).addOnSuccessListener {
                        bitmap.value = DataBaseConverter.convertByteArrayToBitmap(it)
                        if (bitmap == null) Toast.makeText(context, "sucess", Toast.LENGTH_LONG).show()
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                     Icon(Icons.Filled.Close,contentDescription = "", modifier = Modifier.offset(x = -25.dp).fillMaxSize(0.10f).clickable { scope.launch { sheetState.hide() }.invokeOnCompletion { if (!sheetState.isVisible) { showBottomSheet = false } } })
                    }
                    bitmap.value?.let {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(bitmap =it.asImageBitmap() , modifier = Modifier.fillMaxSize(0.15f), contentScale = ContentScale.FillBounds, contentDescription ="" )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = mail, fontSize =18.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        editText(Modifier, "Password") {
                            password = it
                            "" }

                        if (passwordEntered) {
                            if (!isVerfied) {
                                Text("Verify your Account", fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().clickable { Toast.makeText(context, "Reset password link sent to Email", Toast.LENGTH_LONG).show()
                                            //  Toast.makeText(context,"Retry and check connection",Toast.LENGTH_LONG).show()
                                            // auth.createUserWithEmailAndPassword()
                                            auth.currentUser
                                                ?.reload()
                                                ?.addOnCompleteListener() {
                                                    if (!auth.currentUser?.isEmailVerified!!)
                                                    {
                                                        auth.currentUser
                                                            ?.sendEmailVerification()!!
                                                            .addOnCompleteListener {
                                                                if (it.isSuccessful) Toast.makeText(context, "The verification mail sent", Toast.LENGTH_LONG).show() else Toast.makeText(context, "Retry", Toast.LENGTH_LONG).show()
                                                            }
                                                    } else if (auth.currentUser?.isEmailVerified!! && auth.currentUser != null)
                                                    {
                                                        isVerfied = true
                                                        try
                                                        {
                                                            sqlCusHelper.updateData(
                                                                SqlCusHelper.customerInfo, " email", mail, "isVerified", isVerfied)
                                                        }
                                                        catch (e: Exception)
                                                        {
                                                            sqlCusHelper.addDataString(SqlCusHelper.customerInfo, "email", mail)
                                                            sqlCusHelper.updateData(SqlCusHelper.customerInfo, "email", mail, "isVerified", isVerfied)
                                                        }
                                                    }
                                                }
                                        })
                            } else {
                                Text("Verified successful", fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                 ValueTransfer.auth = auth.currentUser
                                navigationControl.navigate("HomeScreen")
                                Toast.makeText(context,"NextPage",Toast.LENGTH_LONG).show()
                            }
                        }
                            Spacer(modifier = Modifier.height(25.dp))
                            Row(modifier = Modifier.fillMaxWidth(0.75f), horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Forgot Password", modifier = Modifier.clickable {
                                    auth.sendPasswordResetEmail(mail).addOnCompleteListener {
                                        if (it.isSuccessful) Toast.makeText(context, "Reset password link sent to Email", Toast.LENGTH_LONG).show()
                                        else Toast.makeText(context, "Retry and check connection", Toast.LENGTH_LONG).show()
                                    }
                                })
                                Button(onClick = {

                                    try {
                                        auth.signInWithEmailAndPassword(mail, password)
                                            .addOnCompleteListener() {

                                                if (it.isSuccessful) passwordEntered = true
                                                if (it.isSuccessful) auth.currentUser?.reload()
                                                    ?.addOnCompleteListener() {
                                                        if (!auth.currentUser?.isEmailVerified!!) {
                                                            auth.currentUser
                                                                ?.sendEmailVerification()!!
                                                                .addOnCompleteListener {
                                                                    if (it.isSuccessful) Toast.makeText(context, "The verification mail sent", Toast.LENGTH_LONG).show() else Toast.makeText(context, "Retry", Toast.LENGTH_LONG).show()
                                                                }
                                                        } else {
                                                            isVerfied = true
                                                            try {
                                                                sqlCusHelper.updateData(SqlCusHelper.customerInfo, " email", mail, "isVerified",isVerfied)
                                                            } catch (e: Exception) {
                                                                sqlCusHelper.addDataString(SqlCusHelper.customerInfo, "email", mail)
                                                                sqlCusHelper.updateData(SqlCusHelper.customerInfo, "email", mail, "isVerified", isVerfied)
                                                            }
                                                        }
                                                    }
                                                else Toast.makeText(context, "The Password is wrong", Toast.LENGTH_LONG).show()

                                            }
                                            .addOnFailureListener {
                                                try {
                                                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                                } catch (e: Exception) {

                                                }

                                            }
                                    } catch (e: Exception) {

                                    }
                                }) {
                                    Text(text = "Ok")
                                }
                            }
                    }

                }
            }
        }

    }



}