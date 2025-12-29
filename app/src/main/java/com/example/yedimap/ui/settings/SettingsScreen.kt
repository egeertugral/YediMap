package com.example.yedimap.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgPurple = Color(0xFFE9E2F2)
private val HomePurple = Color(0xFF614184)

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPurple)
            .padding(horizontal = 18.dp)
    ) {

        // 🔹 Top Row: Back + Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = HomePurple
                )
            }

            Text(
                text = "Settings",
                color = HomePurple,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // 🔹 Filter (Settings başlığının ALTINDA – sağda)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, end = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.FilterAlt,
                contentDescription = "Filter",
                tint = HomePurple,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Filter",
                color = HomePurple,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        SettingsItem(title = "About YediMap", onClick = onAboutClick)
        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(title = "Profile Information")
        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(title = "Schedule")
        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(title = "Language")
        Spacer(modifier = Modifier.height(24.dp))

        SettingsItem(title = "Log Out")
    }
}


@Composable
private fun SettingsItem(
    title: String,
    onClick: () -> Unit = {} // şimdilik pasif
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        color = HomePurple,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = "Go",
                tint = Color.White
            )
        }
    }
}
