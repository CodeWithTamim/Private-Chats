package com.cwtstudio.privatechats.model

import com.google.gson.annotations.SerializedName

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
data class UserResponse(
    val success: Boolean,
    val message: String,
    val user: User
)

data class User(
    val id: Int? = null,
    val username: String? = null,
    @SerializedName("device_id")
    val deviceId: String? = null
)
