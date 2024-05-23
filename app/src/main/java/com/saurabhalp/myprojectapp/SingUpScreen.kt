package com.saurabhalp.myprojectapp

import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import com.saurabhalp.myprojectapp.ui.theme.MyProjectAppTheme

@Composable
fun Screen2(navController: NavHostController) {
    var name = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    var email = remember { mutableStateOf("") }
    var passwordVisible = remember { mutableStateOf(false) }

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
                   onValueChange = {email.value=it},
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
               Button(onClick = {}
               ) {
                   Text("Register",

                       )
               }

               TextButton(onClick ={ navController.navigate("login")},
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
