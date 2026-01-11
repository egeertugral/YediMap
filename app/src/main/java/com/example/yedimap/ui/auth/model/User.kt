package com.example.yedimap.ui.auth.model

data class User(
    val fullName: String,
    val email: String,
    val faculty: String,
    val phone: String,
    val studentNo: String,
    val password: String // demo amaçlı (prod'da saklanmaz)
)
