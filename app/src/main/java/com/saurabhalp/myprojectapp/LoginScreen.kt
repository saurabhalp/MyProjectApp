package com.saurabhalp.myprojectapp
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.mutableStateOf

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.saurabhalp.myprojectapp.ui.theme.MyProjectAppTheme


class LoginScreen : AppCompatActivity()

@Composable
fun LoginScreen(navController: NavHostController) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var passwordVisible = remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "BackgroundImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxHeight()
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .matchParentSize()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "AppLogo",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                modifier = Modifier,
                keyboardOptions = KeyboardOptions
                    .Default.copy(keyboardType = KeyboardType.Email)
                ,
                label = { Text(
                    text = "Email"
                ) }

            )
            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                visualTransformation = if(passwordVisible.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                label = { Text(
                    text = "Password"
                ) }

            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {

                    },
                    modifier = Modifier.width(200.dp)

            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign-up Text
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Don't have an account? Sign up", color = Color.Blue)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyProjectAppTheme {
        LoginScreen(rememberNavController())
    }
}
