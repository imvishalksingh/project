package com.productivityservicehub.eventlens.ui.auth

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.productivityservicehub.eventlens.viewmodel.AuthViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("4.5") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var role by remember { mutableStateOf("USER") } // Default role
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> profileImageUri = uri }
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Profile Image Picker
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (profileImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUri),
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(100.dp)
                )
            } else {
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Select Profile Image")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Name Field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Location Field
        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Email Field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password Field
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Role Selection (User or Photographer)
        Row {
            RadioButton(
                selected = role == "USER",
                onClick = { role = "USER" }
            )
            Text("Regular User")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = role == "PHOTOGRAPHER",
                onClick = { role = "PHOTOGRAPHER" }
            )
            Text("Photographer/Videographer")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {
                if (password == confirmPassword && email.isNotEmpty() && name.isNotEmpty() && location.isNotEmpty()) {
                    isError = false
                    scope.launch {
                        val success = authViewModel.registerWithProfile(
                            email = email,
                            password = password,
                            name = name,
                            location = location,
                            role = role,
                            profileImageUri = profileImageUri,
                            rating=rating
                        )
                        if (success) {
                            onRegisterSuccess() // Navigate on success
                        } else {
                            isError = true
                            errorMessage = "Registration failed. Please try again."
                        }
                    }
                } else {
                    isError = true
                    errorMessage = "Please fill all fields and ensure passwords match."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        // Error Message
        if (isError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}
