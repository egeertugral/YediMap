package com.example.yedimap

import androidx.compose.material3.ExperimentalMaterial3Api
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.yedimap.ui.navigation.Screen
import com.example.yedimap.ui.navigation.YediMapNavGraph
import com.example.yedimap.ui.splash.SplashScreen
import com.example.yedimap.ui.theme.YediMapTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YediMapTheme {
                YediMapRoot()
            }
        }
    }
}

@Composable
fun YediMapRoot() {
    // Başta splash görünsün
    val showSplash = remember { mutableStateOf(true) }

    // 2.5 saniye bekleyip ana uygulamaya geç
    LaunchedEffect(Unit) {
        delay(2500) // 2500 ms = 2.5 saniye
        showSplash.value = false
    }

    if (showSplash.value) {
        SplashScreen()
    } else {
        YediMapApp()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YediMapApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "YediMap",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                Screen.bottomNavItems.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = stringResource(id = screen.titleRes)
                            )
                        },
                        label = { Text(text = stringResource(id = screen.titleRes)) },
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        YediMapNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
