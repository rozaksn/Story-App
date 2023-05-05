package com.example.storyapp.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(context:Context){
    val preference = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)

    fun setUser(user:UserModel){
        val preferenceEdit = preference.edit()
        preferenceEdit.putString(NAME,user.name)
        preferenceEdit.putString(TOKEN,user.token)
        preferenceEdit.putString(USER_ID,user.userId)
        preferenceEdit.putBoolean(LOGIN_STATE,user.isLogin)
        preferenceEdit.apply()
    }

    fun getUser():UserModel{
        return UserModel(
            preference.getString(NAME,"")?:"",
            preference.getString(TOKEN,"")?:"",
            preference.getString(USER_ID,"")?:"",
            preference.getBoolean(LOGIN_STATE,false)
        )
    }

    fun logout(){
        val preferenceEdit = preference.edit()
        preferenceEdit.remove(NAME)
        preferenceEdit.remove(TOKEN)
        preferenceEdit.putBoolean(LOGIN_STATE,false)
        preferenceEdit.apply()
    }

    companion object{
        const val PREF_NAME = "login"
        const val NAME = "name"
        const val TOKEN = "token"
        const val USER_ID = "userId"
        const val LOGIN_STATE = "login_state"
    }

}