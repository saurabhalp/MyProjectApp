package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PdfListScreen(navController: NavHostController) {
    val pdfItems = remember { mutableStateListOf<NotesPdf>() }
    val db = Firebase.firestore
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val documents = db.collection("pdfs").get().await()
            for (document in documents) {
                val name = document.getString("name") ?: "Not Found"
                val url = document.getString("url") ?: "Url Not Accessible"
                pdfItems.add(NotesPdf(name, url))
            }
        } catch (e: Exception) {
            // Handle exceptions
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("PDF List") })
        }
    ) {contentPadding->
            Column(modifier = Modifier.padding(contentPadding).padding(16.dp)) {
                PdfItemView(NotesPdf("Check","No Url"))
                pdfItems.forEach { pdf ->
                    PdfItemView(pdf)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            TextButton(onClick = { navController.navigate("login") })

            {
                Text(
                    text = "GO Back"
                )
            }
        }
    }


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


@Preview
@Composable
fun previewer(){
    PdfListScreen( rememberNavController() )
}

