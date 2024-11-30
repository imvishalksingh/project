package com.productivityservicehub.eventlens.ui.Main

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import com.productivityservicehub.eventlens.R
import com.productivityservicehub.eventlens.data.models.BottomNavItem
import com.productivityservicehub.eventlens.data.models.User
import com.productivityservicehub.eventlens.ui.components.AppBottomBar
import com.productivityservicehub.eventlens.ui.screen.UserHomeScreen
import com.productivityservicehub.eventlens.viewmodel.ProfilesViewModel
import kotlinx.coroutines.tasks.await

@Composable
fun AppNavigation(user: User, role: String, profilesViewModel: ProfilesViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(user, role, profilesViewModel)
        }
        composable(
            "messages/{photographerId}",
            arguments = listOf(navArgument("photographerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val photographerId = backStackEntry.arguments?.getString("photographerId")
            if (photographerId != null) {
                MessagesScreen(photographerId = photographerId)
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    user: User,
    role: String,
    profilesViewModel: ProfilesViewModel,
) {
    val items = if (role == "USER") {
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Messages,
            BottomNavItem.Bookings,
            BottomNavItem.Profile
        )
    } else {
        listOf(
            BottomNavItem.Dashboard,
            BottomNavItem.Messages,
            BottomNavItem.Bookings,
            BottomNavItem.Profile
        )
    }

    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                items = items,
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) {
        when (selectedItem) {
            0 -> {
                if (role == "USER") {
                    UserHomeScreen(
                        profilesViewModel = profilesViewModel,
                        onPhotographerClick = { photographerId ->

                        }
                    )
                } else PhotographerDashboardScreen()
            }
            1 -> MessagesListScreen()
            2 -> BookingsScreen()
            3 -> ProfileScreen()
        }
    }
}



@Composable
fun PhotographerDashboardScreen() {
    Text("Photographer/Videographer Dashboard", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
}


@Composable
fun MessagesScreen(photographerId: String) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserId = currentUser?.uid ?: return
    val database = FirebaseDatabase.getInstance().getReference("chats/${currentUserId}_$photographerId/messages")
    val messages = remember { mutableStateListOf<Message>() }
    var newMessage by remember { mutableStateOf("") }

    // Real-time listener to update messages
    LaunchedEffect(Unit) {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Message::class.java)?.let {

                    messages.add(0, it) // Add to the beginning of the list for reverse layout
                    Log.d("MessagesScreen", "New message received: $messages") // Log for debugging
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle message updates if needed
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Handle message removal if needed
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle message move if needed
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MessagesScreen", "Error: ${error.message}")
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                MessageBubble(message = message, isCurrentUser = message.senderId == currentUserId)
            }
        }

        // Message Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (newMessage.isNotBlank()) {
                            sendMessage(database, currentUserId, newMessage)
                            newMessage = ""
                        }
                    }
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newMessage.isNotBlank()) {
                    sendMessage(database, currentUserId, newMessage)
                    newMessage = ""
                }
            }) {
                Text("Send")
            }
        }
    }
}

// Message Bubble Composable
@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isCurrentUser) MaterialTheme.colorScheme.primary else Color.Gray,
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Text(text = message.message, color = Color.White, fontSize = 16.sp)
        }
    }
}

// Send Message Function
private fun sendMessage(database: DatabaseReference, senderId: String, message: String) {
    val messageId = database.push().key ?: return
    val newMessage = Message(
        senderId = senderId,
        message = message,
        timestamp = System.currentTimeMillis()
    )
    database.child(messageId).setValue(newMessage)
}

@Composable
fun MessagesListScreen() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserId = currentUser?.uid ?: return

    val database = FirebaseDatabase.getInstance().getReference("chats")
    val chatList = remember { mutableStateListOf<ChatItem>() }

    // Fetch list of chats
    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                snapshot.children.forEach { chatSnapshot ->
                    val chatId = chatSnapshot.key ?: return@forEach
                    val participants = chatId.split("_")
                    if (participants.contains(currentUserId)) {
                        val otherParticipantId = participants.first { it != currentUserId }
                        val chatItem = ChatItem(chatId = chatId, otherParticipantId = otherParticipantId)
                        chatList.add(chatItem)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text(
            text = "Your Conversations",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn {
            items(chatList) { chatItem ->
                ChatRow(chatItem = chatItem, onClick = {

                })
            }
        }
    }
}

@Composable
fun ChatRow(chatItem: ChatItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Chat with: ${chatItem.otherParticipantId}", // Replace with actual name fetching logic
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.weight(1f)
        )
    }
}

// Data Class to Represent a Chat
data class ChatItem(
    val chatId: String,
    val otherParticipantId: String
)

// Message Data Class
data class Message(
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)

@Composable
fun BookingsScreen() {
    Text("Bookings Screen", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen() {
    var user by remember { mutableStateOf(User("", "", "")) }
    var showPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val profileImage = remember { mutableStateOf<Uri?>(null) }
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("users")
    val storage = FirebaseStorage.getInstance().reference

    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid ?: return@LaunchedEffect
        database.child(userId).get().addOnSuccessListener { snapshot ->
            snapshot.getValue(User::class.java)?.let {
                user = it
                Log.d("ProfileScreen", "Fetched profileImage URL: ${user.profileImage}")
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
        }
    }
// Image picker launcher to select a new profile image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            profileImage.value = uri
            uri?.let {
                // Upload image to Firebase Storage
                val imageRef = storage.child("profile_images/${auth.currentUser?.uid}.jpg")
                val uploadTask = imageRef.putFile(it)
                uploadTask.addOnSuccessListener {
                    // Get the download URL
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        database.child(auth.currentUser?.uid ?: "").child("profileImage")
                            .setValue(downloadUrl.toString())
                        user.profileImage = downloadUrl.toString()
                        Toast.makeText(context, "Profile image updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Profile image with placeholder and error handling
        Card(
            modifier = Modifier.size(200.dp),
            shape = CircleShape
        ) {
            GlideImage(
                model = user.profileImage,
                contentDescription = "User Photo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop,

            )



        }

        Text(
            text = "NAME: ${user.name.uppercase()}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "EMAIL: ${user.email}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )

        // Edit Profile Button
        TextButton(onClick = { showPopup = true }) {
            Text(text = "Edit Profile", color = Color.Cyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        if (showPopup) {
            EditProfilePopup(
                user = user,
                profileImage = remember { mutableStateOf(null) },
                imagePickerLauncher = { imagePickerLauncher.launch("image/*") },
                onDismiss = { showPopup = false }
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EditProfilePopup(
    user: User,
    profileImage: MutableState<Uri?>,
    imagePickerLauncher: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("users")

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                // Editable Profile Image
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { imagePickerLauncher() },
                    shape = CircleShape
                ) {
                    GlideImage(
                        model =profileImage.value ?: user.profileImage ,
                        contentDescription = "User Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )

                // Save Button
                Button(
                    onClick = {
                        val userId = auth.currentUser?.uid ?: return@Button
                        val userData = mapOf(
                            "name" to name,
                            "email" to email,
                            "profileImage" to user.profileImage,
                            "role" to user.role,
                            "location" to user.location
                        )
                        database.child(userId).setValue(userData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Profile updated", Toast.LENGTH_LONG).show()
                                onDismiss()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to save data", Toast.LENGTH_LONG).show()
                            }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}
