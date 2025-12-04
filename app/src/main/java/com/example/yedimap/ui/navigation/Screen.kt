package com.example.yedimap.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.yedimap.R

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
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

    companion object {
        val bottomNavItems = listOf(Map, Schedule, Notifications)
    }
}
