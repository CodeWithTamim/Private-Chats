package com.cwtstudio.privatechats.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cwtstudio.privatechats.R
import com.cwtstudio.privatechats.adapter.ChatAdapter
import com.cwtstudio.privatechats.databinding.ActivityChatBinding
import com.cwtstudio.privatechats.model.Chat
import com.cwtstudio.privatechats.repository.RequestManager
import com.cwtstudio.privatechats.util.MMKVM

class ChatActivity : AppCompatActivity()
{
    private val binding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: ChatAdapter
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var refreshRunnable: Runnable

    private var lastChatList: List<Chat> = listOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }
        val receiver: String = intent.getStringExtra("receiver_name") ?: return
        setupRefreshRunnable(receiver)
        handler.post(refreshRunnable)

        binding.btnSendChat.setOnClickListener {
            val message = binding.edtChat.text.toString()
            if (message.isEmpty())
            {
                Toast.makeText(this, "Message cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.edtChat.setText("")
            RequestManager.sendMessage(Chat(null, receiver, MMKVM.decodeUser().username!!, message),
                object : RequestManager.OnChatsResponseListener
                {
                    override fun didFetch(success: Boolean, msg: String)
                    {
                        if (success)
                        {
                            refreshChats(receiver)
                            Toast.makeText(this@ChatActivity, msg, Toast.LENGTH_SHORT).show()
                        } else
                        {
                            Toast.makeText(this@ChatActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun didError(msg: String)
                    {
                        Toast.makeText(this@ChatActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun setupRefreshRunnable(receiver: String)
    {
        refreshRunnable = object : Runnable
        {
            override fun run()
            {
                refreshChats(receiver)
                handler.postDelayed(this, 2000)
            }
        }
    }

    private fun refreshChats(receiver: String)
    {
        val currentUsername = MMKVM.decodeUser().username!!
        RequestManager.getAllChats(
            currentUsername,
            receiver,
            object : RequestManager.OnChatsListener
            {
                override fun didFetch(chats: List<Chat>, msg: String)
                {
                    if (chats != lastChatList)
                    {
                        lastChatList = chats
                        adapter = ChatAdapter(this@ChatActivity, currentUsername, chats)
                        binding.rvChats.adapter = adapter
                        binding.rvChats.scrollToPosition(chats.size - 1)
                    }
                }

                override fun didError(msg: String)
                {
                    Toast.makeText(this@ChatActivity, msg, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDestroy()
    {
        super.onDestroy()
        handler.removeCallbacks(refreshRunnable)
    }
}