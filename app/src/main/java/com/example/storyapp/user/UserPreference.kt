package com.example.storyapp.user

import android.content.Context

class UserPreference(context:Context){
    val preference = context.getSharedPreferences(PREF_LOGIN,Context.MODE_PRIVATE)

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
        preferenceEdit.putBoolean(LOGIN_STATE, false)
        preferenceEdit.apply()
    }

    companion object{
        const val PREF_LOGIN = "login"
        const val NAME = "name"
        const val TOKEN = "token"
        const val USER_ID = "userId"
        const val LOGIN_STATE = "login_state"
    }

}