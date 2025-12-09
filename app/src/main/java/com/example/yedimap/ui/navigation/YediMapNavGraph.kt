package com.example.yedimap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.yedimap.ui.map.MapScreen
import com.example.yedimap.ui.notifications.NotificationsScreen
import com.example.yedimap.ui.schedule.ScheduleScreen
import com.example.yedimap.ui.home.HomeScreen

@Composable
fun YediMapNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Map.route) {
            MapScreen()
        }
        composable(Screen.Schedule.route) {
            ScheduleScreen()
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen()
        }
    }

}