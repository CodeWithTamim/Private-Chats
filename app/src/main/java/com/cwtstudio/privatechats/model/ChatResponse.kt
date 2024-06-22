package com.cwtstudio.privatechats.model

import com.google.gson.annotations.SerializedName

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
data class ChatResponse(
    val success: Boolean,
    val message: String,
    val chats: List<Chat>
)

data class Chat(
    val id: Int? = null,
    @SerializedName("receiver_username")
    val receiverUsername: String,
    @SerializedName("sender_username")
    val senderUsername: String,
    val message: String,
)

