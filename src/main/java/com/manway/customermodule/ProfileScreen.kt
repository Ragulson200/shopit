package com.manway.customermodule


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.manway.customermodule.MainActivity2
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ProfileScreen(context:Activity, navigationControl:NavHostController)
{

    val pdb = Firebase.firestore.collection("Users Data")
    var map by remember {
        mutableStateOf(HashMap<String, Any>())
    }

    var name by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }

    var address by remember { mutableStateOf("") }

    var age by remember { mutableStateOf(0) }

    var gender by remember { mutableStateOf("Male") }

    var contactNumber by remember { mutableStateOf(0L) }


    val storageReference: StorageReference = Firebase.storage.reference


    // Firebase.firestore.collection("Users Data").document(email).set(customerInfo(name, age, gender, email, contactNumber, address, textBitmap(name), storageReference))

    var editText: @Composable (Modifier, String, String, (String) -> String) -> String =
        { modifier, intial, label, onResponse ->
            var text by remember { mutableStateOf("") }
            var textChangeStart by remember {
                mutableStateOf(false)
            }
            if (!textChangeStart) text = intial
            // Toast.makeText(context,intial,Toast.LENGTH_LONG).show()
            Column {
                TextField(value = text,
                    singleLine = true,
                    label = { Text(text = label, color = Color.Black) },
                    modifier = modifier
                        .border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10))
                        .fillMaxWidth(0.70f),
                    onValueChange = { text = it;textChangeStart = true },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Text(
                    text = onResponse(text),
                    fontSize = 10.sp,
                    modifier = Modifier.offset(x = 10.dp, y = 5.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
            text
        }

    var editTextNum: @Composable (Modifier, String, String, (String) -> String) -> String =
        { modifier, intial, label, onResponse ->
            var text by remember { mutableStateOf("") }
            var textChangeStart by remember {
                mutableStateOf(false)
            }
            if (!textChangeStart) text = intial
            Column {
                TextField(value = text,
                    singleLine = true,
                    label = { Text(text = label, color = Color.Black) },
                    modifier = modifier
                        .border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(10))
                        .fillMaxWidth(0.70f),
                    onValueChange = { text = it;textChangeStart = true },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Text(
                    text = onResponse(text),
                    fontSize = 10.sp,
                    modifier = Modifier.offset(x = 10.dp, y = 5.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
            text
        }

    var editTextAdr: @Composable (Modifier, String, String, (String) -> String) -> String =
        { modifier, intial, label, onResponse ->
            var text by remember { mutableStateOf("") }
            var textChangeStart by remember { mutableStateOf(false) }
            if (!textChangeStart) text = intial
            Column {
                TextField(value = text,
                    label = { Text(text = label, color = Color.Black) },
                    maxLines = 7,
                    modifier = modifier
                        .border(2.dp, Color(0xFFAF9D95), RoundedCornerShape(3))
                        .fillMaxWidth(0.70f),
                    onValueChange = { text = it;textChangeStart = true },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Text(text = onResponse(text), fontSize = 10.sp, modifier = Modifier.offset(x = 10.dp, y = 5.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
            text
        }




    Firebase.auth.currentUser?.email?.let {

        var genderSwapUnlock by remember {
            mutableStateOf(false)
        }
        var bitmap by remember {
            mutableStateOf<Bitmap?>(null)
        }


        email = it


        var blickBlock = false
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 25.sp,
                color = Color(0xFFAF9D95),
                modifier = Modifier.padding(15.dp)
            )

            pdb.document(it).get().addOnSuccessListener {
                name = it.data?.get(customerInfo.name).toString()
                age = it.data?.get(customerInfo.age).toString().toInt()
                if (!genderSwapUnlock) gender = it.data?.get(customerInfo.gender).toString()
                val l: Any = (it.data?.get(customerInfo.address) as ArrayList<*>)[0]
                address = (l as HashMap<*, *>)["address"].toString()
                val m = (it.data?.get("PhoneNumbers") as ArrayList<*>)[0]
                contactNumber = (m as HashMap<*, *>)["phoneNumber"].toString().toLong()
            }

            storageReference.child("customerInfo/$it.jpeg").getBytes(1024 * 1024)
                .addOnSuccessListener {
                    if (blickBlock || bitmap == null)
                    {
                        bitmap = DataBaseConverter.convertByteArrayToBitmap(it!!)
                    }
                    blickBlock = true
                }
//            val imageUri = remember { mutableStateOf<Uri?>(null) }
//
//            val launcher =
//                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//                    imageUri.value = uri
//                    Toast.makeText(context, imageUri.toString(), Toast.LENGTH_LONG).show()
//                }
//
//            val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.PickVisualMedia(),
//                onResult = { uri ->
//                    imageUri.value = uri
//
//                }
//            )

//            AndroidView(modifier =Modifier.size(60.dp), factory = {
//
//                ImageView(it).apply {
//                    setOnClickListener {
//                        MainActivity2().launchImagePicker()
//                        pickerState=MainActivity2.uri
//                        setImageURI(MainActivity2.uri)
//                        getBitmapFromImageView(this)?.let { it1 -> getBitmap(it1);setImageBitmap(it1) }
//                    }
//
//                    if(MainActivity2.uri!=null) setImageURI(MainActivity2.uri) else setImageResource(R.drawable.ic_launcher_foreground)
//                    scaleType=ImageView.ScaleType.FIT_XY
//
//                }
//            })

//            Image(modifier = Modifier
//                .size(100.dp)
//                .clickable {
////                    singlePhotoPickerLauncher.launch(
////                        PickVisualMediaRequest(
////                            ActivityResultContracts.PickVisualMedia.ImageOnly
////                        )
////                    )
//                }, painter = rememberAsyncImagePainter(model = bitmap), contentDescription = ""
//            )
            Spacer(modifier = Modifier.height(15.dp))

            Row {
                name = editText(Modifier.fillMaxWidth(.60f), name, "Name") {
                    val k = when
                    {
                        it.isEmpty() -> "Empty"
                        it.length < 3 -> "at least 3 letters"
                        else -> ""
                    }
                    if (k.isEmpty())
                    {
                        name = it
                    } else
                    {
                        name = "null"
                    }
                    k
                }
                Spacer(modifier = Modifier.width(10.dp))

                age = editTextNum(Modifier.fillMaxWidth(0.50f), age.toString(), "Age") {
                    val k = try
                    {
                        when
                        {
                            it.toInt() in 18..100 -> ""
                            it.toInt() < 18 -> "18 below"
                            else -> "100 above"
                        }
                    }
                    catch (e: Exception)
                    {
                        "Empty"
                    }
                    if (k.isEmpty())
                    {
                        age = it.toInt()
                    } else
                    {
                        age = 0
                    }
                    k

                }.toInt()
            }

            contactNumber = editTextNum(Modifier.fillMaxWidth(0.8f), contactNumber.toString(), "Contact Number") {
                val k = when
                {
                    customerInfo.phoneNumbe.checkPhoneNumber(it) -> ""
                    else -> "This not phone Number"
                }
                if (k.isEmpty())
                {
                    contactNumber = it.toLong()
                } else
                {
                    contactNumber = 0
                }
                k
            }.toLong()
            Text(text = gender)
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Image(painter = painterResource(id = R.drawable.male), contentDescription = "",
                    modifier = Modifier
                        .clickable { gender = "Male";genderSwapUnlock = true }
                        .fillMaxSize(0.23f))
                Image(painter = painterResource(id = R.drawable.female),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable { gender = "Female";genderSwapUnlock = true }
                        .fillMaxSize(0.23f))
                Image(painter = painterResource(id = R.drawable.others),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable { gender = "Others";genderSwapUnlock = true }
                        .fillMaxSize(0.23f))
            }
            Spacer(modifier = Modifier.height(25.dp))

            address = editTextAdr(
                Modifier
                    .fillMaxWidth(0.75f)
                    .height(200.dp), address, "Address"
            ) {
                val k = when
                {
                    it.isEmpty() -> "Empty"
                    else -> ""
                }
                if (k.isEmpty())
                {
                    address = it
                } else
                {
                    address = "null"
                }
                k
            }
            Button(onClick = {
                pdb.document(it).update(map)
                map = HashMap<String, Any>().apply {

                    this[customerInfo.name] = name
                    this[customerInfo.age] = age
                    this[customerInfo.address] = ArrayList<HashMap<String, String>>().apply {
                        this.add(HashMap<String, String>().apply {
                            this["address"] = address
                        })
                    }
                    this[customerInfo.gender] = gender
                    this[customerInfo.email] = email
                    this["PhoneNumbers"] = ArrayList<HashMap<String, Any>>().apply {
                        this.add(HashMap<String, Any>().apply {
                            this["phoneNumber"] = contactNumber
                        })
                    }
                }
            }) {
                Text(text = "Update")
            }
        }


    }


}

fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
    val drawable = imageView.drawable
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    return null
}


@Composable
fun PickImageFromGallery()
{

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28)
            {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else
            {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Pick Image")
        }
    }
}

