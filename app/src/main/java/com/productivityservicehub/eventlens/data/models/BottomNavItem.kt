package com.productivityservicehub.eventlens.data.models

import com.productivityservicehub.eventlens.R

sealed class BottomNavItem(val title: String, val icon: Int) {
    object Home : BottomNavItem("Home", R.drawable.ic_home)
    object Messages : BottomNavItem("Messages", R.drawable.ic_message)
    object Bookings : BottomNavItem("Bookings", R.drawable.ic_calendar)
    object Profile : BottomNavItem("Profile", R.drawable.ic_user)
    object Dashboard : BottomNavItem("Dashboard", R.drawable.ic_message)
}
