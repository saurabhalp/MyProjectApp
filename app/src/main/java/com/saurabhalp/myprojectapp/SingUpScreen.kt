package com.saurabhalp.myprojectapp

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

import com.saurabhalp.myprojectapp.ui.theme.MyProjectAppTheme
import kotlinx.coroutines.delay


@Composable
fun Screen2(navController: NavHostController) {
    var name = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var email = remember { mutableStateOf("") }
    var passwordVisible = remember { mutableStateOf(false) }
    var enable by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var context = LocalContext.current
    val db =Firebase.firestore
    Box(Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription ="Bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxHeight()
        )
           Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
           )
           {

               OutlinedTextField(
                   value = name.value,
                   onValueChange = {name.value=it},
                   label = ({ Text("Name") })
               )
               OutlinedTextField(
                value = email.value,
                onValueChange = {email.value=it},
                label = ({ Text("Email") })
               )
               OutlinedTextField(
                   value = password.value,
                   onValueChange = {password.value=it},
                   label = ({ Text("Password") }),
                   visualTransformation = if(passwordVisible.value) VisualTransformation.None
                   else PasswordVisualTransformation(),
                   trailingIcon = {
                       painterResource(id = R.drawable.icon1)


                       IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                           Icon(painter = painterResource(R.drawable.icon1), contentDescription = null,
                               modifier = Modifier.padding(4.dp))
                       }
                   }
               )
               Spacer(Modifier.height(16.dp))

               if(loading) {
                   CircularProgressIndicator()
               }
                else{

               Button(onClick = {
                   loading=true
                   auth = Firebase.auth
                   try {
                       auth.createUserWithEmailAndPassword(email.value, password.value)
                           .addOnCompleteListener { task ->
                               if (task.isSuccessful) {
                                   val user = auth.currentUser
                                   user?.let {
                                       val userMap = hashMapOf(
                                           "name" to name.value,
                                           "email" to email.value,
                                           "password" to password.value
                                       )

                                       try {
                                           db.collection("users").document(it.uid).set(userMap)
                                               .addOnSuccessListener {
                                                   Toast.makeText(
                                                       context,
                                                       "Registration Successful",
                                                       Toast.LENGTH_SHORT
                                                   ).show()
                                                   navController.navigate("login")
                                               }
                                               .addOnFailureListener {
                                                   Toast.makeText(
                                                       context,
                                                       "Failed to save user data",
                                                       Toast.LENGTH_SHORT
                                                   ).show()
                                               }

                                           loading = false


                                       } catch (e: Exception) {
                                           loading = false
                                           Toast.makeText(
                                               context,
                                               "${e.message}",
                                               Toast.LENGTH_SHORT).show()
                                       }
                                   }
                               }
                           }
                   }
                   catch (f: Exception) {
                       loading = false
                       Toast.makeText(
                           context,
                           "${f.message}",
                           Toast.LENGTH_SHORT
                       )
                           .show()
                   }
               }
        ) {
                   Text(
                       "Register",

                       )
               }
               }

               TextButton(onClick = {
                   navController.navigate("login")
                   { popUpTo("login") { inclusive = true } }
               },
                   colors =  ButtonDefaults.textButtonColors(Color.White)) {
                   Text("Already have a account? SignIn",
                       color = Color.Blue)

               }
        }
    }
}


    @Preview
    @Composable
    fun SignUpScreenPreview() {
        MyProjectAppTheme {
            Screen2(rememberNavController())
        }
    }
