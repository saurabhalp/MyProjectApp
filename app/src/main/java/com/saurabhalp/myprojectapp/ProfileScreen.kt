package com.saurabhalp.myprojectapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    //todo : add option to update profile picture through firebase
    val context = LocalContext.current
    var email = remember { mutableStateOf("") }
    var name = remember { mutableStateOf("") }
    var db = FirebaseAuth.getInstance().currentUser
    var userType by remember { mutableStateOf("") }
    var emailVerified by remember { mutableStateOf(true) }
    if (db != null) {
        email.value = db.email.toString()
        emailVerified = db.isEmailVerified
    } else {
        name.value = "Error Getting Email"
    }
    var docRef = db?.let { FirebaseFirestore.getInstance().collection("users").document(it.uid) }
    docRef?.get()?.addOnSuccessListener { document ->
        if (document != null) {
            name.value = document.getString("name") ?: "User3"
            userType = document.getString("userType")?: "2"
        } else {
            Toast.makeText(context, "error in fetching name", Toast.LENGTH_SHORT).show()
            name.value = "User1"
        }
    }?.addOnFailureListener { exception ->
        Log.w("FetchUsername", "Error getting document", exception)
        name.value = "UserName"
    }
    Scaffold(
        Modifier.background(Color(0xffe3f1fb)),
        topBar = {
            TopAppBar(title = { Text("Profile",Modifier.padding(start = 20.dp)
                , fontWeight = FontWeight.Bold)}, colors =
            TopAppBarColors(Color(0xfff1f9fe),Color(0xfff1f9fe),Color(0xfff1f9fe),Color(0xff0d2c3f),Color(0xfff1f9fe),)
            )
        }
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(it).background(Color(0xfff1f9fe)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(100.dp)
            ) {
                Box(
                    Modifier
                        .padding(8.dp)
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = "Profile",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .height(100.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(50))
                    )
                }
                Column(
                    Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        name.value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color(0xff0d2c3f)
                    )

                    Text(
                        text = email.value,
                        color = Color(0xff0d2c3f)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            if (db != null) {
                db.reload()
                if (!db.isEmailVerified) {
                    Button(
                        onClick = { 
                            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                    Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT).show()
                                }
                        }
                    ) {
                        Text(text = "Verify Email",color = Color(0xff0d2c3f))

                    }
                }
                if (db.email.toString() == "saurabhk.nitp@gmail.com" || userType == "1" || db.isEmailVerified)
                    UploadPdfScreen()
                }
                Spacer(modifier = Modifier.height(32.dp))

                TextButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.popBackStack("profile", inclusive = true)
                        navController.navigate("login")
                    },
                ) {
                    Text(text = "Logout", color = Color(0xff0d2c3f))
                }
            }
        }
    }

@Preview
@Composable
fun ScreenPreview(){
    ProfileScreen(rememberNavController())
}