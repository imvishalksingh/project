package com.productivityservicehub.eventlens.ui.screen

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.productivityservicehub.eventlens.R
import com.productivityservicehub.eventlens.data.models.PhotographerProfile
import com.productivityservicehub.eventlens.viewmodel.ProfilesViewModel
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    profilesViewModel: ProfilesViewModel,
    onPhotographerClick: (String) -> Unit // Added lambda for click handling
) {
    val stringItem = listOf(
        "Birthday Parties",
        "Weddings",
        "Engagement Parties",
        "Anniversaries",
        "Festivals",
        "Personal"
    )
    val imageItem = listOf(
        R.drawable.birthdayimage,
        R.drawable.weddingimage,
        R.drawable.engagmentimage,
        R.drawable.aniverceryimage,
        R.drawable.fesitalvs,
        R.drawable.personalmage
    )
    val photographerProfiles by profilesViewModel.photographerProfiles.collectAsState()

    // Fetch profiles when the screen is first loaded
    LaunchedEffect(Unit) {
        profilesViewModel.fetchPhotographerProfiles()
    }

    var searchtext by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = searchtext,
                onQueryChange = { searchtext = it },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(text = "Search") },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            ) {}

            HeaderItem("Popular Services", "See All")

            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(stringItem.size) { index ->
                    EventCard(text = stringItem[index], painterResource(imageItem[index]))
                }
            }

            HeaderItem("Popular Profiles", "See All")

            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(photographerProfiles) { profile ->
                    PhotographerCard(
                        profile = profile,
                        onClick = { onPhotographerClick(profile.id) } // Pass the profile ID to the click handler
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            ShareCard()
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotographerCard(profile: PhotographerProfile, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(250.dp)
            .width(190.dp)
            .clickable { onClick() }, // Trigger the click action
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .height(250.dp)
                .width(190.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = profile.profileImage,
                contentDescription = "Profile Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            )
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = profile.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Row {
                Text(
                    modifier = Modifier.padding(top = 1.dp),
                    text = profile.location,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    modifier = Modifier.padding(top = 1.dp),
                    text = profile.rating,
                    fontSize = 12.sp
                )
            }
        }
    }
}



@Composable
fun ShareCard(){
    Card(modifier = Modifier){
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center){
            Text("Invite Friends & get up to $100", fontSize = 18.sp , fontWeight = FontWeight.Bold)
            Text("Introduce Your friend to the fastest wat to hire photographer and viderographer for your events")
            TextButton(onClick = {}){
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically){

                    Text(text = "Invite Friends", fontWeight = FontWeight.Bold)
                    Icon(imageVector = Icons.Filled.ArrowForward,contentDescription = null)
                }
            }
        }
    }
}


@Composable
fun HeaderItem(heading: String, btntxt:String){
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            text = heading
        )
        Text(
            modifier = Modifier.clickable { },
            text = btntxt
        )

    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun EventCard(text: String, image: Painter) {
    Card(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .height(200.dp)
                .width(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .height(150.dp),
                painter = image,
                contentDescription = null
            )
            Text(modifier = Modifier.padding(top = 10.dp), text = text)

        }
    }
}