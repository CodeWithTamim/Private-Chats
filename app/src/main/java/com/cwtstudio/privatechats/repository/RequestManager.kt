package com.cwtstudio.privatechats.repository

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.cwtstudio.privatechats.R
import com.cwtstudio.privatechats.model.Chat
import com.cwtstudio.privatechats.model.ChatResponse
import com.cwtstudio.privatechats.model.Clients
import com.cwtstudio.privatechats.model.SubmitResponse
import com.cwtstudio.privatechats.model.User
import com.cwtstudio.privatechats.model.UserResponse
import com.cwtstudio.privatechats.ui.MainActivity
import com.cwtstudio.privatechats.util.Constants
import com.cwtstudio.privatechats.util.MMKVM
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
object RequestManager
{
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val service by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun registerUser(context: Context)
    {
        // create the dialog
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_register)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(R.drawable.blank_bg)
        val edtUsername: TextInputEditText = dialog.findViewById(R.id.edtUsername)
        val btnSubmit: MaterialButton = dialog.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            if (edtUsername.text.toString().isEmpty())
            {
                Toast.makeText(context, "Please enter a username.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (edtUsername.text.toString().contains(" "))
            {
                Toast.makeText(context, "Username cannot contain space.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val username = edtUsername.text.toString().trim().lowercase()
            val call = service.registerUser(
                Constants.API_KEY,
                User(null, username, Constants.getAndroidId(context))
            )
            call.enqueue(
                object : Callback<UserResponse>
                {
                    override fun onFailure(call: Call<UserResponse>, t: Throwable)
                    {
                        Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    )
                    {
                        if (!response.isSuccessful && response.body() == null)
                        {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                            return
                        }
                        if (response.body()!!.success)
                        {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT)
                                .show()
                            MMKVM.encodeUser(response.body()!!.user)
                            context.startActivity(Intent(context, MainActivity::class.java))
                            dialog.dismiss()
                            return
                        } else
                        {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            )
        }

        val callCheck =
            service.checkUser(Constants.API_KEY, User(null, null, Constants.getAndroidId(context)))
        callCheck.enqueue(
            object : Callback<UserResponse>
            {
                override fun onFailure(call: Call<UserResponse>, t: Throwable)
                {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                )
                {
                    if (!response.isSuccessful && response.body() == null)
                    {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        return
                    }

                    if (response.body()!!.success)
                    {
                        MMKVM.encodeUser(response.body()!!.user)
                        context.startActivity(Intent(context, MainActivity::class.java))
                    } else
                    {
                        dialog.show()
                    }
                }
            }
        )
    }

    fun checkUsername(username: String, listener: OnUserResponseListener)
    {

        val callCheck =
            service.checkUsername(Constants.API_KEY, User(null, username = username, null))
        callCheck.enqueue(
            object : Callback<UserResponse>
            {
                override fun onFailure(call: Call<UserResponse>, t: Throwable)
                {
                    listener.didError(t.message.toString())
                }

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                )
                {
                    if (!response.isSuccessful && response.body() == null)
                    {
                        listener.didError(response.message())
                        return
                    }
                    listener.didFetch(response.body()!!.success, response.body()!!.message)
                }
            }
        )

    }

    fun getAllChats(sender: String, receiver: String, listener: OnChatsListener)
    {
        val call = service.getAllChats(
            Constants.API_KEY,
            Clients(sender, receiver)
        )

        call.enqueue(object : Callback<ChatResponse>
        {
            override fun onFailure(call: Call<ChatResponse>, t: Throwable)
            {
                listener.didError(t.message.toString())
            }

            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>)
            {
                if (!response.isSuccessful && response.body() == null)
                {
                    listener.didError(response.message())
                    return
                }
                listener.didFetch(response.body()!!.chats, response.message())
            }

        })


    }

    fun sendMessage(chat: Chat, listener: OnChatsResponseListener)
    {
        val call = service.sendMessage(Constants.API_KEY, chat)
        call.enqueue(object : Callback<SubmitResponse>
        {
            override fun onFailure(call: Call<SubmitResponse>, t: Throwable)
            {
                listener.didError(t.message.toString())
            }

            override fun onResponse(call: Call<SubmitResponse>, response: Response<SubmitResponse>)
            {
                if (!response.isSuccessful && response.body() == null)
                {
                    listener.didError(response.message())
                    return
                }
                listener.didFetch(response.body()!!.success, response.body()!!.message)
            }

        })
    }

    private interface ApiService
    {
        @POST(Constants.REGISTER_USER)
        fun registerUser(
            @Query("api_key") key: String,
            @Body user: User
        ): Call<UserResponse>

        @POST(Constants.CHECK_USER)
        fun checkUser(
            @Query("api_key") key: String,
            @Body user: User
        ): Call<UserResponse>

        @POST(Constants.CHECK_USERNAME)
        fun checkUsername(
            @Query("api_key") key: String,
            @Body user: User
        ): Call<UserResponse>

        @POST(Constants.GET_ALL_CHATS)
        fun getAllChats(
            @Query("api_key") key: String,
            @Body clients: Clients
        ): Call<ChatResponse>

        @POST(Constants.SEND_CHAT)
        fun sendMessage(
            @Query("api_key") key: String,
            @Body body: Chat
        ): Call<SubmitResponse>
    }

    interface OnUserResponseListener
    {
        fun didFetch(success: Boolean, msg: String)
        fun didError(msg: String)
    }

    interface OnChatsResponseListener
    {
        fun didFetch(success: Boolean, msg: String)
        fun didError(msg: String)
    }


    interface OnChatsListener
    {
        fun didFetch(chats: List<Chat>, msg: String)
        fun didError(msg: String)
    }
}
