//package com.saurabhalp.myprojectapp
//
//@Composable
//fun MyApp() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = { BottomNavigationBar(navController = navController) }
//    ) {
//        NavigationGraph(navController = navController)
//    }
//}
//
//@Composable
//fun BottomNavigationBar(navController: NavHostController) {
//    val items = listOf(
//        BottomNavItem.Home,
//        BottomNavItem.Notifications,
//        BottomNavItem.Profile
//    )
//    BottomNavigation {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//        items.forEach { item ->
//            BottomNavigationItem(
//                icon = { Icon(item.icon, contentDescription = item.title) },
//                label = { Text(item.title) },
//                selected = currentRoute == item.route,
//                onClick = {
//                    navController.navigate(item.route) {
//                        // Avoid multiple copies of the same destination
//                        popUpTo(navController.graph.startDestinationId) {
//                            saveState = true
//                        }
//                        // Avoid building up a large stack of destinations
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun BottomNavigationBar(navController: NavHostController) {
//    val items = listOf(
//        BottomNavItem.Home,
//        BottomNavItem.Notifications,
//        BottomNavItem.Profile
//    )
//    BottomNavigation {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//        items.forEach { item ->
//            BottomNavigationItem(
//                icon = { Icon(item.icon, contentDescription = item.title) },
//                label = { Text(item.title) },
//                selected = currentRoute == item.route,
//                onClick = {
//                    navController.navigate(item.route) {
//                        // Avoid multiple copies of the same destination
//                        popUpTo(navController.graph.startDestinationId) {
//                            saveState = true
//                        }
//                        // Avoid building up a large stack of destinations
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}
//
//sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String) {
//    object Home : BottomNavItem("Home", Icons.Filled.Home, "home")
//    object Notifications : BottomNavItem("Notifications", Icons.Filled.Notifications, "notifications")
//    object Profile : BottomNavItem("Profile", Icons.Filled.Person, "profile")
//}
//
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
