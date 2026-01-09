package com.example.yedimap.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val HomePurple = Color(0xFF614184)

@Composable
fun ThankYouScreen(
    onDone: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        delay(2200) // 2.2 sn
        onDone()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomePurple),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Thank You!",
                color = Color.White,
                fontSize = 38.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Submission successful.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 20.sp
            )
        }
    }
}
