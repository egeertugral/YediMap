package com.example.yedimap.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yedimap.R

private val HomePurple = Color(0xFF614184)

@Composable
fun HomeScreen(
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9E2F2)) // ekrandaki açık mor arka plan hissi
    ) {

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 14.dp,      // 👈 geri butonu ile üst arası boşluk
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = HomePurple)
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = HomePurple)
                }
                IconButton(onClick = onFilterClick) {
                    Icon(imageVector = Icons.Outlined.FilterAlt, contentDescription = "Filter", tint = HomePurple)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "YediMap",
                    color = HomePurple,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.offset(x = (-15).dp) // 👈 sola kaydır
                )
            }

            IconButton(onClick = onProfileClick) {
                Icon(imageVector = Icons.Outlined.PersonOutline,
                    contentDescription = "Profile",
                    tint = HomePurple, modifier = Modifier.size(26.dp))
            }
        }

        // Background image area


            // ===== RESİM =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(465.dp)
                    .offset(y = (-60).dp) // 👈 resmi yukarı al
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

// Spacer kaldırıldı (veya 0 yapabilirsin)
// Spacer(modifier = Modifier.height(16.dp))

// ===== BUTONLAR (RESMİN ALTINDA ama yukarı çekildi) =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-95).dp) // ✅ butonlar aşağı kaymaz
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
                    HomeActionCard(text = "Cafeteria", modifier = Modifier.weight(1f))
                }
            }
        }
        }


@Composable
private fun HomeActionCard(
    text: String,
    modifier: Modifier = Modifier
) {
    // Şimdilik PASİF: clickable yok, enabled yok.
    Surface(
        modifier = modifier.height(90.dp),
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
