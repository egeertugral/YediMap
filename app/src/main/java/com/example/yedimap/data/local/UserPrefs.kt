package com.example.yedimap.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.yedimap.ui.auth.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPrefs(private val context: Context) {

    private object Keys {
        val FULL_NAME = stringPreferencesKey("full_name")
        val EMAIL = stringPreferencesKey("email")
        val FACULTY = stringPreferencesKey("faculty")
        val PHONE = stringPreferencesKey("phone")
        val STUDENT_NO = stringPreferencesKey("student_no")
        val PASSWORD = stringPreferencesKey("password")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { p ->
            p[Keys.FULL_NAME] = user.fullName
            p[Keys.EMAIL] = user.email
            p[Keys.FACULTY] = user.faculty
            p[Keys.PHONE] = user.phone
            p[Keys.STUDENT_NO] = user.studentNo
            p[Keys.PASSWORD] = user.password
        }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { p -> p[Keys.IS_LOGGED_IN] = isLoggedIn }
    }

    fun isLoggedIn(): Flow<Boolean> =
        context.dataStore.data.map { it[Keys.IS_LOGGED_IN] ?: false }

    fun userFlow(): Flow<User?> =
        context.dataStore.data.map { p ->
            val fullName = p[Keys.FULL_NAME] ?: return@map null
            val email = p[Keys.EMAIL] ?: return@map null
            val faculty = p[Keys.FACULTY] ?: ""
            val phone = p[Keys.PHONE] ?: ""
            val studentNo = p[Keys.STUDENT_NO] ?: ""
            val password = p[Keys.PASSWORD] ?: ""
            User(fullName, email, faculty, phone, studentNo, password)
        }

    fun validateLogin(usernameOrEmail: String, password: String): Flow<Boolean> =
        context.dataStore.data.map { p ->
            val storedEmail = p[Keys.EMAIL] ?: return@map false
            val storedStudentNo = p[Keys.STUDENT_NO] ?: ""
            val storedPass = p[Keys.PASSWORD] ?: return@map false

            val userMatch = (usernameOrEmail == storedEmail) || (usernameOrEmail == storedStudentNo)
            userMatch && password == storedPass
        }
}
