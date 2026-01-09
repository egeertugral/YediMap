package com.example.yedimap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.yedimap.ui.about.AboutYediMapScreen
import com.example.yedimap.ui.auth.LoginScreen
import com.example.yedimap.ui.auth.SignupScreen
import com.example.yedimap.ui.cafeteria.CafeteriaScreen
import com.example.yedimap.ui.floors.FloorsScreen
import com.example.yedimap.ui.map.MapScreen
import com.example.yedimap.ui.schedule.ScheduleScreen
import com.example.yedimap.ui.home.HomeScreen
import com.example.yedimap.ui.myprofile.MyProfileScreen
import com.example.yedimap.ui.notifications.FeedbackScreen
import com.example.yedimap.ui.notifications.ThankYouScreen
import com.example.yedimap.ui.settings.SettingsScreen

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
            HomeScreen(
                onProfileClick = {
                    navController.navigate(Screen.MyProfile.route)
                },

                onFloorsClick = { navController.navigate("floors") },

                onCafeteriaClick = {
                    navController.navigate("cafeteria")
                },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable("floors") {
            FloorsScreen(
                onBackClick = { navController.popBackStack() } // Home'a döner
            )
        }
        composable(Screen.Map.route) {
            MapScreen()
        }
        composable(Screen.Schedule.route) {
            ScheduleScreen()
        }
        composable(Screen.AboutYediMap.route) {
            AboutYediMapScreen(
                onBackClick = { navController.popBackStack() } // ✅ Settings’e geri döner
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onAboutClick = { navController.navigate(Screen.AboutYediMap.route) }
            )
        }
        composable("login") {
            LoginScreen(
                onBack = { navController.popBackStack() }, // ProfileSelection'a döner (stack doğruysa)
                onSignUp = { navController.navigate("signup") },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("signup") {
            SignupScreen(
                onBack = { navController.popBackStack() }, // login'e döner
                onSignupSuccess = { navController.popBackStack() } // login'e döner
            )
        }
        composable(Screen.Notifications.route) {
            FeedbackScreen(
                onClose = {
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                    }
                },
                onSubmit = {
                    navController.navigate("thank_you")
                }
            )
        }
        composable("thank_you") {
            ThankYouScreen(
                onDone = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo("thank_you") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.MyProfile.route) {
            MyProfileScreen(onBackClick = { navController.popBackStack() })
        }
        composable("cafeteria") {
            CafeteriaScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

    }

}