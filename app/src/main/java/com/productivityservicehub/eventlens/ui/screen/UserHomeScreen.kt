package com.productivityservicehub.eventlens.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.productivityservicehub.eventlens.R
import com.productivityservicehub.eventlens.data.models.Event
import com.productivityservicehub.eventlens.data.models.PhotographerProfile
import com.productivityservicehub.eventlens.ui.screen.EventScreen.EventScreen
import com.productivityservicehub.eventlens.viewmodel.ProfilesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    profilesViewModel: ProfilesViewModel,
    onEventClick: (Event) -> Unit
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
    val events = stringItem.mapIndexed { index, name ->
        Event(
            name = name,
            date = "Date $index",
            location = "Location $index",
            description = "Description for $name",
            image = imageItem[index]
        )
    }
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
            .padding(start = 16.dp, top = 16.dp, end = 16.dp , bottom = 100.dp),
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
                items(events) { event ->
                    EventCard(
                        text = event.name,
                        image = painterResource(event.image),
                        onClick = { onEventClick(event) }
                    )
                }
            }

            HeaderItem("Popular Profiles", "See All")

            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(photographerProfiles) { profile ->
                    PhotographerDirectoryScreen(
                        profile = profile,
                        onClick = { PhotographerProfile() } // Pass the profile ID to the click handler
                    )
                }
            }

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
            .width(250.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .height(250.dp)
                .width(250.dp),
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
    val context = LocalContext.current
    Card(modifier = Modifier){
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Center){
            Text("Invite Friends & get up to $100", fontSize = 18.sp , fontWeight = FontWeight.Bold)
            Text("Introduce Your friend to the fastest wat to hire photographer and viderographer for your events")
            TextButton(onClick = {Icon(imageVector = Icons.Filled.ArrowForward,contentDescription = null)}){
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
    val context = LocalContext.current
    Spacer(modifier = Modifier.height(10.dp))
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
            modifier = Modifier.clickable { Toast.makeText(context,"This feature is not implemented yet !!!",Toast.LENGTH_SHORT).show() },
            text = btntxt
        )

    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun EventCard(text: String, image: Painter,onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp)
            .clickable { onClick() },
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

@Composable
fun PhotographerDirectoryScreen(profile: PhotographerProfile, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { // Dummy items for demonstration
            PhotographerCard(
                imageUrl = profile.profileImage, // Replace with real image URL
                title = profile.name,
                rating = profile.rating,
                distance = "8.2 Km",
                address = profile.location
            )
            Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotographerCard(
    imageUrl: String,
    title: String,
    rating: String,
    distance: String,
    address: String
) {

    val context = LocalContext.current
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(330.dp).height(320.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Main Image
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    IconButton(onClick = { Toast.makeText(context,"This feature is not implemented yet !!!",Toast.LENGTH_SHORT).show() }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { Toast.makeText(context,"This feature is not implemented yet !!!",Toast.LENGTH_SHORT).show() }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options"
                        )
                    }
                }
            }


            // Rating, Distance, and Address
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = rating.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFA726)
                )
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating Star",
                    tint = Color(0xFFFFA726),
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = distance,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
