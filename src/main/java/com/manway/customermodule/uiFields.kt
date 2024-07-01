package com.manway.customermodule

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

public fun isValid(value: String):Boolean{
    return !(value.equals("")||value.equals(null)||value.equals("null"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun textField1(
    value: String? ="null",
    modifier: Modifier = Modifier, label:String,
    requiredText:(it:String)->String
    ,TrailingIcon:()->Unit={},
    onTextChange:(it:String)->Unit={}): String? {
    var field by remember {
        mutableStateOf(if(value.equals("null")) "" else value)
    }

    Column(
        modifier = modifier
            .scale(0.85f, 0.80f)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.Transparent)
            .border(
                border = BorderStroke(1.dp, Color(0xFFAF9D95)),
                shape = RoundedCornerShape(10.dp)
            )
            .offset(x = 20.dp)

    ) {


        field?.let {
            TextField(
                value = it,
                textStyle = TextStyle(fontSize =20.sp, lineHeight = 100.sp),
                label={ Text(
                    text = label,
                    color = Color(0xff765151),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .offset(y = -10.dp)
                )},
                trailingIcon = {TrailingIcon()},
                onValueChange = {
                    field = it
                    onTextChange(field!!)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .requiredWidth(width = 210.dp)
                    .height(75.dp)
                    .offset(y = 10.dp)

            )
        }

        Text(text = requiredText(field!!), color = Color(0xffe92199), style = TextStyle(fontSize = 14.sp), modifier = Modifier
            .fillMaxWidth(0.90f)
            .height(20.dp)
            .offset(y = -2.dp, x = 15.dp))

    }


    if(requiredText(field!!).equals("")) return field else return "null"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun textField1num(
    value: String? ="null",
    modifier: Modifier = Modifier, label:String,
    requiredText:(it:String)->String
    ,TrailingIcon:()->Unit={},
    onTextChange:(it:String)->Unit={}): String {
    var field by remember {
        mutableStateOf(if(value.equals("null")) "" else value)
    }

    //0xffe92199
    Column(
        modifier = modifier
            .scale(0.85f, 0.75f)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.Transparent)
            .border(
                border = BorderStroke(1.dp, Color(0xFFAF9D95)),
                shape = RoundedCornerShape(10.dp)
            )
            .offset(x = 20.dp)

    ) {


        field?.let {
            TextField(
                value = it,
                textStyle = TextStyle(fontSize =20.sp),
                label={ Text(
                    text = label,
                    color = Color(0xff765151),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .offset(y = -10.dp)
                )},
                trailingIcon = {TrailingIcon()},
                keyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Number)
           ,onValueChange = {
                    field = it
                    onTextChange(field!!)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .offset(y = 10.dp)

            )
        }

        Text(text = requiredText(field!!), color = Color(0xffe92199), style = TextStyle(fontSize = 14.sp), modifier = Modifier
            .width(width = 125.dp)
            .height(30.dp)
            .offset(y = 5.dp, x = 15.dp))

    }


    if(requiredText(field!!).equals("")) return field as String; else return "null"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun textField2(
    value: String? ="null",
    modifier: Modifier = Modifier, label:String,
    requiredText:(it:String)->String
    ,TrailingIcon:()->Unit={},
    onTextChange:(it:String)->Unit): String? {
    var field by remember {
        mutableStateOf(if(value.equals("null")) "" else value)
    }

    Column(
        modifier = modifier
            .scale(0.85f, 0.75f)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.Transparent)
            .border(
                border = BorderStroke(1.dp, Color(0xFFAF9D95)),
                shape = RoundedCornerShape(10.dp)
            )
            .offset(x = 20.dp)


    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
            , verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = label,
                color = Color(0xff765151),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .requiredHeight(height = 20.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(10.dp))

            field?.let {
                TextField(
                    value = it,
                    trailingIcon = {TrailingIcon()},
                    textStyle = TextStyle(textAlign = TextAlign.Justify),
                    onValueChange = {
                        field = it
                        onTextChange(field!!)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent
                        , focusedIndicatorColor = Color.Transparent
                        , unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .requiredWidth(width = 210.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }

        }
        Text(text = requiredText(field!!), color = Color(0xffe92199), style = TextStyle(fontSize = 14.sp), modifier = Modifier
            .width(width = 125.dp)
            .height(20.dp)
            .offset(y = 5.dp))



    }
    if(requiredText(field!!).equals("")) return field; else return "null";
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun textField3(
    modifier: Modifier=Modifier,
    editMode: Boolean=false,
    value: String ="",
    label: String="",
    trailingIcon:  @Composable() (() -> Unit)={},
    requiredText: (it: String) -> String={""},
    onValueChangeListener:(field:String)->Unit):String?{
    var field by remember {
        mutableStateOf(value)
    }

    Column(modifier) {
            TextField(value = field,
            singleLine = true,
                label = {
                    Text(text = label)
                },
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 17.sp),
                readOnly = !editMode,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier,
                onValueChange = { field = it;onValueChangeListener(it) },
                trailingIcon = {
                    if (editMode) {
                        Row {
                            trailingIcon()
                            Image(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = ""
                            )
                        }
                    }
                })
        if(editMode) Text(text = requiredText(field), color = Color.Red )
    }
    if(requiredText(field!!).equals("")) return field; else return "null";
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun textFieldAddress(modifier: Modifier=Modifier,label:String,editMode: Boolean=false,TrailingIcon:()->Unit={},requiredText: (it:String) -> String,onTextChange: (it:String) -> Unit): String? {
    var field by remember {
        mutableStateOf("")
    }
    TextField(value = field,
        readOnly=!editMode,
        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent), onValueChange ={
        field = it
        onTextChange(field)
         }, modifier = modifier
            .fillMaxWidth(0.87f)
            .height(250.dp)
            .border(
                1.dp, Color(0xFFAF9D95), RoundedCornerShape(10)
            )
        , label = { Text(text =label ) }
        , supportingText = {
            Text(text = requiredText(field), color = Color(0xffe92199),modifier=modifier.offset(x=10.dp,y=-10.dp))
        }
        , trailingIcon = {TrailingIcon()}
    )

    if(requiredText(field).equals("")) return field; else return "null";

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun textFieldAddress1(modifier: Modifier=Modifier,value: String="",label:String,TrailingIcon:()->Unit={},editMode: Boolean=false,requiredText: (it:String) -> String,onTextChange: (it:String) -> Unit): String? {
    var field by remember {
        mutableStateOf(value)
    }
    TextField(value = field, readOnly =!editMode,colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent), onValueChange ={
        field = it
        onTextChange(field)
    }, modifier = modifier
        .fillMaxWidth(0.87f)
        , label = { Text(text =label ) }
        , supportingText = {
            Text(text = requiredText(field), color = Color(0xffe92199),modifier=Modifier.offset(x=10.dp,y=-10.dp))
        }
        , textStyle = TextStyle()
        , trailingIcon = { Spacer(modifier = Modifier
            .fillMaxWidth(0.05f)
            .fillMaxHeight())}
    )

    if(requiredText(field).equals("")) return field; else return "null";
}


@Composable
fun title(modifier: Modifier = Modifier,title:String) {
    Text(
        text = title,
        color = Color(0xFFAF9D95),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(height = 35.dp)
            .wrapContentHeight(align = Alignment.CenterVertically))
}

@Composable
public fun button1(modifier: Modifier=Modifier,text:String,onClick:()->Unit){
    Button(onClick = { onClick() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAF9D95), contentColor = Color.Black) , modifier = Modifier.width(120.dp)) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}

@Composable
public fun button2(modifier: Modifier=Modifier,text:String,onClick:()->Unit){
    Button(onClick = { onClick }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black), modifier = Modifier.width(120.dp) ) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable public fun dropDownText(
    valu: String?="null",
    label: String,
    list: ArrayList<String>,
    requiredText: () -> String): String? {
    var isExpand by remember {
        mutableStateOf(false)
    }


    var value by remember {
        mutableStateOf(if(valu.equals("null"))"" else valu)
    }

    ExposedDropdownMenuBox(
        expanded = isExpand,
        onExpandedChange = { isExpand = it }) {
        value?.let {
            TextField(modifier = Modifier.fillMaxWidth().scale(0.85f).border(1.dp, Color(0xFFAF9D95), RoundedCornerShape(10)).menuAnchor().clickable { isExpand = !isExpand },value = it,label={ Text(text = label)}, onValueChange = {}, colors = ExposedDropdownMenuDefaults.textFieldColors(unfocusedContainerColor = Color.Transparent,focusedContainerColor=Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpand) }, readOnly = true)
        }
        ExposedDropdownMenu(
            expanded = isExpand,
            onDismissRequest = { isExpand = false }) {
            //items->DropdownMenuItems
            for(s in list) {
                DropdownMenuItem(
                    text = { Text(text = s) },
                    onClick = {
                        isExpand = false
                        value = s
                    })
            }
        }

    }
    return value;
}


@SuppressLint("SuspiciousIndentation")
@Composable
public fun image1Pickable(context: Context,modifier: Modifier=Modifier,onImagePick:(bitmap:Bitmap,uri:Uri)->Unit){

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context= LocalContext.current
    var bitmap= remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher= rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        if (it != null) {
            bitmap.value=android.graphics.BitmapFactory.decodeFile(it.path)
            it.path?.let { it1 -> Log.e("test", it1) }
        }
        imageUri=it
        imageUri?.let {
            if(Build.VERSION.SDK_INT>28){
                bitmap.value= MediaStore.Images.Media.getBitmap(context.contentResolver,it)
            }
        }
    }


    ConstraintLayout(modifier = modifier.width(100.dp).height(100.dp).clickable { launcher.launch("image/*") }) {
            imageUri?.path?.let {  }
            val (image1, image2) = createRefs()

            bitmap.value?.let {
                imageUri?.let { it1 -> onImagePick(it, it1) }
                    Image(bitmap = it.asImageBitmap(),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                            .constrainAs(image1) {
                                top.linkTo(parent.top)
                                absoluteRight.linkTo(parent.absoluteRight)
                                absoluteLeft.linkTo(parent.absoluteLeft)
                                bottom.linkTo(image2.bottom)
                            }
                            .clickable {
                                 launcher.launch("image/*")
                            }
                    )
                }

            }
    }


