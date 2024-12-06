package com.submission.storyapps.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun saveLoginSession(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN", token)
        editor.putBoolean("IS_LOGGED_IN", true)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    fun clearLoginSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}