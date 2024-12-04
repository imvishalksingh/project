package com.productivityservicehub.eventlens.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.productivityservicehub.eventlens.R
import com.productivityservicehub.eventlens.data.models.PhotographerProfile
import com.productivityservicehub.eventlens.viewmodel.ProfilesViewModel

@Composable
fun EntryScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.home),
                contentDescription = "Home Background Image"
            )
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                    navController.navigate("user_home")
                }
            ) {
                Text(text = "Get Started")
            }
        }

        // Title
        Text(
            text = "Professional Profiles and Location-Based Search",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        // Description
        Text(
            text = "Find photographers and videographers with detailed professional profiles. Use location-based search to discover local talent.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Discover Talented Professionals Near You",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // InfoCards
        InfoCard(
            icon = Icons.Default.Home,
            title = "Secure Booking and Real-Time Chat",
            description = "Book with Confidence and Communicate in Real Time",
            buttonText = "Book Now",
            onButtonClick = { /* Navigate to booking */ }
        )
        InfoCard(
            icon = Icons.Default.Search,
            title = "Payment Options and Secure Transactions",
            description = "Choose Your Payment Method with Secure Transactions",
            buttonText = "View Options",
            onButtonClick = { /* Navigate to payment */ }
        )
        InfoCard(
            icon = Icons.Default.Settings,
            title = "Search by Location and Expertise",
            description = "Find the Perfect Match Based on Location and Expertise",
            buttonText = "Explore",
            onButtonClick = { /* Navigate to search */ }
        )
        ExploreProfilesSection()
        BottomFooter()
    }
}

@Composable
fun InfoCard(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterHorizontally),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            TextButton(
                onClick = onButtonClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = buttonText)
            }
        }
    }
}

@Composable
fun ExploreProfilesSection() {
    val profilesViewModel: ProfilesViewModel = viewModel()
    val photographerProfiles by profilesViewModel.photographerProfiles.collectAsState()
    LaunchedEffect(Unit) {
        profilesViewModel.fetchPhotographerProfiles()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Section Title
        Text(
            text = "Explore Professional Profiles",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Browse detailed professional profiles to find the perfect match for your project.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(photographerProfiles) { profile ->
                PhotographerCard(
                    profile = profile,
                    onClick = { PhotographerProfile() } // Pass the profile ID to the click handler
                )
            }
        }

    }
}


@Composable
fun BottomFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 12.dp), // Adjust padding as needed
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "© Business All Rights Reserved.",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}
