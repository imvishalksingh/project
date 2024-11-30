package com.productivityservicehub.eventlens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.productivityservicehub.eventlens.data.models.PhotographerProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



class ProfilesViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()

    private val _photographerProfiles = MutableStateFlow<List<PhotographerProfile>>(emptyList())
    val photographerProfiles: StateFlow<List<PhotographerProfile>> = _photographerProfiles

    fun fetchPhotographerProfiles() {
        viewModelScope.launch {
            try {
                val snapshot = database.reference.child("users").get().await()
                val profiles = snapshot.children.mapNotNull { dataSnapshot ->
                    val profile = dataSnapshot.getValue(PhotographerProfile::class.java)
                    if (profile?.role == "PHOTOGRAPHER") profile else null
                }
                _photographerProfiles.value = profiles
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
