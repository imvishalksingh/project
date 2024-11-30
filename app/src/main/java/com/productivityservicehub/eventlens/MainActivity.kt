package com.productivityservicehub.eventlens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.productivityservicehub.eventlens.data.models.User
import com.productivityservicehub.eventlens.ui.Main.MainScreen
import com.productivityservicehub.eventlens.ui.auth.LoginScreen
import com.productivityservicehub.eventlens.ui.auth.RegisterScreen
import com.productivityservicehub.eventlens.ui.screen.UserHomeScreen
import com.productivityservicehub.eventlens.ui.theme.EventLensTheme
import com.productivityservicehub.eventlens.viewmodel.AuthViewModel
import com.productivityservicehub.eventlens.viewmodel.ProfilesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventLensTheme {
                val authViewModel: AuthViewModel by viewModels()
                val profilesViewModel: ProfilesViewModel by viewModels()
                // Create ViewModel at the parent level

                val user=User()
                val currentScreen = remember { mutableStateOf("LOGIN") }

                when (currentScreen.value) {
                    "LOGIN" -> LoginScreen(
                        authViewModel = authViewModel,
                        onNavigateToUserHome = { currentScreen.value = "USER_HOME" },
                        onNavigateToPhotographerHome = { currentScreen.value = "PHOTOGRAPHER_HOME" },
                        onNavigateToRegister = { currentScreen.value = "REGISTER" }
                    )
                    "REGISTER" -> RegisterScreen(
                        authViewModel = authViewModel,
                        onRegisterSuccess = { currentScreen.value = "LOGIN" }
                    )
                    "USER_HOME" -> MainScreen(user,"USER",profilesViewModel = profilesViewModel)
                    "PHOTOGRAPHER_HOME" -> MainScreen(user,"PHOTOGRAPHER",profilesViewModel = profilesViewModel)
                }
            }
        }
    }
}

