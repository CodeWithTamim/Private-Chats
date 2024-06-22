package com.cwtstudio.privatechats.util

import android.app.Application
import com.tencent.mmkv.MMKV

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
class MainApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        MMKV.initialize(this)
    }
}