package com.cwtstudio.privatechats.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cwtstudio.privatechats.R
import com.cwtstudio.privatechats.databinding.ActivityMainBinding
import com.cwtstudio.privatechats.repository.RequestManager
import com.cwtstudio.privatechats.util.MMKVM

class MainActivity : AppCompatActivity()
{
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val user = MMKVM.decodeUser()

        binding.txtUsername.text = user.username
        binding.btnCopy.setOnClickListener {
            val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val cd = ClipData.newPlainText("username", user.username)
            cm.setPrimaryClip(cd)
            Toast.makeText(this, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
        }

        binding.btnStartChat.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            if (username.isEmpty())
            {
                Toast.makeText(this, "Username cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (username.contains(" "))
            {
                Toast.makeText(this, "Username cannot contain space.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (username == user.username)
            {
                Toast.makeText(this, "Cannot message yourself.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RequestManager.checkUsername(username, object : RequestManager.OnUserResponseListener
            {
                override fun didFetch(success: Boolean, msg: String)
                {
                    if (success)
                    {
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, ChatActivity::class.java)
                        intent.putExtra("receiver_name", username)
                        startActivity(intent)
                    } else
                    {
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun didError(msg: String)
                {
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                }

            })


        }


    }
}