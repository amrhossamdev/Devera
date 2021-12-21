package com.learnawy.app.storage

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.devera.app.ui.signIn.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AppReferences {

    fun setLoginState(context: Activity?, state: Boolean) {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("login", state)
        editor.apply()
    }

    fun getLoginState(context: Activity?): Boolean {
        val sharedPreferences: SharedPreferences =
            context!!.getSharedPreferences("login", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("login", false)
    }


    fun setUserData(context: Context, allData: User?) {
        val appSharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(context.applicationContext)
        val prefsEditor = appSharedPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(allData)
        prefsEditor.putString("user", json)
        prefsEditor.apply()
    }

    fun getUserData(context: Context): User? {
        val appSharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(context.applicationContext)
        val gson = Gson()
        val json = appSharedPrefs.getString("user", "")
        val type = object : TypeToken<User>() {}.type
        return gson.fromJson<User>(json, type)
    }


}