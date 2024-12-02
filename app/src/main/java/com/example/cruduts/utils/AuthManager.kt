package com.example.cruduts.utils

import android.content.Context

class AuthManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Save user credentials during registration
    fun registerUser(username: String, password: String): Boolean {
        if (prefs.contains(username)) {
            return false // Username already exists
        }
        prefs.edit()
            .putString(username, password)
            .apply()
        return true
    }

    // Validate user credentials during login
    fun loginUser(username: String, password: String): Boolean {
        val storedPassword = prefs.getString(username, null)
        return storedPassword != null && storedPassword == password
    }

    // Check if a user is logged in
    fun isLoggedIn(): Boolean {
        return prefs.contains("logged_in_user")
    }

    // Set the currently logged-in user
    fun setLoggedInUser(username: String) {
        prefs.edit()
            .putString("logged_in_user", username)
            .apply()
    }

    // Get the currently logged-in user
    fun getLoggedInUser(): String? {
        return prefs.getString("logged_in_user", null)
    }

    // Log out the current user
    fun logout() {
        prefs.edit()
            .remove("logged_in_user")
            .apply()
    }
}
