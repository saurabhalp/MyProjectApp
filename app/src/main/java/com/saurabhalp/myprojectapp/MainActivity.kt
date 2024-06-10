package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.saurabhalp.myprojectapp.ui.theme.MyProjectAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyProjectAppTheme {
                MainScreen(MainViewModel())
            }
        }
    }
}

fun fetchUsername(uid: String, context: Context, onResult: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("users").document(uid)
    docRef.get()
        .addOnSuccessListener { document ->
            if (document != null) {
                onResult(document.getString("name") ?: "User3")
            } else {
                Toast.makeText(context, "error in fetching name", Toast.LENGTH_SHORT).show()
                onResult("User1")
            }
        }
        .addOnFailureListener { exception ->
            Log.w("FetchUsername", "Error getting document", exception)
            onResult("User2")
        }
}
@Composable
fun Greeting(name: String) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()) {
            Box (
                Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray)){
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp))
                {
                    Text(
                        text = "Welcome to the App $name",
                        fontSize = 30.sp,
                        lineHeight = 45.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.padding(bottom = 16.dp),
        bottomBar = {
                Row (
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)){
                    Button(onClick = {
                        if(FirebaseAuth.getInstance().currentUser!=null){
                            navController.popBackStack()
                        navController.navigate("home2")
                        }
                        else{
                            Toast.makeText(context, "Login First", Toast.LENGTH_SHORT).show()
                            }},

                            Modifier
                                .weight(1f)
                                .padding(3.dp)) { Text("Notes", )
                    }
                    Button(onClick = { },
                        Modifier
                            .weight(1f)
                            .padding(3.dp)) { Text("LabFile",maxLines = 1)
                    }
                    Button(onClick = {},
                        Modifier
                            .weight(1f)
                            .padding(3.dp)) { Text("Notice")
                    }
                    Button(onClick = { navController.popBackStack()
                        navController.navigate("profile")

                                     },
                        Modifier
                            .weight(1f)
                            .padding(3.dp)) { Text("Profile")
                    }}

        }
    ) {
            NavHost(
                navController = navController, startDestination =
                if (FirebaseAuth.getInstance().currentUser != null) "home2" else
                    "login"
            ) {
                composable("login") { LoginScreen(navController,viewModel) }
                composable("signup") { Screen2(navController) }
                composable("home2") { App(navController) }
                composable("profile") { ProfileScreen() }
                composable("App2/{detailText}") { backStackEntry ->
                    val detailText = backStackEntry.arguments?.getString("detailText") ?: ""
                    App2(navController, detailText)
                }
            }
        }
    }





//@Composable
//fun NavigationGraph(navController: NavHostController) {
//    NavHost(navController, startDestination = BottomNavItem.Home.route) {
//        composable(BottomNavItem.Home.route) {
//            HomeScreen()
//        }
//        composable(BottomNavItem.Notifications.route) {
//            NotificationsScreen()
//        }
//        composable(BottomNavItem.Profile.route) {
//            ProfileScreen()
//        }
//    }
//}








