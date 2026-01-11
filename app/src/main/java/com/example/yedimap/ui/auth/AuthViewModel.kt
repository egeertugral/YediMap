package com.example.yedimap.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yedimap.data.local.UserPrefs
import com.example.yedimap.ui.auth.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = UserPrefs(app.applicationContext)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        viewModelScope.launch {
            _currentUser.value = prefs.userFlow().first()
        }
    }

    fun refreshUser() {
        viewModelScope.launch { _currentUser.value = prefs.userFlow().first() }
    }

    fun signUp(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            // basit validasyon
            if (user.fullName.isBlank() || user.email.isBlank() || user.faculty.isBlank()
                || user.phone.isBlank() || user.studentNo.isBlank() || user.password.isBlank()
            ) {
                onError("All fields are required.")
                return@launch
            }
            prefs.saveUser(user)
            prefs.setLoggedIn(false)
            _currentUser.value = user
            onSuccess()
        }
    }

    fun login(usernameOrEmail: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val ok = prefs.validateLogin(usernameOrEmail, password).first()
            if (!ok) {
                onError("Invalid credentials.")
                return@launch
            }
            prefs.setLoggedIn(true)
            _currentUser.value = prefs.userFlow().first()
            onSuccess()
        }
    }

    fun logout(onDone: () -> Unit = {}) {
        viewModelScope.launch {
            prefs.setLoggedIn(false)
            onDone()
        }
    }
}
