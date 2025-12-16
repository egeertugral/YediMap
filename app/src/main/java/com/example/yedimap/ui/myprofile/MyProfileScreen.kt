package com.example.yedimap.ui.myprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yedimap.R

private val ProfilePurple = Color(0xFF614184)

@Composable
fun MyProfileScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = ProfilePurple
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "My Profile",
            color = ProfilePurple,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_placeholder),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
            )

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .border(
                        width = 4.dp,
                        color = ProfilePurple,
                        shape = CircleShape
                    )
            )

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit",
                tint = ProfilePurple,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        ProfileField(title = "NAME", value = "Selin Yılmaz")
        ProfileField(title = "EMAIL", value = "selin.yilmaz@std.yeditepe.edu.tr")
        ProfileField(title = "FACULTY", value = "Faculty of Fine Arts")
        ProfileField(title = "PHONE", value = "+90 532 654 21 89")
    }
}

@Composable
private fun ProfileField(title: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Divider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(20.dp))
    }
}