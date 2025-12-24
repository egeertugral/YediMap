package com.example.yedimap

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.example.yedimap.ui.theme.PurpleSplash
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.yedimap.ui.language.LanguageSelectionScreen
import com.example.yedimap.ui.profile.ProfileSelectionScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.yedimap.ui.theme.PrimaryPurple

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

enum class OnboardingStep {
    LANGUAGE,
    PROFILE,
    MAIN
}

@Composable
fun YediMapRoot() {
    var showSplash by remember { mutableStateOf(true) }
    var currentStep by remember { mutableStateOf(OnboardingStep.LANGUAGE) }

    LaunchedEffect(Unit) {
        delay(2000)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        when (currentStep) {
            OnboardingStep.LANGUAGE -> {
                LanguageSelectionScreen(
                    onEnglishClick = {
                        currentStep = OnboardingStep.PROFILE
                    }
                )
            }
            OnboardingStep.PROFILE -> {
                ProfileSelectionScreen(
                    onBackClick = { currentStep = OnboardingStep.LANGUAGE },
                    onStudentClick = {
                        // TODO: ileride Student onboarding / main app
                        currentStep = OnboardingStep.MAIN
                    },
                    onVisitorClick = {
                        // TODO: ileride Visitor onboarding / main app
                        currentStep = OnboardingStep.MAIN
                    }
                )
            }
            OnboardingStep.MAIN -> {
                YediMapApp()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YediMapApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("?")
    val bottomBarHiddenRoutes = setOf(
        "my_profile",
        "drawer",
        "cafeteria"
    )

    val shouldShowBottomBar = currentRoute !in bottomBarHiddenRoutes
    var isDrawerOpen by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = PrimaryPurple,  // #52489C arka plan
                    contentColor = Color.White,
                    modifier = Modifier.height(56.dp)
                ) {
                    Screen.bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route

                        NavigationBarItem(
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
                            },
                            icon = {
                                // Seçili ikonda mor yuvarlak + hafif gölge efekti
                                Box(
                                    modifier = Modifier.size(45.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selected) {
                                        Box(
                                            modifier = Modifier
                                                .matchParentSize()
                                                .shadow(
                                                    elevation = 8.dp,
                                                    shape = CircleShape,
                                                    clip = false
                                                )
                                                .background(
                                                    color = Color(0xFF3E2F7D),
                                                    shape = CircleShape
                                                )
                                        )
                                    }

                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = stringResource(id = screen.titleRes),
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.8f),
                                selectedTextColor = Color.White,
                                unselectedTextColor = Color.White.copy(alpha = 0.8f),
                                indicatorColor = Color.Transparent // default pill'i kapat
                            )
                        )
                    }
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
