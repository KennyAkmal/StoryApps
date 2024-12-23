package com.submission.storyapps.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun saveLoginSession(token: String, name: String) {
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN", token)
        editor.putString("USER_NAME", name)
        editor.putBoolean("IS_LOGGED_IN", true)
        editor.apply()

        Log.d("SessionManager", "Saved user name: $name")
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }

    fun getUserName(): String {
        val userName = sharedPreferences.getString("USER_NAME", "") ?: ""
        Log.d("SessionManager", "Retrieved user name: $userName")
        return userName
    }

    fun getToken(): String {
        val token = sharedPreferences.getString("TOKEN", "") ?: ""
        Log.d("SessionManager", "Retrieved token: $token")
        return token
    }

    fun clearLoginSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}