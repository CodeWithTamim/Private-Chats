package com.cwtstudio.privatechats.util

import com.cwtstudio.privatechats.model.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
object MMKVM
{
    private const val USER_KEY = "USER_DATA"
    private val mmkv by lazy {
        MMKV.defaultMMKV()
    }

    fun encodeUser(value: User)
    {
        mmkv.encode(USER_KEY, Gson().toJson(value))
    }

    fun decodeUser(): User
    {
        return Gson().fromJson(mmkv.decodeString(USER_KEY), User::class.java)
    }

}