package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPdfScreen() {
    var filePath by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var subNumber by remember { mutableStateOf("Select Subject") }
    var expanded by remember { mutableStateOf(false) }
    var url by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var filechoosed by remember { mutableStateOf(false) }
    var isAlert by remember { mutableStateOf(false) }

    if(filePath==null) filechoosed = false
    else filechoosed = true
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        filePath = uri
    }

    Column (modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {

       TextButton(onClick = { isAlert = true}) {
           Text(text = "Add files", color = Color.Blue, fontWeight = FontWeight.Bold)

       }


if(isAlert) {
    BasicAlertDialog(onDismissRequest = { isAlert = false }) {
        Column(
            Modifier
                .padding(20.dp)
                .background(
                    color = Color.White, shape = RoundedCornerShape(10.dp)
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Title") })
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    Modifier.border(2.dp, Color.Gray)
                ) {
                    Text(
                        text = subNumber.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (1..5).forEach { number ->
                            DropdownMenuItem(
                                onClick = {
                                    subNumber = number.toString()
                                    expanded = false
                                },
                                text = { Text(number.toString()) }
                            )
                        }
                    }
                }
                var path by remember {
                    mutableStateOf("Select File")
                }
                if (!filechoosed) {
                    Text(text = path,
                        Modifier
                            .padding(20.dp)
                            .clickable {
                                pdfPickerLauncher.launch("application/pdf")
                            })
                } else {
                    path = filePath?.lastPathSegment.toString()
                }
                Button(onClick = {
                    if(filePath!=null && name!= null) {
                        loading = true
                        filePath?.let {
                            uploadPdf(it) { Arli ->
                                url = Arli
                                loading = false
                                addFilesViewModel(
                                    Files(name, url, subNumber),
                                    context = context
                                )
                                name = ""
                                filePath = null
                                subNumber = "Select Subject"
                                isAlert = false
                            }

                        }
                    }
                }) {
                    Text(text = "Upload PDF")
                }


            }
        }


    }
}

//            OutlinedTextField(value = name,
//                onValueChange = { name = it },
//                label = { Text(text = "Title") })
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Box(modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.CenterHorizontally)
//                .padding(start = 20.dp, end = 20.dp)) {
//                Text(
//                    text = subNumber.toString(),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { expanded = true }
//                        .background(MaterialTheme.colorScheme.surface)
//                        ,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//                DropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    (1..5).forEach { number ->
//                        DropdownMenuItem(
//                            onClick = {
//                                subNumber = number.toString()
//                                expanded = false
//                            },
//                            text = { Text(number.toString()) }
//                        )
//
//                    }
//                }
//            }
//        }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            if(!filechoosed) {
//                Button(onClick = { pdfPickerLauncher.launch("application/pdf")
//                }) {
//                    Text(text = "Choose PDF")
//                }
//            }else{
//                Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
////                    Text(text = "heel/dc",Modifier.weight(3f).padding(start = 20.dp), overflow = TextOverflow.Clip, maxLines = 1
////                    )
//                    filePath?.path?.let {
//                        Text(
//                            text = it,
//                            Modifier
//                                .weight(3f)
//                                .padding(start = 20.dp),
//                            overflow = TextOverflow.Clip
//                        )
//                        IconButton(onClick = { pdfPickerLauncher.launch("application/pdf") }) {
//                            Icon(
//                                painter = painterResource(R.drawable.edbutton),
//                                contentDescription = null,
//                                Modifier
//                                    .padding(10.dp)
//                                    .height(24.dp)
//                            )
//                        }
//                    }
                }
//            }
//
//
//
//            Spacer(
//                modifier =
//                Modifier.height(16.dp)
//            )
//
//            Button(onClick = {
//                loading= true
//                filePath?.let {
//                    uploadPdf(it) { Arli ->
//                        url = Arli
//                        loading = false
//                        addFilesViewModel(Files(name, url, subNumber), context = context)
//                        name = ""
//                        filePath = null
//                        subNumber = "Select Subject"
//                    }
//                }
//            }) {
//                Text(text = "Upload PDF")
//            }
        }



private fun uploadPdf(filePath: Uri, onUrlReady: (String) -> Unit){
    val storage = FirebaseStorage.getInstance()
    val storageReference = storage.reference

    val ref = storageReference.child("pdfs/" + UUID.randomUUID().toString())
    ref.putFile(filePath)
        .addOnSuccessListener { taskSnapshot ->
            ref.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                Log.d("Firebase", "Download URL: $downloadUrl")
                onUrlReady(downloadUrl)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firebase", "Failed to upload PDF", exception)
        }
}
@SuppressLint("SuspiciousIndentation")
fun addFilesViewModel(data : Files, context: Context) {
    val db = Firebase.firestore
    val notes = hashMapOf(
        "name" to data.title,
        "url" to data.url
    )
        db.collection(data.subject).document("fdsa").collection("pdf1").add(notes)
            .addOnSuccessListener {
                Log.d("Upload","uploaded with id ${it.id}")
                Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("upload","error in uploading",e)
            }

}


data class Files(var title:String,var url : String,var subject:String)
@Preview(showBackground = true)
@Composable
fun UploadPdfScreenPreview() {
    UploadPdfScreen()
}
