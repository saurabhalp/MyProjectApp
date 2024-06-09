package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.saurabhalp.myprojectapp.data.DataSource
import com.saurabhalp.myprojectapp.ItemRepository
import kotlinx.coroutines.tasks.await
import com.saurabhalp.myprojectapp.Subject


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
        subjectList(affirmationList = DataSource().loadSubjects(), navController, viewModel = MainViewModel())
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
fun subjectList(affirmationList : List<Subject>,navController: NavController,viewModel: MainViewModel) {

    Scaffold(
        topBar= {
            TopAppBar(title = { Text("Subject List") })
        },
    )
    { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(8.dp)) {
            LazyColumn {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesList(id: String, navController: NavController) {
    var db = Firebase.firestore
    var p:String
    var loading by remember { mutableStateOf(true) }
    var context = LocalContext.current
    var pdfItems = remember { mutableStateListOf<NotesPdf>() }
    LaunchedEffect(Unit) {
        try {
            if(id.toInt()!=5){
                p = "pdfs"

            }
            else{
                p=id
            }
            val documents = db.collection(p).document("fdsa").collection("pdf1").get().await()
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
            TopAppBar(title = { Text("Units") })
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize().padding(it)
                .padding(8.dp)
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(pdfItems) { itt ->
                        NotesCard(
                            PdfItem(itt.name, itt.url),
                        )
                    }
                }

                TextButton(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login")
                    navController.popBackStack("home2", inclusive = true)
                }

                ) { Text(text = "Logout") }
            }
        }
    }
}


@Composable
fun SubjectCard(subject: Subject,onClick:()->Unit,navController: NavController ){
    Card (modifier = Modifier
        .fillMaxWidth().height(100.dp)
        .padding(8.dp)
        .clickable(onClick = { onClick() }

        )){ Box (Modifier.fillMaxSize()){

      Row (modifier = Modifier.fillMaxSize().align(Alignment.Center) ){
                Text(
                    text = LocalContext.current.getString(subject.nameId),
                    Modifier.weight(2f)
                        .align(Alignment.CenterVertically)
                        .padding(start = 20.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold

                )
                TextButton(onClick={
                    navController.navigate("App2/${subject.id}")
                },Modifier.weight(1f).align(Alignment.CenterVertically)
                   ) {
                    Text( text = "Open",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }

            }
        }
    }
}
@Composable
fun NotesCard(subject:PdfItem){
    val context = LocalContext.current
    Card (modifier = Modifier
        .fillMaxWidth().height(100.dp)
        .padding(8.dp)
    ){
            Box(Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxSize().align(Alignment.Center),
                ) {
                    Text(
                        text = subject.name,
                        Modifier.weight(2f)
                            .align(Alignment.CenterVertically)
                            .padding(start = 20.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    ClickableText(
                        text = AnnotatedString("Open PDF"),
                        Modifier.weight(1f).align(Alignment.CenterVertically),
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(Uri.parse(subject.url), "application/pdf")
                                flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                            }
                            context.startActivity(intent)
                        }
                    )


                }
            }
        }
}

@Preview
@Composable
fun notePreview(){
    NotesCard(PdfItem("Name","url"))
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PdfListScreen(navController: NavHostController) {
//    val pdfItems = remember { mutableStateListOf<NotesPdf>() }
//    val db = Firebase.firestore
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//
//        try {
//            val documents = db.collection("pdfs").document("fdsa").collection("pdf1").get().await()
//            for (document in documents) {
//                val name = document.getString("name") ?: "Not Found"
//                val url = document.getString("url") ?: "Url Not Accessible"
//                pdfItems.add(NotesPdf(name, url))
//            }
//        } catch (e: Exception) {
//            // Handle exceptions
//            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("PDF List") })
//        }
//    ) {contentPadding->
//            Column(modifier = Modifier
//                .padding(contentPadding)
//                .padding(16.dp)) {
//                PdfItemView(NotesPdf("Check", "No Url"))
//                pdfItems.forEach { pdf ->
//                    PdfItemView(pdf)
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//
//                TextButton(onClick = { navController.navigate("login") })
//
//                {
//                    Text(
//                        text = "GO Back"
//                    )
//                }
//
//                TextButton(onClick = {
//                    try{
//                    auth.signOut()
//                    }
//                    catch (e:Exception){
//                        Toast.makeText(context,"${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }) {
//                    Text(
//                        text = "Logout",
//                        color = Color.Blue,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//}

@Composable
fun PdfItemView(pdf: NotesPdf) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =Arrangement.Center,
        modifier = Modifier.padding(16.dp)){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = pdf.name, style = MaterialTheme.typography.titleLarge, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ClickableText(
                text = AnnotatedString("Open PDF"),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(Uri.parse(pdf.url), "application/pdf")
                        flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
    }
}




