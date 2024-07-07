package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.api.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.type.Date
import com.saurabhalp.myprojectapp.ui.checkAndRequestNotificationPermission
import com.saurabhalp.myprojectapp.ui.showNotification
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
    var alert by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val messagedocRef = FirebaseFirestore.getInstance().collection("messages")
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateStartPadding(LayoutDirection.Ltr),
                end = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateEndPadding(LayoutDirection.Ltr),
            )
    ) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton =  {FloatingActionButton(
                onClick = {
                          alert = true
                }
                          ,
                Modifier
                    .padding(bottom = 100.dp, end = 30.dp)
                    .height(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edbutton),
                    contentDescription = "add",
                    Modifier.padding(4.dp)
                ) }
            },
            topBar = { Topbar(title = "Updates")}
        ) { it ->
            
            LaunchedEffect(Unit) {
                try {

                    val documents = Firebase.firestore.collection("messages").get().await()
                    for (document in documents) {
                        val title = document.getString("title") ?: "Not Found"
                        val message = document.getString("message") ?: "Not Accessible"
                        val date = document.getString("date") ?: "Not Accessible"
                        updatesList.add(Update(title, message, date))
                    }
                    loading = false
                } catch (e: Exception) {
                    Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                    loading = false
                }
            }
          Box(
              Modifier
                  .padding(it)
                  .fillMaxSize()
                  .background(Color(0xfff1f9fe))) {
              Column(
                  Modifier
                      .fillMaxSize()
                      .padding(8.dp)
              ) {
                  if (loading) {
                      CircularProgressIndicator(
                          Modifier
                              .align(Alignment.CenterHorizontally)
                              .padding(top = 50.dp)
                      )
                  } else {
                      LazyColumn(
                          Modifier
                              .fillMaxWidth()
                              .padding(bottom = 70.dp)) {
                          updatesList.asReversed()
                          items(updatesList) {
                              UpdateCard(
                                  update = it,
                                  onDelete = {
                                      deleteUpdateFromFirestore(
                                          "messages",
                                          it,
                                          updatesList
                                      )
                                  })
                          }


                      }


                      if (alert) {
                          BasicAlertDialog(onDismissRequest = { alert = false }, Modifier.background(Color.White)) {
                              Column(
                                  Modifier
                                      .padding(20.dp)
                                      .background(
                                          color = Color.White,
                                          shape = RoundedCornerShape(10.dp)
                                      )
                                      .padding(20.dp),
                                  horizontalAlignment = Alignment.CenterHorizontally
                              ) {
                                  if (loading) {
                                      CircularProgressIndicator()
                                  } else {
                                      OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                                      Spacer(modifier = Modifier.height(8.dp))

                                      OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Message") })
                                      Spacer(modifier = Modifier.height(8.dp))

                                      OutlinedTextField(value = date, onValueChange = { date = it })
                                      Spacer(modifier = Modifier.height(8.dp))

                                      val ss = DateFormat.getLongDateFormat(context)
                                      date = ss.format(java.util.Date())

                                      Button(onClick = {
                                          loading = true
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
                                                  updatesList.add(Update(title, message, date))
                                                  // Check and request notification permission before sending notification
                                                  try {
                                                      showNotification(context, title, message)
                                                  }catch (e:Exception){
                                                      Log.d("notification se hai",e.toString())

                                                  }
                                              }
                                          } catch (e: Exception) {
                                              loading = false
                                              alert = false
                                              Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                              Log.d("adding message", e.toString())
                                          }

                                      }) {
                                          Text("Submit")
                                      }
                                  }
                              }
                          }
                      }

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
                    color = colorResource(id = R.color.textColor)
                )
                Text(
                    text = message,
                    Modifier.align(Alignment.CenterHorizontally),
                    color = colorResource(id = R.color.textColor),
                )
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
@Composable
fun UpdateScreen2(navController: NavController) {
    Scaffold(floatingActionButtonPosition = FabPosition.End,
        floatingActionButton =  {FloatingActionButton(
            onClick = { /*TODO*/ },
            Modifier
                .padding(bottom = 100.dp, end = 30.dp)
                .height(50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.edbutton),
                contentDescription = "add",
                Modifier.padding(4.dp)
            ) }
}
 )
        {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(80.dp)
            ) {

                Text(text = "erhfgjv", Modifier)
                Spacer(modifier = Modifier.height(80.dp))
                Text(text = "fjhbjd")
            }
        }
    }
}

@Preview
@Composable
fun UpPre() {
    UpdateScreen(navController = rememberNavController())

}




