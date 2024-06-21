package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.saurabhalp.myprojectapp.data.DataSource
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun App(navController: NavController) {
    val layoutDirection = LocalLayoutDirection.current
    Surface (modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(
            start = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateStartPadding(layoutDirection),
            end = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateEndPadding(layoutDirection),
        )){
        SubjectList(affirmationList = DataSource().loadSubjects(), navController)
    }
}
@Composable
fun App2(navController: NavController,id:String) {
    val layoutDirection = LocalLayoutDirection.current
    Surface (modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(
            start = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateStartPadding(layoutDirection),
            end = WindowInsets.safeDrawing
                .asPaddingValues()
                .calculateEndPadding(layoutDirection),
        )){
        NotesList(id,navController)

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectList(affirmationList : List<Subject>, navController: NavController) {

    Scaffold(
        Modifier.background(Color(0xfff1f9fe)),
        topBar= {
            TopAppBar(title = { Text("Subject List", fontWeight = FontWeight.Bold) }, colors =
                    TopAppBarColors(Color(0xfff1f9fe),Color(0xfff1f9fe),Color(0xfff1f9fe),Color(0xff0d2c3f),Color(0xfff1f9fe)))
        },
    )
    { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(Color(0xfff1f9fe))) {
            LazyColumn(Modifier.padding(10.dp)) {
                items(affirmationList) { affirmation ->
                    var id = affirmation.id
                    SubjectCard(
                        subject = affirmation,
                        onClick = {},
                      navController)
                }
            }
        }
    }
}
    fun deleteNoteFromFirestore(
        collection: String,
        note: NotesPdf,
        pdfItems: SnapshotStateList<NotesPdf>
    ) {
        val db = Firebase.firestore
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = db.collection(collection)
                    .document("fdsa")
                    .collection("pdf1")
                    .whereEqualTo("name", note.name)
                    .whereEqualTo("url", note.url)
                    .get()
                    .await()
                for (document in querySnapshot.documents) {
                    db.collection(collection)
                        .document("fdsa")
                        .collection("pdf1")
                        .document(document.id)
                        .delete()
                        .await()
                }
                withContext(Dispatchers.Main) {
                    pdfItems.remove(note)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesList(id: String, navController: NavController) {
    var db = Firebase.firestore
    var loading by remember { mutableStateOf(true) }
    var context = LocalContext.current
    var pdfItems = remember { mutableStateListOf<NotesPdf>() }
    LaunchedEffect(Unit) {
        try {
            val documents = db.collection(id).document("fdsa").collection("pdf1").get().await()
            for (document in documents) {
                val name = document.getString("name") ?: "Not Found"
                val url = document.getString("url") ?: "Url Not Accessible"
                pdfItems.add(NotesPdf(name, url))
            }
            loading = false
        } catch (e: Exception) {
            // Handle exceptions
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Units",Modifier.padding(start = 20.dp)
            , fontWeight = FontWeight.Bold)}, colors =
            TopAppBarColors(Color(0xfff1f9fe),Color(0xfff1f9fe),Color(0xfff1f9fe),Color(0xff0d2c3f),Color(0xfff1f9fe),))
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xfff1f9fe))
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn (Modifier.padding(10.dp)){
                    items(pdfItems) { itt ->
                        NotesCard(
                            PdfItem(itt.name, itt.url), onDelete = {deleteNoteFromFirestore(id,itt,pdfItems)}
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun SubjectCard(subject: Subject,onClick:()->Unit,navController: NavController ){
    Card (modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(8.dp)
        .clickable(onClick = { onClick() }

        )){ Box (Modifier.fillMaxSize().background(Color(0xFFC0E5F7))){

      Row (modifier = Modifier
          .fillMaxSize()
          .align(Alignment.Center) ){
                Text(
                    text = LocalContext.current.getString(subject.nameId),
                    Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                        .padding(start = 20.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D2C3F)

                )
                TextButton(onClick={
                    navController.navigate("App2/${subject.id}")
                },
                    Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                   ) {
                    Text( text = "Open",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color(0xFF0D2C3F)
                    )
                }

            }
        }
    }
}
@Composable
fun NotesCard(subject:PdfItem,onDelete:()->Unit){
    val context = LocalContext.current
    Card (modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(8.dp)
    ){
            Box(Modifier.fillMaxSize().background(Color(0xFFC0E5F7))) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                ) {
                    Text(
                        text = subject.name,
                        Modifier
                            .weight(2f)
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D2C3F)
                    )
                    ClickableText(
                        text = AnnotatedString("Open PDF"),

                        Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(Uri.parse(subject.url), "application/pdf")
                                flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                            }
                            context.startActivity(intent)
                        }
                    )
                    if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true || FirebaseAuth.getInstance().currentUser?.email == "saurabhk.nitp@gmail.com") {
                        Button(onClick = onDelete,
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(10.dp)) {
                            Text(text = "Delete")
                        }

                    }
                }


                }
            }
        }


@SuppressLint("ResourceType")
@Preview
@Composable
fun notePreview(){
    SubjectCard(subject = Subject(2,""), onClick = { /*TODO*/ }, navController = rememberNavController() )
}


