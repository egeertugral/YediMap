package com.example.yedimap.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yedimap.R
import com.example.yedimap.ui.drawer.DrawerMenu
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.yedimap.ui.auth.AuthViewModel

private val HomePurple = Color(0xFF614184)

@Composable
fun HomeScreen(
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCafeteriaClick: () -> Unit = {},
    onDrawerStateChange: (Boolean) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val vm: AuthViewModel = viewModel()
    val user by vm.currentUser.collectAsState()
    // ✅ Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(drawerState.currentValue) {
        onDrawerStateChange(drawerState.currentValue == DrawerValue.Open)
    }
    // ✅ Drawer wrapper
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            DrawerMenu(
                name = user?.fullName ?: "Guest",
                email = user?.email ?: "guest@yeditepe.edu.tr",
                onClose = {
                    scope.launch { drawerState.close() }
                },
                onMyProfileClick = {
                    scope.launch { drawerState.close() }
                    onProfileClick()
                },
                onSettingsClick = {
                    scope.launch { drawerState.close() }
                    onSettingsClick()
                }
            )


        }

    ) {
        // ✅ Senin mevcut Home UI aynen burada
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE9E2F2))
        ) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 14.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = HomePurple)
                }
            }

            // ⚠️ Bu spacer Column içinde width olduğu için etkisiz, istersen height yap
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // ✅ Menü ikonuna basınca Drawer aç
                    IconButton(
                        onClick = {
                            scope.launch { drawerState.open() }
                            onMenuClick()
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = HomePurple)
                    }

                    IconButton(onClick = onFilterClick) {
                        Icon(
                            imageVector = Icons.Outlined.FilterAlt,
                            contentDescription = "Filter",
                            tint = HomePurple
                        )
                    }
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "YediMap",
                        color = HomePurple,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.offset(x = (-15).dp)
                    )
                }

                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = "Profile",
                        tint = HomePurple,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // ===== RESİM =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(465.dp)
                    .offset(y = (-60).dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home_bg),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // ===== BUTONLAR =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-95).dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeActionCard(text = "Major", modifier = Modifier.weight(1f))
                    HomeActionCard(text = "Ring Stop", modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeActionCard(text = "Floors", modifier = Modifier.weight(1f))
                    HomeActionCard(text = "Cafeteria", modifier = Modifier.weight(1f),onClick = onCafeteriaClick)
                }
            }
        }
    }
}

@Composable
private fun HomeActionCard(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .height(90.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        color = HomePurple,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
