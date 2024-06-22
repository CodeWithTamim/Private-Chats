package com.cwtstudio.privatechats.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cwtstudio.privatechats.R
import com.cwtstudio.privatechats.repository.RequestManager

class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        RequestManager.registerUser(this)
    }
}