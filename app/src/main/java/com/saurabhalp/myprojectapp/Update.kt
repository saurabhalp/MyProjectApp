package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.type.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UpdateScreen(navController: NavController) {
    var updatesList = remember {
        mutableStateListOf<Update>()
    }
    //93042
    var loading by remember {
        mutableStateOf(true)
    }
    var update by remember {
        mutableStateOf(false)
    }
    var context  = LocalContext.current
    var messagedocRef =  Firebase.firestore.collection("messages")
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Updates", Modifier.padding(start = 20.dp), fontWeight = FontWeight.Bold
                    )
                }, colors =
                TopAppBarColors(
                    Color(0xfff1f9fe),
                    Color(0xfff1f9fe),
                    Color(0xfff1f9fe),
                    Color(0xff0d2c3f),
                    Color(0xfff1f9fe),
                )
            )
        }
    ) { it ->

        LaunchedEffect(Unit) {
            try {

                val documents = Firebase.firestore.collection("messages").get().await()
                for (document in documents){
                    val title = document.getString("title") ?: "Not Found"
                    val message = document.getString("message") ?: "Not Accessible"
                    val date = document.getString("date") ?: "Not Accessible"
                    updatesList.add(Update(title, message,date))
                }
                loading=false
            }catch (e:Exception){
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                loading= false
            }
        }
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color(0xfff1f9fe))) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(updatesList) {
                        UpdateCard(update = it, onDelete = { deleteUpdateFromFirestore("messages",it,updatesList) })
                    }


                }
                Button(onClick = { update=!update },Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Add Update")
                }


               if(update){
                   AddUpdate(updateList = updatesList,true)
               }

            }


            }


        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUpdate(updateList: SnapshotStateList<Update>,p:Boolean){
    var alert by remember {
        mutableStateOf(p)
    }
    var loading by remember {
        mutableStateOf(false)
    }
    var title by remember {
        mutableStateOf("")
    }
    var message by remember {
        mutableStateOf("")
    }
    var date by remember {
        mutableStateOf("")
    }
    var messagedocRef =  FirebaseFirestore.getInstance().collection("messages")
    var context = LocalContext.current



    if(alert){
        BasicAlertDialog(onDismissRequest = { alert=false }, Modifier.background(Color.White)) {
                Column(
                    Modifier
                        .padding(20.dp)
                        .background(
                            color = Color.White, shape = RoundedCornerShape(10.dp)
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    if(loading){
                        CircularProgressIndicator()
                    }
                    else {
                    OutlinedTextField(value = title, onValueChange = {
                        title = it
                    })
                        Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(value = message, onValueChange = { message = it })
                        Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = date, onValueChange = {
                        date = it
                    })
                        Spacer(modifier = Modifier.height(8.dp))
                    val ss = DateFormat.getLongDateFormat(context)

                    // on below line we are creating a variable for
                    // current date and time and calling a simple
                    // date format in it.
                    date = ss.format(
                        java.util.Date()
                    )
                    Button(onClick = {
                        loading=true
                        val messages = hashMapOf(
                            "title" to title,
                            "message" to message,
                            "date" to date
                        )
                        try {
                            messagedocRef.add(messages).addOnSuccessListener {
                                loading = false
                                alert = false
                                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                                updateList.add(Update(title,message,date))
                            }
                        } catch (e: Exception) {
                            loading = false
                            alert=false
                            Toast.makeText(context,  "Failed", Toast.LENGTH_SHORT).show()
                            Log.d("adding message", e.toString())
                        }
                    }) {
                        Text(text = "Submit")

                    }
                }
            }
        }
    }

}
@Composable
fun UpdateCard(update: Update, onDelete:()->Unit){
    var title by remember {
        mutableStateOf(update.title)
    }
    var message by remember {
        mutableStateOf(update.message)
    }
    var date by remember {
        mutableStateOf(update.date)
    }
    
    Card(Modifier.padding(8.dp)){
        Box (
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFC0E5F7))
                .padding(8.dp)) {
            IconButton(onClick = onDelete,
                Modifier
                    .align(Alignment.TopEnd)
                    .height(20.dp)) {
                Image(painter = painterResource(R.drawable.delete), contentDescription =null ,Modifier.padding(1.dp))
                
            }
            Column (Modifier.fillMaxWidth()){
                Text(
                    text = title, Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF0D2C3F)
                )
                Text(text = message,
                    Modifier.align(Alignment.CenterHorizontally),
                    color = Color(0xFF0D2C3F),)
                Text(text =date,
                    Modifier
                        .padding(4.dp)
                        .align(Alignment.End))

            }
        }
        
    }
    
}
fun deleteUpdateFromFirestore(
    collection: String,
    update: Update,
    updateList: SnapshotStateList<Update>
) {
    val db = Firebase.firestore
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = db.collection(collection)
                .whereEqualTo("title", update.title)
                .whereEqualTo("message", update.message)
                .get()
                .await()
            for (document in querySnapshot.documents) {
                db.collection(collection)
                    .document(document.id)
                    .delete()
                    .await()
            }
            withContext(Dispatchers.Main) {
                updateList.remove(update)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
            }
        }
    }
}


@Preview
@Composable
fun UpPre() {
    UpdateCard(update = Update("tITLE","SDHF AKKJHDFSJHG JHADSFKJA","dATE"),{})

}




