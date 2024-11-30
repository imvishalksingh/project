package com.productivityservicehub.eventlens.ui.components


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.productivityservicehub.eventlens.data.models.BottomNavItem

@Composable
fun AppBottomBar(
    items: List<BottomNavItem>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(painter = painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}