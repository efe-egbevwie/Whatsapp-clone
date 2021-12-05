package com

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.io.whatsapp.BuildConfig
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.offline.ChatDomain
import timber.log.Timber


private const val SHARED_PREFS_KEY = "whatsapp_clone_preferences"
private const val AUTH_TOKEN_KEY = "auth_token"
private const val PHONE_NUMBER_KEY = "phone_number"
private const val USER_NAME_KEY = "user_name"

class App : Application() {

    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        context = applicationContext
        initGetStreamUser(context)

    }

    companion object {

        private lateinit var instance: App

        private val sharedPrefs: SharedPreferences by lazy {
            instance.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        }


        fun saveUid(token: String) {
            2
            sharedPrefs
                .edit()
                .putString(AUTH_TOKEN_KEY, token)
                .apply()
        }

        fun getUid() = sharedPrefs.getString(AUTH_TOKEN_KEY, null)

        fun removeUid() = sharedPrefs.edit().remove(AUTH_TOKEN_KEY).apply()

        fun savePhoneNumber(phoneNumber: String) {
            sharedPrefs
                .edit()
                .putString(PHONE_NUMBER_KEY, phoneNumber)
                .apply()
        }

        fun getPhoneNumber() = sharedPrefs.getString(PHONE_NUMBER_KEY, null)

        fun removePhoneNumber() = sharedPrefs.edit().remove(PHONE_NUMBER_KEY).apply()

        fun saveUserName(userName: String) {
            sharedPrefs
                .edit()
                .putString(USER_NAME_KEY, userName)
                .apply()
        }

        fun getUserName() = sharedPrefs.getString(USER_NAME_KEY, null)

        fun removeUserName() = sharedPrefs.edit().remove(USER_NAME_KEY).apply()

        fun initGetStreamUser(context: Context) {

            if (getPhoneNumber().isNullOrBlank()) return

            val chatClient = ChatClient.Builder(apiKey = "yek6cvxkzdkt", context)
                .logLevel(ChatLogLevel.ALL)
                .build()

            ChatDomain.Builder(chatClient, context).build()

            if (getUserName().isNullOrBlank()) return
            if (getUid().isNullOrBlank()) return
            if (getPhoneNumber().isNullOrBlank()) return

            val user = User(
                id = getPhoneNumber().toString(),
                extraData = mutableMapOf(
                    "name" to getUserName().toString()
                )
            )

            val token = ChatClient.instance().devToken(getUid().toString())

            val client = ChatClient.instance()
            client.connectUser(user, token).enqueue() { result ->
                if (result.isSuccess) {
                    Timber.i("chat user connected with ${result.data()}")
                } else {
                    Timber.i("chat user connection failed with ${result.error()}")
                }

            }

        }

    }
}