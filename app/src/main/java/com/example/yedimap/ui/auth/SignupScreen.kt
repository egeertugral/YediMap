package com.example.yedimap.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yedimap.R
import com.example.yedimap.ui.auth.model.User

private val HomePurple = Color(0xFF614184)

@Composable
fun SignupScreen(
    onBack: () -> Unit,
    onSignupSuccess: () -> Unit,
    vm: AuthViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var studentNo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var passVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.login_bg), // aynı bg kullan
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(14.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                SimpleField(value = fullName, onValueChange = { fullName = it; error = null }, placeholder = "Name Surname")
                SimpleField(value = email, onValueChange = { email = it; error = null }, placeholder = "Email")
                SimpleField(value = faculty, onValueChange = { faculty = it; error = null }, placeholder = "Faculty")
                SimpleField(value = phone, onValueChange = { phone = it; error = null }, placeholder = "Phone number")
                SimpleField(value = studentNo, onValueChange = { studentNo = it; error = null }, placeholder = "Student No")

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; error = null },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Password") },
                    trailingIcon = {
                        Text(
                            text = if (passVisible) "Hide" else "Show",
                            color = HomePurple,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { passVisible = !passVisible }
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HomePurple,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.White.copy(alpha = 0.55f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.55f)
                    )
                )

                if (error != null) {
                    Text(
                        text = error!!,
                        color = Color(0xFFB00020),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        // basit zorunlu alan kontrolü
                        if (fullName.isBlank() || email.isBlank() || faculty.isBlank()
                            || phone.isBlank() || studentNo.isBlank() || password.isBlank()
                        ) {
                            error = "All fields are required."
                            return@Button
                        }

                        vm.signUp(
                            user = User(
                                fullName = fullName.trim(),
                                email = email.trim(),
                                faculty = faculty.trim(),
                                phone = phone.trim(),
                                studentNo = studentNo.trim(),
                                password = password
                            ),
                            onSuccess = onSignupSuccess, // login'e dön
                            onError = { error = it }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HomePurple),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Sign Up", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SimpleField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HomePurple,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.White.copy(alpha = 0.55f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.55f)
        )
    )
}
