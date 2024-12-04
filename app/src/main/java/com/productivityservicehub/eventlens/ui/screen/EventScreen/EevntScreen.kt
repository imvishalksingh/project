package com.productivityservicehub.eventlens.ui.screen.EventScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@Composable
fun EventScreen(
    eventName: String,
    eventDate: String,
    eventLocation: String,
    eventDescription: String,
    eventImageUrl: Painter
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = eventImageUrl,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = eventName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date: $eventDate", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Location: $eventLocation", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = eventDescription, style = MaterialTheme.typography.bodySmall)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotographerItem(photographer: Photographer) {
    // Photographer item layout
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable {
                // Navigate to photographer profile or handle click
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            GlideImage(
                model = (photographer.profileImageUrl),
                contentDescription = "Photographer Profile Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(50))
                    .border(2.dp, Color.Gray, RoundedCornerShape(50)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Photographer Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = photographer.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = photographer.specialty,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            // More Info Button
            IconButton(onClick = {
                // Handle more info button click
            }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "More Info"
                )
            }
        }
    }
}

data class Photographer(
    val name: String,
    val specialty: String,
    val profileImageUrl: String
)
