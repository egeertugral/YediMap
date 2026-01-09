package com.example.yedimap.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val HomePurple = Color(0xFF614184)
private val BgPurple = Color(0xFFE9E2F2)

@Composable
fun FeedbackScreen(
    onClose: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPurple)
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Top-left X
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = HomePurple
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))


        // Title area
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Feedback",
                color = HomePurple,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "How was your\nexperience with\nYediMap?",
                color = HomePurple,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(22.dp))
        Spacer(modifier = Modifier.height(22.dp))



        // Stars row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            (1..5).forEach { star ->
                IconButton(onClick = { rating = star }) {
                    if (star <= rating) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star $star",
                            tint = HomePurple,
                            modifier = Modifier.size(50.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = "Star $star",
                            tint = HomePurple,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        Spacer(modifier = Modifier.height(18.dp))


        // Comment box
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text("Tell us more about your experience...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HomePurple,
                unfocusedBorderColor = HomePurple.copy(alpha = 0.35f),
                cursorColor = HomePurple
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Submit button
        Button(
            onClick = {
                // istersen: rating==0 veya comment boşsa validasyon ekleriz
                onSubmit()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HomePurple),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Submit", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(18.dp))
        Spacer(modifier = Modifier.height(18.dp))

    }
}
