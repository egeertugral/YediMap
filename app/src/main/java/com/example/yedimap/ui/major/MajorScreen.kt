package com.example.yedimap.ui.major

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yedimap.R

private val HomePurple = Color(0xFF614184)
private val BgPurple = Color(0xFFE9E2F2)

@Composable
fun MajorScreen(
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BgPurple,               // üst
                        BgPurple.copy(alpha = 0.90f),
                        HomePurple.copy(alpha = 0.55f),
                        HomePurple.copy(alpha = 0.85f) // alt
                    )
                )
            )
    ) {
        // ÜST içerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            // Top row: Back
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

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = "Faculty Departments",
                color = HomePurple,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))


            // Card list (mor kutu)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                color = HomePurple,
                shape = RoundedCornerShape(18.dp),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DepartmentItem("Journalism")
                    DepartmentItem("Advertising Design and Communication")
                    DepartmentItem("Public Relations and Promotion")
                    DepartmentItem("Radio, Television and Cinema")
                    DepartmentItem("Visual Communication Design")
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // ALT görsel (png)
        Image(
            painter = painterResource(id = R.drawable.major_bg), // ✅ png adın bu olsun
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun DepartmentItem(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 14.5.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}
