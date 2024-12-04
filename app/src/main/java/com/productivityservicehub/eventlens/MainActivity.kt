package com.productivityservicehub.eventlens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.productivityservicehub.eventlens.data.models.User
import com.productivityservicehub.eventlens.ui.Main.MainScreen
import com.productivityservicehub.eventlens.ui.auth.LoginScreen
import com.productivityservicehub.eventlens.ui.auth.RegisterScreen
import com.productivityservicehub.eventlens.ui.screen.EntryScreen
import com.productivityservicehub.eventlens.ui.screen.EventScreen.EventScreen
import com.productivityservicehub.eventlens.ui.theme.EventLensTheme
import com.productivityservicehub.eventlens.viewmodel.AuthViewModel
import com.productivityservicehub.eventlens.viewmodel.ProfilesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventLensTheme {

                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel() // Shared ViewModel
                val profilesViewModel: ProfilesViewModel = viewModel()

                val isUserLoggedIn =
                    authViewModel.isUserLoggedIn.collectAsState() // Expose user state as Flow/State

                // Determine the start destination based on login state
                val startDestination = if (isUserLoggedIn.value) "entry" else "login"
                //val startDestination = "entry"

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("entry") {
                        EntryScreen(navController)
                    }
                    composable("login") {
                        LoginScreen(
                            authViewModel = authViewModel,
                            onNavigateToUserHome = {
                                navController.navigate("user_home") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            },
                            onNavigateToPhotographerHome = {
                                navController.navigate("photographer_home") {
                                    popUpTo(
                                        "login"
                                    ) { inclusive = true }
                                }
                            },
                            onNavigateToRegister = { navController.navigate("register") }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            authViewModel = authViewModel,
                            onRegisterSuccess = {
                                navController.navigate("login") {
                                    popUpTo("register") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable("user_home") {
                        MainScreen("USER",profilesViewModel , navController)
                    }

                    composable("photographer_home") {
                        MainScreen(
                            role = "PHOTOGRAPHER",profilesViewModel,navController
                        )
                    }
                    composable(
                        route = "event?name={name}&date={date}&location={location}&description={description}&image={image}",
                        arguments = listOf(
                            navArgument("name") { defaultValue = "Event Name" },
                            navArgument("date") { defaultValue = "Event Date" },
                            navArgument("location") { defaultValue = "Event Location" },
                            navArgument("description") { defaultValue = "Event Description" },
                            navArgument("image") { defaultValue = "" } // Handle images properly
                        )
                    ) { backStackEntry ->
                        EventScreen(
                            eventName = backStackEntry.arguments?.getString("name") ?: "Unknown",
                            eventDate = backStackEntry.arguments?.getString("date") ?: "Unknown",
                            eventLocation = backStackEntry.arguments?.getString("location") ?: "Unknown",
                            eventDescription = backStackEntry.arguments?.getString("description") ?: "Unknown",
                            eventImageUrl = painterResource(R.drawable.ic_launcher_background) // Replace with logic to load image
                        )
                    }
                }
            }

        }
    }
}

