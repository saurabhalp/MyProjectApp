package com.saurabhalp.myprojectapp

import android.util.Log
import android.view.RoundedCorner
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(){
    val context = LocalContext.current
    var email = remember { mutableStateOf("") }
    var name = remember { mutableStateOf("") }
    var db = FirebaseAuth.getInstance().currentUser
    if(db!=null){
    email.value = db.email.toString()
    }
    else{
        name.value = "Error Getting Email"
    }
        var docRef = db?.let { FirebaseFirestore.getInstance().collection("users").document(it.uid) }
    if (docRef != null) {
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name.value = document.getString("name") ?: "User3"
                } else {
                    Toast.makeText(context, "error in fetching name", Toast.LENGTH_SHORT).show()
                    name.value = "User1"
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FetchUsername", "Error getting document", exception)
                name.value = "UserName"
            }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        }
    ) {

        Column(Modifier.fillMaxSize().padding(it)) {
            Row(Modifier.fillMaxWidth().padding(8.dp).height(100.dp)) {
                Box(Modifier.padding(8.dp).weight(1f)) {
                    Image(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = "Profile",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.height(100.dp)
                            .padding(8.dp).clip(RoundedCornerShape(50))
                    )
                }

                Column(Modifier.weight(2f).align(Alignment.CenterVertically)) {
                    Text(
                        name.value,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    Text(
                        text = email.value,
                        color = Color.White
                    )
                }


            }

            TextButton(onClick = {
                FirebaseAuth.getInstance().signOut()
            }, Modifier.align(Alignment.CenterHorizontally)) {
                Text("Logout")
            }


        }
    }
}



@Preview
@Composable
fun ScreenPreview(){
    ProfileScreen()
}