package com.cwtstudio.privatechats.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ANDROID_ID

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
object Constants
{
    const val BASE_URL = "http://192.168.0.107/"
    const val REGISTER_USER = "chat_app/api/v1/register_user.php"
    const val CHECK_USER = "chat_app/api/v1/check_user.php"
    const val CHECK_USERNAME = "chat_app/api/v1/check_username.php"
    const val GET_ALL_CHATS = "chat_app/api/v1/get_all_chats.php"
    const val SEND_CHAT = "chat_app/api/v1/send_chat.php"
    const val API_KEY = "Hacker"

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String
    {
        return Secure.getString(context.contentResolver, ANDROID_ID)
    }


}