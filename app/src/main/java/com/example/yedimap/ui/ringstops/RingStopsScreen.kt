package com.example.yedimap.ui.ringstops

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
fun RingStopsScreen(
    onBackClick: () -> Unit = {}
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(BgPurple, HomePurple)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
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

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ring Stops",
                    color = HomePurple,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Buttons list (pasif)
            RingStopButton(text = "Ring Stop1")
            Spacer(modifier = Modifier.height(10.dp))
            RingStopButton(text = "Ring Stop2")
            Spacer(modifier = Modifier.height(10.dp))
            RingStopButton(text = "Ring Stop3")
            Spacer(modifier = Modifier.height(10.dp))
            RingStopButton(text = "Ring Stop4")
            Spacer(modifier = Modifier.height(10.dp))
            RingStopButton(text = "Ring Stop5")
            Spacer(modifier = Modifier.height(10.dp))
            RingStopButton(text = "Ring Stop6")
            Spacer(modifier = Modifier.height(10.dp))
            RingStopButton(text = "Ring Stop7")
            Spacer(modifier = Modifier.height(10.dp))
            RingStopButton(text = "Ring Stop8")

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom image
            Image(
                painter = painterResource(id = R.drawable.ring_stops_bg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun RingStopButton(
    text: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        color = HomePurple,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}