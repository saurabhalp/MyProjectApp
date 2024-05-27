package com.saurabhalp.myprojectapp
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

lateinit var auth : FirebaseAuth
@Composable
fun LoginScreen(navController: NavHostController,viewModel: MainViewModel) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var passwordVisible = remember { mutableStateOf(false) }
    var context = LocalContext.current
    var loading by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "BackgroundImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

//
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp).fillMaxSize()

            ) {

                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "AppLogo",
                    modifier = Modifier.size(200.dp)
                )
//
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    modifier = Modifier,
                    keyboardOptions = KeyboardOptions
                        .Default.copy(keyboardType = KeyboardType.Email),
                    label = {
                        Text(
                            text = "Email"
                        )
                    }

                )
                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    label = {
                        Text(
                            text = "Password"
                        )
                    },
                    trailingIcon = {
                        painterResource(id = R.drawable.icon1)


                        IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                            Icon(
                                painter = painterResource(R.drawable.icon1),
                                contentDescription = null,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    },

                )
                Spacer(modifier = Modifier.height(16.dp))

                // Login Button
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            loading = true
                            try {
                                FirebaseAuth.getInstance()
                                    .signInWithEmailAndPassword(email.value, password.value)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            loading = false
                                            MainViewModel().login()
                                            navController.navigate("home2")
                                            navController.popBackStack("login", inclusive = true)
                                        } else {
                                            loading = false
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                            } catch (e: Exception) {
                                loading = false
                                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        },
                        modifier = Modifier.width(200.dp)

                    ) {
                        Text("Login")
                    }
                }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sign-up Text
                    TextButton(onClick = { navController.navigate("signup") }) {
                        Text("Don't have an account? Sign up", color = Color.Blue)
                    }
                }
            }
       }



@Composable
@Preview
fun loginPreview(){
    LoginScreen(rememberNavController(),MainViewModel())
}


