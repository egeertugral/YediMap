package com.example.yedimap.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.yedimap.R
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    object Home : Screen(
        route = "home",
        titleRes = R.string.nav_home,   // bunu az sonra ekleyeceğiz
        icon = Icons.Filled.Home
    )

    object Map : Screen(
        route = "map",
        titleRes = R.string.nav_map,
        icon = Icons.Filled.Map
    )

    object Schedule : Screen(
        route = "schedule",
        titleRes = R.string.nav_schedule,
        icon = Icons.Filled.Schedule
    )

    object Notifications : Screen(
        route = "notifications",
        titleRes = R.string.nav_notifications,
        icon = Icons.Filled.Notifications
    )

    object MyProfile : Screen(
        route = "my_profile",
        titleRes = R.string.nav_my_profile, // bunu strings.xml'e ekleyeceğiz
        icon = Icons.Filled.Person // ikon zorunlu olduğu için veriyoruz
    )

    companion object {
        val bottomNavItems = listOf(Home, Map, Schedule, Notifications)    }
}
