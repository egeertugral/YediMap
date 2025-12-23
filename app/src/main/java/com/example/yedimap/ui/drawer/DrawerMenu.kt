package com.example.yedimap.ui.drawer

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yedimap.R

private val DrawerPurple = Color(0xFF614184)

@Composable
fun DrawerMenu(
    modifier: Modifier = Modifier,
    name: String = "Kutay Büyükboyacı",
    email: String = "kutay.buyukboyaci@std.yeditepe.edu.tr",
    onClose: () -> Unit = {},
    onMyProfileClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(320.dp)
            .background(DrawerPurple)
            .padding(top = 24.dp, start = 18.dp, end = 18.dp, bottom = 18.dp)
    ) {

        // Header (Avatar + Name + Email)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_placeholder), // yoksa sonra ekleriz
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = email,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp
                )
            }

            // sağ taraftaki "ok/kapama" görseli (pasif)
            Box(
                modifier = Modifier
                    .height(44.dp)
                    .width(34.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "‹", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.White.copy(alpha = 0.25f))
        Spacer(modifier = Modifier.height(16.dp))

        // Menu items (PASİF)
        DrawerItem(icon = Icons.Outlined.Home, title = "Home")
        Spacer(modifier = Modifier.height(12.dp))

        DrawerItem(
            icon = Icons.Outlined.PersonOutline,
            title = "My Profile",
            onClick = {
                Log.d("DrawerMenu", "My Profile clicked")
                onMyProfileClick()
                onClose() // drawer kapanması için
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        DrawerItem(icon = Icons.Outlined.BookmarkBorder, title = "Saved Places")
        Spacer(modifier = Modifier.height(12.dp))

        DrawerItem(icon = Icons.Outlined.Settings, title = "Settings")

        Spacer(modifier = Modifier.weight(1f))

        Divider(color = Color.White.copy(alpha = 0.18f))
        Spacer(modifier = Modifier.height(14.dp))

        // Logout (PASİF)
        DrawerItem(icon = Icons.Outlined.Logout, title = "Log Out")
    }
}

@Composable
private fun DrawerItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
