package com.cwtstudio.privatechats.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cwtstudio.privatechats.R
import com.cwtstudio.privatechats.model.Chat
import com.google.android.material.card.MaterialCardView

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
class ChatAdapter(private val context: Context, private val username: String, private val chats: List<Chat>) :
    Adapter<ChatAdapter.ChatViewHolder>()
{

    inner class ChatViewHolder(itemView: View) : ViewHolder(itemView)
    {
        val txtChat: TextView = itemView.findViewById(R.id.txtChat)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardViewMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder
    {
        return ChatViewHolder(
            LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int)
    {
        val layoutParams = holder.cardView.layoutParams as ConstraintLayout.LayoutParams
        holder.txtChat.text = chats[position].message
        if (username == chats[position].senderUsername)
        {
            holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.sent_message))
            holder.txtChat.setTextColor(context.resources.getColor(R.color.white))
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.startToStart = ConstraintLayout.LayoutParams.UNSET

        } else
        {
            holder.cardView.setCardBackgroundColor(Color.WHITE)
            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.UNSET
        }
    }

    override fun getItemCount(): Int
    {
        return chats.size
    }
}