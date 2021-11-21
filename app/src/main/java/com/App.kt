package com

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.io.whatsapp.BuildConfig
import timber.log.Timber


private const val SHARED_PREFS_KEY = "whatsapp_clone_preferences"
private const val AUTH_TOKEN_KEY = "auth_token"
private const val PHONE_NUMBER_KEY = "phone_number"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {})
        }
    }

    companion object {

        private lateinit var instance: App

        val sharedPrefs: SharedPreferences by lazy {
            instance.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        }


        fun saveAuthToken(token: String) {
            sharedPrefs
                .edit()
                .putString(AUTH_TOKEN_KEY, token)
                .apply()
        }

        fun getAuthToken() = sharedPrefs.getString(AUTH_TOKEN_KEY, null)

        fun removeAuthToken() = sharedPrefs.edit().remove(AUTH_TOKEN_KEY).apply()

        fun savePhoneNumber(phoneNumber:String){
            sharedPrefs
                .edit()
                .putString(PHONE_NUMBER_KEY, phoneNumber)
                .apply()
        }

        fun getPhoneNumber() = sharedPrefs.getString(PHONE_NUMBER_KEY, null)

        fun removePhoneNumber() = sharedPrefs.edit().remove(PHONE_NUMBER_KEY).apply()

        fun initGetStreamUser(context: Context) {


        }

    }
}