package com.example.yedimap.ui.cafeteria

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yedimap.R

private val HomePurple = Color(0xFF614184)
private val BgPurple = Color(0xFFE9E2F2)

@Composable
fun CafeteriaScreen(
    onBackClick: () -> Unit = {}
) {
    // ✅ FULL SCREEN GRADIENT (altta mor daha belirgin)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFDFBFF), // üst: neredeyse beyaz
                        Color(0xFFF3EEF9), // orta: çok hafif mor
                        HomePurple            // alt: senin mor (#E9E2F2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Back
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = HomePurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "Cafeteria",
                    color = HomePurple,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Floor 4 button (pasif)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(HomePurple, shape = RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Floor 4",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom image
            Image(
                painter = painterResource(id = R.drawable.cafeteria_bg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
