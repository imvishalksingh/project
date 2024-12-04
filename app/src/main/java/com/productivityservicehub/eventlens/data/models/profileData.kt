package com.productivityservicehub.eventlens.data.models

import androidx.annotation.DrawableRes

data class PhotographerProfile(
    val id: String = "",
    val name: String = "",
    val profileImage: String = "",
    val location: String = "",
    val role: String = "",
    val rating:String="4.5"
// Ensure this field exists in the database
)

data class User(
    var name: String = "",
    var email: String = "",
    var profileImage: String = "",
    var role: String = "", // Add these fields if present in Firebase
    var location: String = "" // Add these fields if present in Firebase
)

data class Event(
    val name: String,
    val date: String,
    val location: String,
    val description: String,
    @DrawableRes val image: Int
)
