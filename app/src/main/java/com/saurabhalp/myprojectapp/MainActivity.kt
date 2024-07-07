package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.google.firebase.auth.FirebaseAuth
import com.saurabhalp.myprojectapp.ui.theme.MyProjectAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyProjectAppTheme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .background(Color(0xffe3f1fb)),
        bottomBar = {
            if (currentRoute?.destination?.route != "login" && currentRoute?.destination?.route!="signup") {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xffe3f1fb))
                ) {

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(
                                Alignment.CenterVertically
                            ), contentAlignment = Alignment.Center ){
                        BottomNavIcon(
                            navController = navController,
                            route = "home2",
                            drawableId = R.drawable.reminder,
                            context = context,
                            currentRoute = currentRoute?.destination?.route,
                            screenName = "Notes"
                        )
                    }

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(
                                Alignment.CenterVertically
                            ), contentAlignment = Alignment.Center ){
                        BottomNavIcon(
                            navController = navController,
                            route = "underConstruction",
                            drawableId = R.drawable.book,
                            context = context,
                            currentRoute = currentRoute?.destination?.route,
                            screenName = "Lab Files"
                        )
                    }

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(
                                Alignment.CenterVertically
                            ), contentAlignment = Alignment.Center ){
                        BottomNavIcon(
                            navController = navController,
                            route = "update",
                            drawableId = R.drawable.notification,
                            context = context,
                            currentRoute = currentRoute?.destination?.route,
                            screenName = "Update"
                        )
                    }

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(
                                Alignment.CenterVertically
                            ), contentAlignment = Alignment.Center ){
                        BottomNavIcon(
                            navController = navController,
                            route = "profile",
                            drawableId = R.drawable.userprofile,
                            context = context,
                            currentRoute = currentRoute?.destination?.route,
                            screenName = "Profile"
                        )
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = if (FirebaseAuth.getInstance().currentUser != null) "home2" else "login"
        ) {
            composable("login") { LoginScreen(navController) }
            composable("signup") { Screen2(navController) }
            composable("home2") { App(navController) }
            composable("update") { UpdateScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
            composable("App2/{detailText}") { backStackEntry ->
                val detailText = backStackEntry.arguments?.getString("detailText") ?: ""
                App2(navController, detailText)
            }
            composable("underConstruction") { UnderConstruction() }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen2() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .background(Color(0xffe3f1fb)),
        bottomBar = {
            if (currentRoute?.destination?.route != "login") {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xffe3f1fb))
                ) {

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(
                                Alignment.CenterVertically
                            ), contentAlignment = Alignment.Center ){
                        BottomNavIcon(
                            navController = navController,
                            route = "home2",
                            drawableId = R.drawable.reminder,
                            context = context,
                            currentRoute = currentRoute?.destination?.route,
                            screenName = "Notes"
                        )
                    }

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(
                                Alignment.CenterVertically
                            ), contentAlignment = Alignment.Center ){
                        BottomNavIcon(
                            navController = navController,
                            route = "underConstruction",
                            drawableId = R.drawable.book,
                            context = context,
                            currentRoute = currentRoute?.destination?.route,
                            screenName = "Lab Files"
                        )
                    }

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(
                                Alignment.CenterVertically
                            ), contentAlignment = Alignment.Center ){
                        BottomNavIcon(
                            navController = navController,
                            route = "update",
                            drawableId = R.drawable.notification,
                            context = context,
                            currentRoute = currentRoute?.destination?.route,
                            screenName = "Updates"
                        )
                    }

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .align(
                                Alignment.CenterVertically
                            ), contentAlignment = Alignment.Center ){
                        BottomNavIcon(
                            navController = navController,
                            route = "profile",
                            drawableId = R.drawable.userprofile,
                            context = context,
                            currentRoute = currentRoute?.destination?.route,
                            screenName = "Profile"
                        )
                    }
                }
            }
        }
    ) {
    }
    }

@Composable
fun BottomNavIcon(
    navController: NavController,
    route: String,
    drawableId: Int,
    context: Context,
    currentRoute: String?,
    screenName: String

) {
    Card() {
        Column (Modifier
                    .fillMaxWidth()
                    .background(Color(0xffe3f1fb))
                    .padding(start = 4.dp, end = 4.dp)

        ){
            Card(Modifier.align(Alignment.CenterHorizontally)) {
                IconButton(
                    onClick = {
                        if (FirebaseAuth.getInstance().currentUser != null) {
                            navController.popBackStack()
                            navController.navigate(route)
                        } else {
                            Toast.makeText(context, "Login First", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Modifier.
                        then( if (currentRoute == route) Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFC0E5F7))

                        else{
                            Modifier.align(Alignment.CenterHorizontally)
                                .background(Color(0xffe3f1fb))
                        }
                        )



                ) {
                    Image(
                        painter = painterResource(drawableId),
                        contentDescription = null,
                        Modifier
                            .padding(3.dp)
                            .height(100.dp)
                    )
                }
            }

            Text(text = screenName,Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFF0D2C3F))
        }
    }
}


@Preview
@Composable
fun MainScreenPreview(){
    MainScreen2()
}








