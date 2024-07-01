package com.manway.customermodule

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.Composable
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.manway.customermodule.packageTools.Companion.textBitmap
import java.util.regex.Pattern
import kotlin.concurrent.thread

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen( modifier: Modifier = Modifier,context: Activity,navigationControl:NavHostController) {
    var storageReference = Firebase.storage.reference
    var db = Firebase.firestore
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var sqlHelper = SqlCusHelper(context)



    var text by remember { mutableStateOf(0) }

    var name by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }

    var address by remember { mutableStateOf("") }

    var age by remember { mutableStateOf(0) }

    var gender by remember { mutableStateOf("Male") }

    var contactNumber by remember { mutableStateOf(0L) }

    var password by remember { mutableStateOf("") }

    var retypePassword by remember { mutableStateOf("") }

    var verificationStatus by remember { mutableStateOf("Verify") }

    var isVerfied by remember { mutableStateOf(false) }


    var registerAction:()->Unit={

        auth.signOut()
        if (password == retypePassword && !address.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context,
                    OnCompleteListener<AuthResult?> {
                        if (it.isSuccessful) {
                            auth.currentUser?.reload()?.addOnCompleteListener {
                                if (!auth.currentUser?.isEmailVerified!!) {
                                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verificationStatus = "Check verification status" }
                                        sqlHelper.addDataString(SqlCusHelper.customerInfo, "email", email)
                                        sqlHelper.updateData(SqlCusHelper.customerInfo, " email", email, "isVerified", isVerfied)
                                        sqlHelper.updateData(SqlCusHelper.customerInfo, "email", email, "isLogin", false)
                                        Firebase.firestore.collection("Users Data").document(email).set(customerInfo(name, age, gender, email, contactNumber, address, textBitmap(name), storageReference))
                                        Toast.makeText(context, "Sent verification link into Email", Toast.LENGTH_LONG).show()
                                    } else {
                                        isVerfied = true;
                                        verificationStatus = "Verified successfully"
                                        sqlHelper.updateData(SqlCusHelper.customerInfo, " email", email, "isVerified", isVerfied)
                                        sqlHelper.updateData(SqlCusHelper.customerInfo, "email", email, "isVerfied", isVerfied)
                                        navigationControl.navigate("LoginScreen")
                                    }
                                }


                        } else {
                            if (auth.currentUser?.email != null) Toast.makeText(context, "Allready Exsists Account", Toast.LENGTH_LONG).show()
                        }


                    })
                .addOnFailureListener {
                    auth.currentUser?.reload()?.addOnCompleteListener {
                        if (auth.currentUser?.isEmailVerified!!) {
                            verificationStatus="verified"
                            Toast.makeText(context, "Go to Login page", Toast.LENGTH_LONG).show()
                        }
                    }
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
        } else {
            auth.currentUser?.reload()?.addOnSuccessListener {
                if (!auth.currentUser?.isEmailVerified!!) { isVerfied = true;sqlHelper.updateData(SqlCusHelper.customerInfo, "email", email, "isVerfied", isVerfied) }
            }
            Toast.makeText(context, "Enter the Password correctly and enter the address", Toast.LENGTH_LONG).show()

        }

    }

        var editText: @Composable (Modifier,String, String, (String) -> String) -> String = { modifier,intial, label, onResponse ->
                var text by remember { mutableStateOf(intial) }
                Column {
                    TextField(value = text, singleLine = true, label = { Text(text = label, color = Color.Black) }, modifier = modifier.border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10)).fillMaxWidth(0.70f), onValueChange = { text = it; }, colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
                    Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
                    Spacer(modifier = Modifier.height(15.dp))
                }
                text
            }

        var editTextNum: @Composable (Modifier, String,String, (String) -> String) -> String = {  modifier,intial, label, onResponse ->
                var text by remember { mutableStateOf(intial) }
                Column {
                    TextField(value = text, singleLine = true, label = { Text(text = label, color = Color.Black) }, modifier = modifier.border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10)).fillMaxWidth(0.70f), onValueChange = { text = it; }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
                    Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
                    Spacer(modifier = Modifier.height(15.dp))
                }
                text
            }

        var editTextAdr: @Composable (Modifier, String,String, (String) -> String) -> String = { modifier,intial, label, onResponse ->
                var text by remember { mutableStateOf(intial) }
                Column {
                    TextField(value = text, label = { Text(text = label, color = Color.Black) }, maxLines = 7, modifier = modifier.border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(3)).fillMaxWidth(0.70f), onValueChange = { text = it; }, colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
                    Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
                    Spacer(modifier = Modifier.height(15.dp))
                }
                text
            }


        var editTextMail: @Composable (Modifier,String, String, (String) -> String, @Composable () -> Unit) -> String = { modifier,intial, label, onResponse, trailingIcon ->
                var text by remember { mutableStateOf(intial) }
                Column {
                    TextField(value = text, singleLine = true, label = { Text(text = label, color = Color.Black) }, modifier = modifier.border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10)).fillMaxWidth(0.70f), onValueChange = { text = it; }, trailingIcon = { trailingIcon() }, colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent))
                }
                Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp))
                Spacer(modifier = Modifier.height(15.dp))
                text
            }


        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Register", fontSize = 25.sp, color = Color(0xFFAF9D95), modifier = Modifier.padding(15.dp))
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            Column {
                var next by remember { mutableStateOf(false) }
                if (!next) {
                    Row {
                        editText(Modifier.fillMaxWidth(.60f),name, "Name") {
                            val k = when {
                                it.isEmpty() -> "Empty"
                                it.length < 3 -> "at least 3 letters"
                                else -> ""
                            }
                            if (k.isEmpty()) {
                                name = it
                            } else {
                                name = "null"
                            }
                            k
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        editTextNum(Modifier.fillMaxWidth(0.50f),age.toString(), "Age") {
                            val k = try {
                                when {
                                    it.toInt() in 18..100 -> ""
                                    it.toInt() < 18 -> "18 below"
                                    else -> "100 above"
                                }
                            } catch (e: Exception) {
                                "Empty"
                            }
                            if (k.isEmpty()) {
                                age = it.toInt()
                            } else {
                                age = 0
                            }
                            k

                        }
                    }
                    editTextNum(Modifier.fillMaxWidth(0.8f),contactNumber.toString(), "Contact Number") {
                        val k = when {
                            customerInfo.phoneNumbe.checkPhoneNumber(it) -> ""
                            else -> "This not phone Number"
                        }
                        if (k.isEmpty()) {
                            contactNumber = it.toLong()
                        } else {
                            contactNumber = 0
                        }
                        k
                    }
                    Text(text = gender)

                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth(0.75f)) {
                        Image(painter = painterResource(id = R.drawable.male), contentDescription = "", modifier = Modifier.clickable { gender = "Male" }.fillMaxSize(0.23f))
                        Image(painter = painterResource(id = R.drawable.female), contentDescription = "", modifier = Modifier.clickable { gender = "Female" }.fillMaxSize(0.23f))
                        Image(painter = painterResource(id = R.drawable.others), contentDescription = "", modifier = Modifier.clickable { gender = "Others" }.fillMaxSize(0.23f))
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                } else {
                    editTextAdr(Modifier.fillMaxWidth(0.75f).height(200.dp),address, "Address") {
                        val k = when {
                            it.isEmpty() -> "Empty"
                            else -> ""
                        }
                        if (k.isEmpty()) {
                            address = it
                        } else {
                            address = "null"
                        }
                        k
                    }
                    editTextMail(Modifier.fillMaxWidth(0.75f),email, "Email", {
                        val k = when {
                            it.isEmpty() -> "Empty"
                            else -> ""
                        }
                        if (k.isEmpty()) {
                            email = it
                        } else {
                            email = "null"
                        }
                        k
                    }) {
                        Text(text = verificationStatus, Modifier.padding(10.dp).clickable { registerAction() })



                    }


                    editText(Modifier.fillMaxWidth(0.75f),password,"Password") { val k = when {
                            it.isEmpty() -> "Empty"
                            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
                                .matcher(it).find() -> ""
                            else -> "At-least one capital letters,small letters, number,one specialCharters minimum 8 total letters"
                        }
                        if (k.isEmpty()) { password = it } else { password = "null" }
                        k
                    }
                    editText(Modifier.fillMaxWidth(0.75f),retypePassword, "RetypePassword") {
                        val k = when {
                            it.isEmpty() -> "Empty"
                            password == retypePassword -> ""
                            else -> "Password not Matched"
                        }
                        retypePassword = it
                        k
                    }
                }

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth(0.75f)) {
                    if (!name.equals("null") && age != 0 && contactNumber != 0L && !address.equals("null") && !email.equals(
                            "null"
                        )
                    ) Button(onClick = { next = !next }, shape = RoundedCornerShape(10), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))) {
                        Text(text = if (!next) "Next" else "Prev")
                    }
                    if (isVerfied) Button(
                        onClick = { next = !next },
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                    ) {
                        Text(text = "Finish")
                        //move to next activity
                    }
                }

                Row(Modifier.fillMaxWidth(0.75f), horizontalArrangement = Arrangement.End) {
                    Text(text = "Log in", Modifier.clickable { navigationControl.navigate("LoginScreen") })
                }
                //  Text(text = sqlHelper.getTableString(SqlCusHelper.customerInfo))

                val threadflow = viewModel<threadFlow>()
                text = threadflow.countFlow.collectAsState(initial = 0).value
                Text(text = text.toString())
            }
        }

        fun textBitmap(str: String): Bitmap {
            val bitmap: Bitmap = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888)
            val canvas: Canvas = Canvas(bitmap)
            val paint: Paint = Paint()
            val paint1: Paint = Paint()
            paint.color = android.graphics.Color.parseColor(
                "#" + RandamList(
                    arrayOf(
                        "EF5350",
                        "EC407A",
                        "AB47BC",
                        "7E57C2",
                        "5C6BC0",
                        "42A5F5",
                        "29B6F6",
                        "26C6DA",
                        "26A69A",
                        "66BB6A",
                        "9CCC65",
                        "D4E157",
                        "FFEE58",
                        "FFCA28",
                        "FFA726",
                        "FF7043"
                    )
                )
            )
            Color(0xFFFF7043)
            paint1.color = android.graphics.Color.parseColor("#000000")
            paint1.textSize = ((512.0 * 0.85).toFloat())
            //  canvas.drawColor(android.graphics.Color.parseColor("#000000").toInt());
            canvas.drawCircle(512f, 512f, 512f * 0.75f, paint)
            try {
                canvas.drawText(str[0].uppercase() + str[1].lowercase(), 270f, 612f, paint1)
            } catch (e: Exception) {
                canvas.drawText(str[0].uppercase(), 270f, 612f, paint1)
            }
            return bitmap
        }

    }

