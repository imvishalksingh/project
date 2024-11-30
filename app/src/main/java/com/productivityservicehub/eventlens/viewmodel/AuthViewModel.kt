package com.productivityservicehub.eventlens.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseDatabase.getInstance()


    suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true // Login successful
        } catch (e: Exception) {
            false // Login failed
        }
    }


    suspend fun getUserRole(): String? {
        return try {
            val userId = auth.currentUser?.uid ?: return null
            val snapshot = database.reference.child("users").child(userId).child("role").get().await()
            snapshot.getValue(String::class.java) // Returns "USER" or "PHOTOGRAPHER"
        } catch (e: Exception) {
            null
        }
    }



    suspend fun registerWithProfile(
    email: String,
    password: String,
    name: String,
    location: String,
    role: String,
    profileImageUri: Uri? = null,
    rating: String
    ): Boolean {
        return try {
            // Register user with Firebase Authentication
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = userCredential.user?.uid ?: return false

            // Upload profile image to Firebase Storage if provided
            val profileImageUrl = profileImageUri?.let {
                val storageRef = storage.reference.child("profile_images/$userId.jpg")
                storageRef.putFile(it).await()
                storageRef.downloadUrl.await().toString()
            }

            // Save additional user details in Realtime Database
            val userData = mutableMapOf(
                "email" to email,
                "name" to name,
                "location" to location,
                "role" to role,
                "rating" to rating
            )
            profileImageUrl?.let { userData["profileImage"] = it }

            database.reference.child("users").child(userId).setValue(userData).await()

            true // Registration successful
        } catch (e: Exception) {
            e.printStackTrace()
            false // Registration failed
        }
    }
}


