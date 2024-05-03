package com.example.storyapp.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class UserPreference @Inject constructor(@ApplicationContext val context:Context){

    private val dataStore = context.dataStore

   suspend fun setUser(user:UserModel){
        dataStore.edit{ preference ->
            preference[NAME] = user.name
            preference[TOKEN] = user.token
            preference[USER_ID] = user.userId
            preference[IS_LOGIN] = user.isLogin
        }
    }

    fun getUser():Flow<UserModel> {
        return dataStore.data.map { preference ->
            UserModel(
                preference[NAME] ?:"",
                preference[TOKEN] ?:"",
                preference[USER_ID] ?:"",
                preference[IS_LOGIN] ?: false
            )
        }
    }

    suspend fun logout(){
        dataStore.edit { preference ->
            preference[NAME] = ""
            preference[USER_ID] = ""
            preference[TOKEN] = ""
            preference[IS_LOGIN] = false
        }
    }

    fun getToken():Flow<String?>{
        return dataStore.data.map { preference->
            preference[TOKEN]
        }
    }

    companion object{
        private val IS_LOGIN = booleanPreferencesKey("login_state")
        private val NAME = stringPreferencesKey("name")
        private val TOKEN = stringPreferencesKey("token")
        private val USER_ID = stringPreferencesKey("userId")

    }

}