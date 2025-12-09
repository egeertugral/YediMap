package com.example.yedimap.ui.language

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LanguageSelectionScreen(
    onEnglishClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        // YediMap Title
        Text(
            text = "YediMap",
            color = Color(0xFF614184),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )

        Spacer(modifier = Modifier.height(280.dp))

        Text(
            text = "Welcome to YediMap",
            color = Color(0xFF333333),
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "The official guide to Yeditepe\nUniversity campus.",
            textAlign = TextAlign.Center,
            color = Color(0xFF666666),
            fontSize = 19.sp
        )

        Spacer(modifier = Modifier.height(80.dp))

        // Turkish (disabled)
        Button(
            onClick = { /* No action */ },
            enabled = false,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF614184),
                disabledContainerColor = Color(0xFF614184),
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Türkçe", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // English
        Button(
            onClick = { onEnglishClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF009A44),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "English", fontSize = 16.sp)
        }
    }
}