package com.example.yedimap.ui.floors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
fun FloorsScreen(
    onBackClick: () -> Unit = {}
) {
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
    )

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

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "Floors",
                color = HomePurple,
                fontSize = 30.sp,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Buttons (pasif)
        FloorsButton("Floor 1")
        Spacer(modifier = Modifier.height(12.dp))
        FloorsButton("Floor 2")
        Spacer(modifier = Modifier.height(12.dp))
        FloorsButton("Floor 3")
        Spacer(modifier = Modifier.height(12.dp))
        FloorsButton("Floor 4")
        Spacer(modifier = Modifier.height(12.dp))
        FloorsButton("Floor 5")
        Spacer(modifier = Modifier.height(12.dp))
        FloorsButton("Floor 6")
        Spacer(modifier = Modifier.height(12.dp))
        FloorsButton("Floor 7")
        Spacer(modifier = Modifier.height(12.dp))
        FloorsButton("Floor 8")

        Spacer(modifier = Modifier.height(10.dp))

        // Bottom image
        Image(
            painter = painterResource(id = R.drawable.floors_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun FloorsButton(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(HomePurple, shape = RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}